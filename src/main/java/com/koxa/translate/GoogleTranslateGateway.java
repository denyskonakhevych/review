package com.koxa.translate;

/**
 * Created by Denys on 2017-05-14.
 */
public class GoogleTranslateGateway {

    private static final int MAX_CHARACTERS_PER_REQUEST = 1000;

    public String translate(final String textToTranslate) throws InterruptedException {
        if(textToTranslate.length() / MAX_CHARACTERS_PER_REQUEST > 1)
            return multipartTranslation(textToTranslate);
        else
            return doTranslate(textToTranslate);
    }

    private String multipartTranslation(final String textToTranslate) {
        StringBuilder translatedParts = new StringBuilder();

        int startIndex = 0;
        String part = takePart(textToTranslate, startIndex, MAX_CHARACTERS_PER_REQUEST);
        while (!part.isEmpty()) {
            translatedParts.append(part);
            startIndex += part.length();
            part = takePart(textToTranslate, startIndex, startIndex + MAX_CHARACTERS_PER_REQUEST);
        }
        return translatedParts.toString();
    }

    private String takePart(final String text, int startIndex, int endIndex) {
        String subString = text.substring(startIndex, text.length() > endIndex ? endIndex : text.length());
        return subString.substring(0, subString.lastIndexOf(".") + 1);
    }

    private String doTranslate(final String textToTranslate) {
        try {
            Thread.sleep(200);
            return textToTranslate;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
