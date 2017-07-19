package com.koxa.model;

import java.util.Locale;

/**
 * Created by Denys on 2017-05-16.
 */
public class TranslateRequest {
    private String inputLanguage;
    private String outputLanguage;
    private String text;

    public TranslateRequest(ReviewItem reviewItem) {
        this(Locale.ENGLISH.getLanguage(), Locale.FRANCE.getLanguage(), reviewItem.getText());
    }

    public TranslateRequest(String inputLanguage, String outputLanguage, String text) {
        this.inputLanguage = inputLanguage;
        this.outputLanguage = outputLanguage;
        this.text = text;
    }

    public String getInputLanguage() {
        return inputLanguage;
    }

    public void setInputLanguage(String inputLanguage) {
        this.inputLanguage = inputLanguage;
    }

    public String getOutputLanguage() {
        return outputLanguage;
    }

    public void setOutputLanguage(String outputLanguage) {
        this.outputLanguage = outputLanguage;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
