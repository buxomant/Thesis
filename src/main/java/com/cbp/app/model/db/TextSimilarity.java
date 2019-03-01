package com.cbp.app.model.db;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class TextSimilarity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int similarityId;

    @Column
    @NotNull
    private int firstId;

    @Column
    @NotNull
    private int secondId;

    @Column
    @NotNull
    private String timeFrame;

    @Column
    @NotNull
    private String firstType;

    @Column
    @NotNull
    private String secondType;

    @Column
    private double similarityCoefficient;

    public TextSimilarity() { }

    public TextSimilarity(@NotNull int firstId, @NotNull int secondId, @NotNull String timeFrame, @NotNull String firstType, @NotNull String secondType, double similarityCoefficient) {
        this.firstId = firstId;
        this.secondId = secondId;
        this.timeFrame = timeFrame;
        this.firstType = firstType;
        this.secondType = secondType;
        this.similarityCoefficient = similarityCoefficient;
    }

    public int getSimilarityId() {
        return similarityId;
    }

    public void setSimilarityId(int similarityId) {
        this.similarityId = similarityId;
    }

    public int getFirstId() {
        return firstId;
    }

    public void setFirstId(int firstId) {
        this.firstId = firstId;
    }

    public int getSecondId() {
        return secondId;
    }

    public void setSecondId(int secondId) {
        this.secondId = secondId;
    }

    public String getTimeFrame() {
        return timeFrame;
    }

    public void setTimeFrame(String timeFrame) {
        this.timeFrame = timeFrame;
    }

    public String getFirstType() {
        return firstType;
    }

    public void setFirstType(String firstType) {
        this.firstType = firstType;
    }

    public String getSecondType() {
        return secondType;
    }

    public void setSecondType(String secondType) {
        this.secondType = secondType;
    }

    public double getSimilarityCoefficient() {
        return similarityCoefficient;
    }

    public void setSimilarityCoefficient(double similarityCoefficient) {
        this.similarityCoefficient = similarityCoefficient;
    }
}
