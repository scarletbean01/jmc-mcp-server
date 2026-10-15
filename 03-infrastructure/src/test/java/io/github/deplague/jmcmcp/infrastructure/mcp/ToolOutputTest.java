package io.github.deplague.jmcmcp.infrastructure.mcp;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ToolOutputTest {

    @Test
    void shortTextIsReturnedUnchanged() {
        assertThat(ToolOutput.limit("hello", 100)).isEqualTo("hello");
    }

    @Test
    void truncatedOutputStaysWithinBudgetAndKeepsHint() {
        String hint = "<agent_hint>Try `hot_methods`.</agent_hint>";
        String text = "row\n".repeat(5_000) + hint;

        String result = ToolOutput.limit(text, 1_000);

        assertThat(result.length()).isLessThanOrEqualTo(1_000);
        assertThat(result).contains("Output truncated").endsWith(hint);
    }

    @Test
    void oversizedHintDoesNotBlowTheBudget() {
        String hint = "<agent_hint>" + "x".repeat(2_000) + "</agent_hint>";
        String text = "row\n".repeat(2_000) + hint;

        assertThat(ToolOutput.limit(text, 1_000).length()).isLessThanOrEqualTo(1_000);
    }

    @Test
    void unclosedCodeFenceIsClosed() {
        String text = "```\n" + "line\n".repeat(2_000);

        String result = ToolOutput.limit(text, 1_000);

        assertThat(result.length()).isLessThanOrEqualTo(1_000);
        assertThat(result.split("```", -1).length - 1).isEven();
    }

    @Test
    void tinyBudgetsAreRaisedToTheMinimumInsteadOfThrowing() {
        String text = "x".repeat(300);
        assertThat(ToolOutput.limit(text, 10)).isEqualTo(text);

        String big = "row\n".repeat(2_000);
        assertThat(ToolOutput.limit(big, 10).length()).isLessThanOrEqualTo(ToolOutput.MIN_MAX_CHARS);
    }
}
