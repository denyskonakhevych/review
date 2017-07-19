package com.koxa.model;

/**
 * Created by Denys on 2017-05-02.
 */
public class ProcessItem {
    private String filePath;
    private boolean withTranslation;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isWithTranslation() {
        return withTranslation;
    }

    public void setWithTranslation(boolean withTranslation) {
        this.withTranslation = withTranslation;
    }
}
