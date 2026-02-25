package com.vocab.dto;

public class TranslateResponse {

    private String original;
    private String translated;
    private String detectedLanguage;

    public TranslateResponse() {}

    public TranslateResponse(String original, String translated, String detectedLanguage) {
        this.original = original;
        this.translated = translated;
        this.detectedLanguage = detectedLanguage;
    }

    public String getOriginal() { return original; }
    public String getTranslated() { return translated; }
    public String getDetectedLanguage() { return detectedLanguage; }
}
