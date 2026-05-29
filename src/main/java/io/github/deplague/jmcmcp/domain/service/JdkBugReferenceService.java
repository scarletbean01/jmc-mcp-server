package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.BugMatch;
import io.github.deplague.jmcmcp.domain.model.BugSignature;
import io.github.deplague.jmcmcp.domain.model.JdkBugReferenceResult;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.*;

import java.util.ArrayList;
import java.util.List;

import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepository.getAccessor;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrQuantityAggregator.count;
import static java.lang.Math.min;
import static java.util.List.of;
import static java.util.Optional.ofNullable;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static org.openjdk.jmc.common.item.ItemFilters.type;

/**
 * Pure domain service for JDK bug cross-reference analysis.
 */
@Slf4j
@ApplicationScoped
public final class JdkBugReferenceService {

    private static final List<BugSignature> BUG_DATABASE = of(
            new BugSignature("JDK-8214231", "C2 compiler crash.*PhaseIdealLoop", of("11.", "17.0.0", "17.0.1", "17.0.2"),
                    "17.0.3", "-XX:CompileCommand=exclude,com/example/Class::method", "CRITICAL", "COMPILER"),
            new BugSignature("JDK-8231223", "java.lang.OutOfMemoryError.*Direct buffer", of("11.", "17."),
                    null, "-XX:MaxDirectMemorySize=512m", "HIGH", "MEMORY"),
            new BugSignature("JDK-8159193", "BiasedLockRevocation", of("11.", "17."),
                    null, "-XX:-UseBiasedLocking", "MEDIUM", "LOCKING"),
            new BugSignature("JDK-8245266", "G1GC.*assert.*should_not_be_here", of("11.", "15."),
                    "16+", "-XX:+UseZGC", "CRITICAL", "GC"),
            new BugSignature("JDK-8268060", "InternalError.*sigsegv.*PhaseIdealLoop", of("11.", "17.0.0", "17.0.1"),
                    "17.0.2", "-XX:TieredStopAtLevel=1", "CRITICAL", "COMPILER"),
            new BugSignature("JDK-8285916", "StackOverflowError.*C2.*compile", of("17.0.0", "17.0.1", "17.0.2"),
                    "17.0.3", "-XX:CompileCommand=exclude,*::*", "HIGH", "COMPILER"),
            new BugSignature("JDK-8297970", "ZGC.*heap.*allocation.*failure", of("17.", "19.", "20."),
                    "21+", null, "HIGH", "GC"),
            new BugSignature("JDK-8277044", "OutOfMemoryError.*Metaspace", of("11.", "17."),
                    null, "-XX:MaxMetaspaceSize=512m", "HIGH", "MEMORY"),
            new BugSignature("JDK-8256829", "CompilerFailure.*C2.*infinite loop", of("11.", "15.", "16."),
                    "17+", "-XX:TieredStopAtLevel=1", "HIGH", "COMPILER"),
            new BugSignature("JDK-8307424", "VirtualThread.*pin.*synchronized", of("21.", "22.0.0"),
                    "22.0.1", "Replace synchronized with ReentrantLock", "MEDIUM", "VIRTUAL_THREADS"),
            new BugSignature("JDK-8313206", "VirtualThread.*carrier.*starvation", of("21.", "22."),
                    null, "Increase -Djdk.virtualThreadScheduler.parallelism", "MEDIUM", "VIRTUAL_THREADS"),
            new BugSignature("JDK-8267693", "G1GC.*humongous.*allocation.*failure", of("11.", "15.", "16."),
                    "17+", "-XX:G1HeapRegionSize=32m", "HIGH", "GC"),
            new BugSignature("JDK-8240576", "InternalError.*C2.*node.*dominates", of("11.", "14.", "15."),
                    "16+", "-XX:CompileCommand=exclude,*::*", "CRITICAL", "COMPILER"),
            new BugSignature("JDK-8285972", "OutOfMemoryError.*GC overhead", of("11.", "17."),
                    null, "Increase -Xmx or reduce heap pressure", "HIGH", "GC"),
            new BugSignature("JDK-8235897", "assert.*failed.*PhaseChaitin", of("11.", "13.", "14."),
                    "15+", "-XX:TieredStopAtLevel=1", "CRITICAL", "COMPILER")
    );

