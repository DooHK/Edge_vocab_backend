package com.vocab.dto;

import jakarta.validation.constraints.NotBlank;

public class TranslateRequest {

    @NotBlank
    private String text;

    private String source = "en";
    private String target = "ko";

    public String getText() { return text; }
    public String getSource() { return source; }
    public String getTarget() { return target; }

    public void setText(String text) { this.text = text; }
    public void setSource(String source) { this.source = source; }
    public void setTarget(String target) { this.target = target; }
}
