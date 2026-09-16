package com.gage.traffic_optimizer.analysis;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/traffic")
@CrossOrigin(origins = "http://localhost:5173")
public class TrafficController {

    private final TrafficOptimizationService optimizationService;
    private final TrafficAnalysisRepository repository;
    private final TrafficCsvService csvService;

    public TrafficController(
            TrafficOptimizationService optimizationService,
            TrafficAnalysisRepository repository,
            TrafficCsvService csvService) {

        this.optimizationService = optimizationService;
        this.repository = repository;
        this.csvService = csvService;
    }

    @PostMapping("/analyze")
    public TrafficAnalysis analyze(
            @Valid @RequestBody TrafficRequest request) {

        return optimizationService.analyze(request);
    }

    @GetMapping("/analyses")
    public List<TrafficAnalysis> getAllAnalyses() {
        return repository.findAll();
    }

    @GetMapping("/analyses/{id}")
    public ResponseEntity<TrafficAnalysis> getAnalysis(
            @PathVariable Long id) {

        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
    }

    @PostMapping("/upload")
    public ResponseEntity<List<TrafficAnalysis>> uploadCsv(
            @RequestParam("file") MultipartFile file) {

        try {
            return ResponseEntity.ok(
                    csvService.processCsv(file)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}