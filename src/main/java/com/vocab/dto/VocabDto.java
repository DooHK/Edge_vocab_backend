package com.vocab.dto;

import com.vocab.entity.Vocabulary;
import jakarta.validation.constraints.NotBlank;

public class VocabDto {

    private Long id;

    @NotBlank
    private String word;

    @NotBlank
    private String translation;

    private String date;

    public VocabDto() {}

    public static VocabDto from(Vocabulary v) {
        VocabDto dto = new VocabDto();
        dto.id = v.getId();
        dto.word = v.getWord();
        dto.translation = v.getTranslation();
        dto.date = v.getAddedDate();
        return dto;
    }

    public Long getId() { return id; }
    public String getWord() { return word; }
    public String getTranslation() { return translation; }
    public String getDate() { return date; }

    public void setWord(String word) { this.word = word; }
    public void setTranslation(String translation) { this.translation = translation; }
    public void setDate(String date) { this.date = date; }
}
