package com.koxa.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Denys on 2017-05-03.
 */
public class MostFrequentWords {
    private Map<String, Integer> words = new HashMap<>();

    public MostFrequentWords(Map<String, Integer> words) {
        this.words = words;
    }

    public Map<String, Integer> getWords() {
        return words;
    }

    public void setWords(Map<String, Integer> words) {
        this.words = words;
    }
}
