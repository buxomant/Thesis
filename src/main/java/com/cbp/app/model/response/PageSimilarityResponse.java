package com.cbp.app.model.response;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PageSimilarityResponse {
    @Id
    private int similarityId;
    private double similarityCoefficient;
    private int firstPageId;
    private int secondPageId;
    private String firstWebsiteUrl;
    private String secondWebsiteUrl;
    private String firstPageUrl;
    private String secondPageUrl;

    public PageSimilarityResponse() { }

    public PageSimilarityResponse(int similarityId, double similarityCoefficient, int firstPageId, int secondPageId, String firstWebsiteUrl, String secondWebsiteUrl, String firstPageUrl, String secondPageUrl) {
        this.similarityId = similarityId;
        this.similarityCoefficient = similarityCoefficient;
        this.firstPageId = firstPageId;
        this.secondPageId = secondPageId;
        this.firstWebsiteUrl = firstWebsiteUrl;
        this.secondWebsiteUrl = secondWebsiteUrl;
        this.firstPageUrl = firstPageUrl;
        this.secondPageUrl = secondPageUrl;
    }

    public int getSimilarityId() {
        return similarityId;
    }

    public void setSimilarityId(int similarityId) {
        this.similarityId = similarityId;
    }

    public double getSimilarityCoefficient() {
        return similarityCoefficient;
    }

    public void setSimilarityCoefficient(double similarityCoefficient) {
        this.similarityCoefficient = similarityCoefficient;
    }

    public int getFirstPageId() {
        return firstPageId;
    }

    public void setFirstPageId(int firstPageId) {
        this.firstPageId = firstPageId;
    }

    public int getSecondPageId() {
        return secondPageId;
    }

    public void setSecondPageId(int secondPageId) {
        this.secondPageId = secondPageId;
    }

    public String getFirstWebsiteUrl() {
        return firstWebsiteUrl;
    }

    public void setFirstWebsiteUrl(String firstWebsiteUrl) {
        this.firstWebsiteUrl = firstWebsiteUrl;
    }

    public String getSecondWebsiteUrl() {
        return secondWebsiteUrl;
    }

    public void setSecondWebsiteUrl(String secondWebsiteUrl) {
        this.secondWebsiteUrl = secondWebsiteUrl;
    }

    public String getFirstPageUrl() {
        return firstPageUrl;
    }

    public void setFirstPageUrl(String firstPageUrl) {
        this.firstPageUrl = firstPageUrl;
    }

    public String getSecondPageUrl() {
        return secondPageUrl;
    }

    public void setSecondPageUrl(String secondPageUrl) {
        this.secondPageUrl = secondPageUrl;
    }
}
