package io.olmosjt.saboqbackend.domain.enums;

public enum ComponentType {
    // --- DISPLAY / TEACHING ---
    TEXT,               // Plain text or formatted content
    TABLE,              // Tabular data
    CODE_BLOCK,         // Just for reading (syntax highlighting), not executing
    CALLOUT,            // Covers: Info, Warning, Note
    IMAGE,              // Static images
    VIDEO,              // Video clips or lectures
    AUDIO,              // Audio recordings (e.g., pronunciation, storytelling)
    ANIMATION,          // GIFs or step-by-step visual demonstrations
    INTERACTIVE_GRAPH,  // Charts/graphs that users can manipulate
    FORMULA,            // Mathematical or scientific formulas (LaTeX)

    // --- PRACTICE / INTERACTIVE ---
    MULTIPLE_CHOICE,    // Single or multiple correct answers
    TRUE_FALSE,         // Boolean questions
    CATEGORIZE,         // Drag and drop items into buckets
    FILL_BLANKS,        // "Java is a [__] language"
    FILL_LETTERS,       // "O_j_ct" -> "Object"
    FLIP_CARD,          // Vocabulary flashcards
    CODE_EDITOR,        // Executable code exercises
    DRAG_DROP_ON_TEXT,  // Highlight words/phrases and move to categories
    MATCHING,           // Match terms to definitions
    SORTING,            // Arrange items in correct order
    SIMULATION,         // Interactive simulations or labs
    QUIZ_COMPOSITE,     // Multiple question types combined
    SCENARIO,           // Branching story/problem-solving exercises

    // --- OTHER / FEEDBACK ---
    SURVEY,             // Polls, reflection prompts, feedback forms
    ANNOTATION          // Highlighting or commenting on content
}
