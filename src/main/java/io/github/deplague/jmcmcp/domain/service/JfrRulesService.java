package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.JfrRuleEntry;
import io.github.deplague.jmcmcp.domain.model.JfrRulesResult;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.RunnableFuture;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.flightrecorder.rules.IResult;
import org.openjdk.jmc.flightrecorder.rules.IRule;
import org.openjdk.jmc.flightrecorder.rules.RuleRegistry;
import org.openjdk.jmc.flightrecorder.rules.Severity;

/**
 * Pure domain service for JMC rules engine analysis.
 */
@Slf4j
public final class JfrRulesService {

    public JfrRulesResult analyze(IItemCollection events, String minSevStr) {
        Severity threshold;
        try {
            threshold = Severity.valueOf(minSevStr);
        } catch (IllegalArgumentException e) {
            threshold = Severity.WARNING;
        }

        List<JfrRuleEntry> significantResults = new ArrayList<>();
        for (IRule rule : RuleRegistry.getRules()) {
            try {
                RunnableFuture<IResult> future = rule.createEvaluation(events, null, null);
                future.run();
                IResult r = future.get();

                if (r.getSeverity().compareTo(threshold) >= 0) {
                    significantResults.add(new JfrRuleEntry(
                            rule.getName(),
                            r.getSeverity().getLocalizedName(),
                            r.getSummary(),
                            r.getExplanation()
                    ));
                }
            } catch (Exception ex) {
                // Skip rules that fail
            }
        }

        significantResults.sort((a, b) -> {
            Severity sa = Severity.valueOf(a.severity().toUpperCase());
            Severity sb1 = Severity.valueOf(b.severity().toUpperCase());
            return sb1.compareTo(sa);
        });

        return new JfrRulesResult(
                significantResults,
                threshold.getLocalizedName(),
                !significantResults.isEmpty()
        );
    }
}
