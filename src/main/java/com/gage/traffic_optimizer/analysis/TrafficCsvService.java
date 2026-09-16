package com.gage.traffic_optimizer.analysis;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class TrafficCsvService {

    private final TrafficOptimizationService optimizationService;

    public TrafficCsvService(
            TrafficOptimizationService optimizationService) {

        this.optimizationService = optimizationService;
    }

    public List<TrafficAnalysis> processCsv(
            MultipartFile file) throws Exception {

        List<TrafficAnalysis> results = new ArrayList<>();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream())
        );

        String line;
        boolean firstLine = true;

        while ((line = reader.readLine()) != null) {

            if (firstLine) {
                firstLine = false;
                continue;
            }

            if (line.isBlank()) {
                continue;
            }

            String[] values = line.split(",");

            TrafficRequest request = new TrafficRequest(
                    values[0].trim(),
                    Integer.parseInt(values[1].trim()),
                    Integer.parseInt(values[2].trim()),
                    Integer.parseInt(values[3].trim()),
                    Integer.parseInt(values[4].trim()),
                    Integer.parseInt(values[5].trim()),
                    Integer.parseInt(values[6].trim())
            );

            results.add(
                    optimizationService.analyze(request)
            );
        }

        return results;
    }
}