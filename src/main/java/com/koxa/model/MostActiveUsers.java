package com.koxa.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Denys on 2017-05-03.
 */
public class MostActiveUsers {
    private Map<String, Integer> activeUsers = new HashMap<>();

    public MostActiveUsers(Map<String, Integer> activeUsers) {
        this.activeUsers = activeUsers;
    }

    public Map<String, Integer> getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(Map<String, Integer> activeUsers) {
        this.activeUsers = activeUsers;
    }
}
