package io.olmosjt.saboqbackend.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.olmosjt.saboqbackend.domain.enums.ComponentType;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = true // Allows the 'type' field to be passed to the Record constructor
)
@JsonSubTypes({
        // --- DISPLAY ---
        @JsonSubTypes.Type(value = ContentWrapper.Text.class, name = "TEXT"),
        @JsonSubTypes.Type(value = ContentWrapper.Table.class, name = "TABLE"),
        @JsonSubTypes.Type(value = ContentWrapper.CodeBlock.class, name = "CODE_BLOCK"),
        @JsonSubTypes.Type(value = ContentWrapper.Callout.class, name = "CALLOUT"),
        @JsonSubTypes.Type(value = ContentWrapper.Media.class, name = "IMAGE"),
        @JsonSubTypes.Type(value = ContentWrapper.Media.class, name = "VIDEO"),
        @JsonSubTypes.Type(value = ContentWrapper.Media.class, name = "AUDIO"),
        @JsonSubTypes.Type(value = ContentWrapper.Formula.class, name = "FORMULA"),

        // --- PRACTICE ---
        @JsonSubTypes.Type(value = ContentWrapper.MultipleChoice.class, name = "MULTIPLE_CHOICE"),
        @JsonSubTypes.Type(value = ContentWrapper.TrueFalse.class, name = "TRUE_FALSE"),
        @JsonSubTypes.Type(value = ContentWrapper.Categorize.class, name = "CATEGORIZE"),
        @JsonSubTypes.Type(value = ContentWrapper.FillBlanks.class, name = "FILL_BLANKS"),
        @JsonSubTypes.Type(value = ContentWrapper.CodeEditor.class, name = "CODE_EDITOR"),
        @JsonSubTypes.Type(value = ContentWrapper.Matching.class, name = "MATCHING"),
        @JsonSubTypes.Type(value = ContentWrapper.Scenario.class, name = "SCENARIO")
})
public sealed interface ComponentContent permits
        ContentWrapper.Text,
        ContentWrapper.Table,
        ContentWrapper.CodeBlock,
        ContentWrapper.Callout,
        ContentWrapper.Media,
        ContentWrapper.Formula,
        ContentWrapper.MultipleChoice,
        ContentWrapper.TrueFalse,
        ContentWrapper.Categorize,
        ContentWrapper.FillBlanks,
        ContentWrapper.CodeEditor,
        ContentWrapper.Matching,
        ContentWrapper.Scenario {


    ComponentType type();
}
