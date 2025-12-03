package com.example.remember_words.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateWordDto {

    @NotBlank
    Long id;
    
    @NotBlank
    String foreignWord;

    @NotBlank
    String translatedWord;

    Long groupId;

    public CreateWordDto(String foreignWord, String translatedWord, Long groupId) {
        this.foreignWord = foreignWord;
        this.translatedWord = translatedWord;
        this.groupId = groupId;
    }

    public CreateWordDto(Long id, String foreignWord, String translatedWord) {
        this.id = id;
        this.foreignWord = foreignWord;
        this.translatedWord = translatedWord;
    }

    public String getForeignWord() {
        return foreignWord;
    }
    public void setForeignWord(String foreignWord) {
        this.foreignWord = foreignWord;
    }
    public String getTranslatedWord() {
        return translatedWord;
    }
    public void setTranslatedWord(String translatedWord) {
        this.translatedWord = translatedWord;
    }
    public Long getGroupId() {
        return groupId;
    }
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
}
