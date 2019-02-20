package com.cbp.app.model.db;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class WebsiteTextSimilarity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int similarityId;

    @Column
    @NotNull
    private int firstWebsiteId;

    @Column
    @NotNull
    private int secondWebsiteId;

    @Column
    @NotNull
    private String timeFrame;

    @Column
    private double similarityCoefficient;

    public WebsiteTextSimilarity() { }

    public WebsiteTextSimilarity(@NotNull int firstWebsiteId, @NotNull int secondWebsiteId, @NotNull String timeFrame, double similarityCoefficient) {
        this.firstWebsiteId = firstWebsiteId;
        this.secondWebsiteId = secondWebsiteId;
        this.timeFrame = timeFrame;
        this.similarityCoefficient = similarityCoefficient;
    }

    public int getSimilarityId() {
        return similarityId;
    }

    public void setSimilarityId(int similarityId) {
        this.similarityId = similarityId;
    }

    public int getFirstWebsiteId() {
        return firstWebsiteId;
    }

    public void setFirstWebsiteId(int firstWebsiteId) {
        this.firstWebsiteId = firstWebsiteId;
    }

    public int getSecondWebsiteId() {
        return secondWebsiteId;
    }

    public void setSecondWebsiteId(int secondWebsiteId) {
        this.secondWebsiteId = secondWebsiteId;
    }

    public String getTimeFrame() {
        return timeFrame;
    }

    public void setTimeFrame(String timeFrame) {
        this.timeFrame = timeFrame;
    }

    public double getSimilarityCoefficient() {
        return similarityCoefficient;
    }

    public void setSimilarityCoefficient(double similarityCoefficient) {
        this.similarityCoefficient = similarityCoefficient;
    }
}
