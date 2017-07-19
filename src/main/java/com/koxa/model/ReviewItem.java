package com.koxa.model;

/**
 * Created by Denys on 2017-05-02.
 */
public class ReviewItem {
    private String id;
    private String productId;
    private String userId;
    private String profileName;
    private int helpfulnessNumerator;
    private int helpfulnessDenominator;
    private int score;
    private long time;
    private String summary;
    private String text;

    public ReviewItem(String id, String productId, String userId, String profileName, Integer helpfulnessNumerator, int helpfulnessDenominator, int score, long time, String summary, String text) {
        this.id = id;
        this.productId = productId;
        this.userId = userId;
        this.profileName = profileName;
        this.helpfulnessNumerator = helpfulnessNumerator;
        this.helpfulnessDenominator = helpfulnessDenominator;
        this.score = score;
        this.time = time;
        this.summary = summary;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public ReviewItem setId(String id) {
        this.id = id;
        return this;
    }

    public String getProductId() {
        return productId;
    }

    public ReviewItem setProductId(String productId) {
        this.productId = productId;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public ReviewItem setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getProfileName() {
        return profileName;
    }

    public ReviewItem setProfileName(String profileName) {
        this.profileName = profileName;
        return this;
    }

    public Integer getHelpfulnessNumerator() {
        return helpfulnessNumerator;
    }

    public ReviewItem setHelpfulnessNumerator(Integer helpfulnessNumerator) {
        this.helpfulnessNumerator = helpfulnessNumerator;
        return this;
    }

    public int getHelpfulnessDenominator() {
        return helpfulnessDenominator;
    }

    public ReviewItem setHelpfulnessDenominator(int helpfulnessDenominator) {
        this.helpfulnessDenominator = helpfulnessDenominator;
        return this;
    }

    public int getScore() {
        return score;
    }

    public ReviewItem setScore(int score) {
        this.score = score;
        return this;
    }

    public long getTime() {
        return time;
    }

    public ReviewItem setTime(long time) {
        this.time = time;
        return this;
    }

    public String getSummary() {
        return summary;
    }

    public ReviewItem setSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public String getText() {
        return text;
    }

    public ReviewItem setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public String toString() {
        return "ReviewItem{" +
                "id='" + id + '\'' +
                ", productId='" + productId + '\'' +
                ", userId='" + userId + '\'' +
                ", profileName='" + profileName + '\'' +
                ", helpfulnessNumerator=" + helpfulnessNumerator +
                ", helpfulnessDenominator=" + helpfulnessDenominator +
                ", score=" + score +
                ", time=" + time +
                ", summary='" + summary + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
