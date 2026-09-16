package com.gage.traffic_optimizer.analysis;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

@Service
public class TrafficOptimizationService {

    private static final double SATURATION_FLOW = 1900.0;
    private static final int LOST_TIME = 10;
    private static final int MIN_CYCLE = 60;
    private static final int MAX_CYCLE = 180;
    private static final int MIN_GREEN = 15;

    private final TrafficAnalysisRepository repository;

    public TrafficOptimizationService(
            TrafficAnalysisRepository repository) {

        this.repository = repository;
    }

    public TrafficAnalysis analyze(TrafficRequest request) {

        int nsCritical = Math.max(
                request.northbound(),
                request.southbound()
        );

        int ewCritical = Math.max(
                request.eastbound(),
                request.westbound()
        );

        double nsFlowRatio =
                nsCritical / SATURATION_FLOW;

        double ewFlowRatio =
                ewCritical / SATURATION_FLOW;

        double criticalFlowRatio =
                nsFlowRatio + ewFlowRatio;

        int currentCycle =
                request.currentNorthSouthGreen()
                + request.currentEastWestGreen()
                + LOST_TIME;

        int recommendedCycle =
                calculateWebsterCycle(criticalFlowRatio);

        int availableGreen =
                recommendedCycle - LOST_TIME;

        int recommendedNS;
        int recommendedEW;

        if (criticalFlowRatio == 0) {

            recommendedNS = availableGreen / 2;
            recommendedEW =
                    availableGreen - recommendedNS;

        } else {

            recommendedNS =
                    (int) Math.round(
                            availableGreen
                            * (nsFlowRatio / criticalFlowRatio)
                    );

            recommendedNS =
                    Math.max(MIN_GREEN, recommendedNS);

            recommendedNS =
                    Math.min(
                            availableGreen - MIN_GREEN,
                            recommendedNS
                    );

            recommendedEW =
                    availableGreen - recommendedNS;
        }

        double currentWait =
                calculateIntersectionDelay(
                        request,
                        currentCycle,
                        request.currentNorthSouthGreen(),
                        request.currentEastWestGreen()
                );

        double optimizedWait =
                calculateIntersectionDelay(
                        request,
                        recommendedCycle,
                        recommendedNS,
                        recommendedEW
                );

        double improvement = 0;

        if (currentWait > 0) {
            improvement =
                    ((currentWait - optimizedWait)
                            / currentWait) * 100;
        }

        TrafficAnalysis analysis =
                new TrafficAnalysis();

        analysis.setIntersectionName(
                request.intersectionName());

        analysis.setNorthbound(
                request.northbound());

        analysis.setSouthbound(
                request.southbound());

        analysis.setEastbound(
                request.eastbound());

        analysis.setWestbound(
                request.westbound());

        analysis.setCurrentNorthSouthGreen(
                request.currentNorthSouthGreen());

        analysis.setCurrentEastWestGreen(
                request.currentEastWestGreen());

        analysis.setCurrentCycleLength(
                currentCycle);

        analysis.setRecommendedCycleLength(
                recommendedCycle);

        analysis.setRecommendedNorthSouthGreen(
                recommendedNS);

        analysis.setRecommendedEastWestGreen(
                recommendedEW);

        analysis.setNorthSouthCriticalVolume(
                nsCritical);

        analysis.setEastWestCriticalVolume(
                ewCritical);

        analysis.setCriticalFlowRatio(
                round(criticalFlowRatio));

        analysis.setCurrentEstimatedWait(
                round(currentWait));

        analysis.setOptimizedEstimatedWait(
                round(optimizedWait));

        analysis.setImprovementPercent(
                round(improvement));

        analysis.setCreatedAt(
                LocalDateTime.now());

        return repository.save(analysis);
    }

    private int calculateWebsterCycle(
            double flowRatio) {

        if (flowRatio >= 0.95) {
            return MAX_CYCLE;
        }

        double cycle =
                (1.5 * LOST_TIME + 5)
                / (1 - flowRatio);

        int rounded =
                (int) (Math.ceil(cycle / 5.0) * 5);

        return Math.max(
                MIN_CYCLE,
                Math.min(MAX_CYCLE, rounded)
        );
    }

    private double calculateIntersectionDelay(
            TrafficRequest request,
            int cycle,
            int nsGreen,
            int ewGreen) {

        int totalVolume =
                request.northbound()
                + request.southbound()
                + request.eastbound()
                + request.westbound();

        if (totalVolume == 0) {
            return 0;
        }

        double totalDelay = 0;

        totalDelay +=
                request.northbound()
                * calculateUniformDelay(
                        request.northbound(),
                        nsGreen,
                        cycle
                );

        totalDelay +=
                request.southbound()
                * calculateUniformDelay(
                        request.southbound(),
                        nsGreen,
                        cycle
                );

        totalDelay +=
                request.eastbound()
                * calculateUniformDelay(
                        request.eastbound(),
                        ewGreen,
                        cycle
                );

        totalDelay +=
                request.westbound()
                * calculateUniformDelay(
                        request.westbound(),
                        ewGreen,
                        cycle
                );

        return totalDelay / totalVolume;
    }

    private double calculateUniformDelay(
            int volume,
            int green,
            int cycle) {

        if (volume == 0) {
            return 0;
        }

        double greenRatio =
                (double) green / cycle;

        double capacity =
                SATURATION_FLOW * greenRatio;

        double saturation =
                volume / capacity;

        double x =
                Math.min(saturation, 1.0);

        double denominator =
                2 * (1 - greenRatio * x);

        if (denominator <= 0) {
            return cycle;
        }

        return cycle
                * Math.pow(1 - greenRatio, 2)
                / denominator;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}