package com.koxa.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Denys on 2017-05-03.
 */
public class MostCommentedItems {
    private Map<String, Integer> items = new HashMap<>();

    public MostCommentedItems(Map<String, Integer> items) {
        this.items = items;
    }

    public Map<String, Integer> getItems() {
        return items;
    }

    public void setItems(Map<String, Integer> items) {
        this.items = items;
    }
}
