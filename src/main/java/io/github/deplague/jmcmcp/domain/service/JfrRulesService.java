package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.JfrRuleEntry;
import io.github.deplague.jmcmcp.domain.model.JfrRulesResult;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.flightrecorder.rules.IResult;
import org.openjdk.jmc.flightrecorder.rules.IRule;
import org.openjdk.jmc.flightrecorder.rules.Severity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.openjdk.jmc.flightrecorder.rules.RuleRegistry.getRules;
import static org.openjdk.jmc.flightrecorder.rules.Severity.WARNING;
import static org.openjdk.jmc.flightrecorder.rules.Severity.valueOf;

/**
 * Pure domain service for JMC rules engine analysis.
 */
@Slf4j
@ApplicationScoped
public final class JfrRulesService {

    public JfrRulesResult analyze(IItemCollection events, String minSevStr) {
        Severity threshold;
        try {
            threshold = valueOf(minSevStr);
        } catch (IllegalArgumentException e) {
            threshold = WARNING;
        }

        List<JfrRuleEntry> significantResults = new ArrayList<>();
        List<Future<IResult>> futures = new ArrayList<>();

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (IRule rule : getRules()) {
                try {
                    RunnableFuture<IResult> future = rule.createEvaluation(events, null, null);
                    futures.add(executor.submit(() -> {
                        future.run();
                        return future.get();
                    }));
                } catch (Exception ex) {
                    // Skip rules that fail creation
                }
            }

            for (Future<IResult> future : futures) {
                try {
                    IResult r = future.get();
                    if (r != null && r.getSeverity() != null && r.getSeverity().compareTo(threshold) >= 0) {
                        significantResults.add(new JfrRuleEntry(
                                r.getRule() != null ? r.getRule().getName() : "Unknown",
                                r.getSeverity().getLocalizedName(),
                                r.getSummary(),
                                r.getExplanation()
                        ));
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    // Skip rules that fail during evaluation
                }
            }
        }

        significantResults.sort((a, b) -> {
            Severity sa = valueOf(a.severity().toUpperCase());
            Severity sb1 = valueOf(b.severity().toUpperCase());
            return sb1.compareTo(sa);
        });

        return new JfrRulesResult(
                significantResults,
                threshold.getLocalizedName(),
                !significantResults.isEmpty()
        );
    }
}
