package com.gage.traffic_optimizer.analysis;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TrafficAnalysisRepository
        extends JpaRepository<TrafficAnalysis, Long> {
}