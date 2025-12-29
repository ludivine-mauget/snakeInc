package org.snakeinc.api.controller;

import jakarta.validation.Valid;
import org.snakeinc.api.model.ScoreDto;
import org.snakeinc.api.model.ScoreParams;
import org.snakeinc.api.service.ScoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/scores")
public class ScoreController {

    private final ScoreService scoreService;

    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @PostMapping
    public ResponseEntity<ScoreDto> createScore(@RequestBody @Valid ScoreParams params) {
        ScoreDto score = scoreService.createScore(params);
        return ResponseEntity.status(HttpStatus.CREATED).body(score);
    }
}

