package io.olmosjt.saboqbackend.domain;

import io.olmosjt.saboqbackend.domain.enums.ComponentType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ContentWrapper {

    public record Text(
            ComponentType type,
            String content // HTML or Markdown
    ) implements ComponentContent {}

    public record Table(
            ComponentType type,
            List<String> headers,
            List<List<String>> rows,
            boolean isStriped
    ) implements ComponentContent {}

    public record CodeBlock(
            ComponentType type,
            String code,
            String language,
            boolean showLineNumbers
    ) implements ComponentContent {}

    public record Callout(
            ComponentType type,
            String title,
            String message,
            String variant // INFO, WARNING, NOTE
    ) implements ComponentContent {}

    public record Media(
            ComponentType type,
            String url,
            String caption,
            String provider // YOUTUBE, NATIVE
    ) implements ComponentContent {}

    public record Formula(
            ComponentType type,
            String latexExpression,
            boolean isBlock
    ) implements ComponentContent {}


    // ==========================================
    // PRACTICE / INTERACTIVE
    // ==========================================

    public record MultipleChoice(
            ComponentType type,
            String question,
            List<Option> options,
            boolean allowMultipleSelection
    ) implements ComponentContent {
        public record Option(String id, String text, boolean isCorrect) {}
    }

    public record TrueFalse(
            ComponentType type,
            String question,
            boolean correctAnswer // true or false
    ) implements ComponentContent {}

    public record Categorize(
            ComponentType type,
            List<Category> categories,
            List<Item> items
    ) implements ComponentContent {
        public record Category(String id, String name) {}
        public record Item(String id, String text, String correctCategoryId) {}
    }

    public record FillBlanks(
            ComponentType type,
            String textWithPlaceholders, // "Java is {{1}} typed."
            Map<String, String> correctAnswers, // "1" -> "statically"
            List<String> wordBank
    ) implements ComponentContent {}

    public record CodeEditor(
            ComponentType type,
            String language,
            String starterCode,
            String solutionCode,
            List<TestCase> testCases
    ) implements ComponentContent {
        public record TestCase(String input, String expectedOutput, boolean isHidden) {}
    }

    public record Matching(
            ComponentType type,
            List<Pair> pairs
    ) implements ComponentContent {
        public record Pair(String id, String leftText, String rightText) {}
    }

    public record Scenario(
            ComponentType type,
            String startNodeId,
            List<Node> nodes
    ) implements ComponentContent {
        public record Node(String id, String text, String nodeType, List<Option> options) {}
        public record Option(String text, String nextNodeId, String feedback) {}
    }
}
