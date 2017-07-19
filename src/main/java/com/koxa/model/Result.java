package com.koxa.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Denys on 2017-05-09.
 */
public class Result {

    private Map<String, Integer> activeUsers = new ConcurrentHashMap<>();
    private Map<String, Integer> commentedItems = new ConcurrentHashMap<>();
    private Map<String, Integer> words = new ConcurrentHashMap<>();

    public Result(Map<String, Integer> activeUsers, Map<String, Integer> commentedItems, Map<String, Integer> words) {
        this.activeUsers = activeUsers;
        this.commentedItems = commentedItems;
        this.words = words;
    }

    public Map<String, Integer> getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(Map<String, Integer> activeUsers) {
        this.activeUsers = activeUsers;
    }

    public Map<String, Integer> getCommentedItems() {
        return commentedItems;
    }

    public void setCommentedItems(Map<String, Integer> commentedItems) {
        this.commentedItems = commentedItems;
    }

    public Map<String, Integer> getWords() {
        return words;
    }

    public void setWords(Map<String, Integer> words) {
        this.words = words;
    }
}