    public JdkBugReferenceResult analyze(IItemCollection events) {
        String jvmVersion = extractJvmVersion(events);

        List<BugMatch> matches = new ArrayList<>();

        IItemCollection compilerFailures = events.apply(type("jdk.CompilerFailure"));
        for (IItemIterable iterable : compilerFailures) {
            IType<?> type1 = iterable.getType();
            IMemberAccessor<Object, IItem> msgAccessor = getAccessor(type1, "failureMessage");
            IType<?> type = iterable.getType();
            IMemberAccessor<Object, IItem> methodAccessor = getAccessor(type, "method");
            if (msgAccessor != null) {
                for (IItem item : iterable) {
                    Object msg = msgAccessor.getMember(item);
                    Object method = methodAccessor != null ? methodAccessor.getMember(item) : null;
                    String text = (msg != null ? msg.toString() : "") + " " + (method != null ? method.toString() : "");
                    findMatches(text, "COMPILER", jvmVersion, matches);
                }
            }
        }

        IItemCollection errorEvents = events.apply(type("jdk.JavaErrorThrow"));
        for (IItemIterable iterable : errorEvents) {
            IType<?> type1 = iterable.getType();
            IMemberAccessor<Object, IItem> classAccessor = getAccessor(type1, "thrownClass");
            IType<?> type = iterable.getType();
            IMemberAccessor<Object, IItem> msgAccessor = getAccessor(type, "message");
            if (classAccessor != null) {
                for (IItem item : iterable) {
                    Object thrownClass = classAccessor.getMember(item);
                    Object msg = msgAccessor != null ? msgAccessor.getMember(item) : null;
                    String className = thrownClass != null ? thrownClass.toString() : "";
                    String message = msg != null ? msg.toString() : "";
                    String text = className + " " + message;
                    String category = className.contains("InternalError") ? "COMPILER" : "MEMORY";
                    findMatches(text, category, jvmVersion, matches);
                }
            }
        }

        IItemCollection biasedRevocations = events.apply(type("jdk.BiasedLockRevocation"));
        long revocationCount = count(biasedRevocations);
        if (revocationCount > 100) {
            BugSignature biasedBug = BUG_DATABASE.stream()
                    .filter(b -> b.id().equals("JDK-8159193"))
                    .findFirst().orElse(null);
            if (biasedBug != null) {
                matches.add(new BugMatch(biasedBug, "BiasedLockRevocation count: " + revocationCount,
                        isVersionAffected(jvmVersion, biasedBug.affectedVersions())));
            }
        }

        return new JdkBugReferenceResult(
                ofNullable(jvmVersion),
                matches,
                count(compilerFailures),
                count(errorEvents),
                revocationCount,
                true
        );
    }

    private String extractJvmVersion(IItemCollection events) {
        IItemCollection propEvents = events.apply(type("jdk.InitialSystemProperty"));
        for (IItemIterable iterable : propEvents) {
            IType<?> type1 = iterable.getType();
            IMemberAccessor<Object, IItem> keyAccessor = getAccessor(type1, "key");
            IType<?> type = iterable.getType();
            IMemberAccessor<Object, IItem> valueAccessor = getAccessor(type, "value");
            if (keyAccessor != null && valueAccessor != null) {
                for (IItem item : iterable) {
                    Object key = keyAccessor.getMember(item);
                    if (key != null && key.toString().equals("java.version")) {
                        Object value = valueAccessor.getMember(item);
                        if (value != null) return value.toString();
                    }
                }
            }
        }

        IItemCollection osEvents = events.apply(type("jdk.OSInformation"));
        for (IItemIterable iterable : osEvents) {
            IType<?> type = iterable.getType();
            IMemberAccessor<Object, IItem> versionAccessor = getAccessor(type, "osVersion");
            if (versionAccessor != null) {
                for (IItem item : iterable) {
                    Object version = versionAccessor.getMember(item);
                    if (version != null) return version.toString();
                }
            }
        }
        return null;
    }

    private void findMatches(String text, String category, String jvmVersion, List<BugMatch> matches) {
        for (BugSignature bug : BUG_DATABASE) {
            if (!bug.category().equals(category) && !bug.category().equals("MEMORY") && !bug.category().equals("COMPILER")) {
                continue;
            }
            try {
                if (compile(bug.pattern(), CASE_INSENSITIVE).matcher(text).find()) {
                    matches.add(new BugMatch(bug, text.substring(0, min(100, text.length())),
                            isVersionAffected(jvmVersion, bug.affectedVersions())));
                }
            } catch (Exception ignored) {
            }
        }
    }

    private boolean isVersionAffected(String jvmVersion, List<String> affectedVersions) {
        if (jvmVersion == null) return false;
        for (String affected : affectedVersions) {
            if (jvmVersion.startsWith(affected)) return true;
        }
        return false;
    }
}
