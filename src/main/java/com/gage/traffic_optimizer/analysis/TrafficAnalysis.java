package com.gage.traffic_optimizer.analysis;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "traffic_analyses")
public class TrafficAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String intersectionName;

    private int northbound;
    private int southbound;
    private int eastbound;
    private int westbound;

    private int currentNorthSouthGreen;
    private int currentEastWestGreen;

    private int currentCycleLength;
    private int recommendedCycleLength;

    private int recommendedNorthSouthGreen;
    private int recommendedEastWestGreen;

    private int northSouthCriticalVolume;
    private int eastWestCriticalVolume;

    private double criticalFlowRatio;

    private double currentEstimatedWait;
    private double optimizedEstimatedWait;
    private double improvementPercent;

    private LocalDateTime createdAt;

    public TrafficAnalysis() {
    }

    public Long getId() {
        return id;
    }

    public String getIntersectionName() {
        return intersectionName;
    }

    public void setIntersectionName(String intersectionName) {
        this.intersectionName = intersectionName;
    }

    public int getNorthbound() {
        return northbound;
    }

    public void setNorthbound(int northbound) {
        this.northbound = northbound;
    }

    public int getSouthbound() {
        return southbound;
    }

    public void setSouthbound(int southbound) {
        this.southbound = southbound;
    }

    public int getEastbound() {
        return eastbound;
    }

    public void setEastbound(int eastbound) {
        this.eastbound = eastbound;
    }

    public int getWestbound() {
        return westbound;
    }

    public void setWestbound(int westbound) {
        this.westbound = westbound;
    }

    public int getCurrentNorthSouthGreen() {
        return currentNorthSouthGreen;
    }

    public void setCurrentNorthSouthGreen(int currentNorthSouthGreen) {
        this.currentNorthSouthGreen = currentNorthSouthGreen;
    }

    public int getCurrentEastWestGreen() {
        return currentEastWestGreen;
    }

    public void setCurrentEastWestGreen(int currentEastWestGreen) {
        this.currentEastWestGreen = currentEastWestGreen;
    }

    public int getCurrentCycleLength() {
        return currentCycleLength;
    }

    public void setCurrentCycleLength(int currentCycleLength) {
        this.currentCycleLength = currentCycleLength;
    }

    public int getRecommendedCycleLength() {
        return recommendedCycleLength;
    }

    public void setRecommendedCycleLength(int recommendedCycleLength) {
        this.recommendedCycleLength = recommendedCycleLength;
    }

    public int getRecommendedNorthSouthGreen() {
        return recommendedNorthSouthGreen;
    }

    public void setRecommendedNorthSouthGreen(int recommendedNorthSouthGreen) {
        this.recommendedNorthSouthGreen = recommendedNorthSouthGreen;
    }

    public int getRecommendedEastWestGreen() {
        return recommendedEastWestGreen;
    }

    public void setRecommendedEastWestGreen(int recommendedEastWestGreen) {
        this.recommendedEastWestGreen = recommendedEastWestGreen;
    }

    public int getNorthSouthCriticalVolume() {
        return northSouthCriticalVolume;
    }

    public void setNorthSouthCriticalVolume(int northSouthCriticalVolume) {
        this.northSouthCriticalVolume = northSouthCriticalVolume;
    }

    public int getEastWestCriticalVolume() {
        return eastWestCriticalVolume;
    }

    public void setEastWestCriticalVolume(int eastWestCriticalVolume) {
        this.eastWestCriticalVolume = eastWestCriticalVolume;
    }

    public double getCriticalFlowRatio() {
        return criticalFlowRatio;
    }

    public void setCriticalFlowRatio(double criticalFlowRatio) {
        this.criticalFlowRatio = criticalFlowRatio;
    }

    public double getCurrentEstimatedWait() {
        return currentEstimatedWait;
    }

    public void setCurrentEstimatedWait(double currentEstimatedWait) {
        this.currentEstimatedWait = currentEstimatedWait;
    }

    public double getOptimizedEstimatedWait() {
        return optimizedEstimatedWait;
    }

    public void setOptimizedEstimatedWait(double optimizedEstimatedWait) {
        this.optimizedEstimatedWait = optimizedEstimatedWait;
    }

    public double getImprovementPercent() {
        return improvementPercent;
    }

    public void setImprovementPercent(double improvementPercent) {
        this.improvementPercent = improvementPercent;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}