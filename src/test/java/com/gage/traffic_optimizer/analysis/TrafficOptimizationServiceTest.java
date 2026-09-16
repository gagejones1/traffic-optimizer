package com.gage.traffic_optimizer.analysis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TrafficOptimizationServiceTest {

    private TrafficAnalysisRepository repository;
    private TrafficOptimizationService service;

    @BeforeEach
    void setUp() {
        repository = mock(TrafficAnalysisRepository.class);

        when(repository.save(any(TrafficAnalysis.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        service = new TrafficOptimizationService(repository);
    }

    @Test
    void shouldOptimizeNorthSouthHeavyTraffic() {
        TrafficRequest request = new TrafficRequest(
                "Main St and 1st Ave",
                420,
                380,
                130,
                160,
                30,
                30
        );

        TrafficAnalysis result = service.analyze(request);

        assertEquals(60, result.getRecommendedCycleLength());
        assertEquals(35, result.getRecommendedNorthSouthGreen());
        assertEquals(15, result.getRecommendedEastWestGreen());

        assertTrue(
                result.getOptimizedEstimatedWait()
                        < result.getCurrentEstimatedWait()
        );

        assertTrue(result.getImprovementPercent() > 0);

        verify(repository).save(any(TrafficAnalysis.class));
    }

    @Test
    void shouldGiveMoreGreenTimeToEastWestWhenTrafficIsHeavier() {
        TrafficRequest request = new TrafficRequest(
                "Highway Test",
                200,
                180,
                500,
                450,
                30,
                30
        );

        TrafficAnalysis result = service.analyze(request);

        assertTrue(
                result.getRecommendedEastWestGreen()
                        > result.getRecommendedNorthSouthGreen()
        );
    }

    @Test
    void shouldHandleZeroTraffic() {
        TrafficRequest request = new TrafficRequest(
                "Empty Intersection",
                0,
                0,
                0,
                0,
                30,
                30
        );

        TrafficAnalysis result = service.analyze(request);

        assertEquals(0, result.getCurrentEstimatedWait());
        assertEquals(0, result.getOptimizedEstimatedWait());
        assertEquals(0, result.getImprovementPercent());
    }
}