package org.snakeinc.api.controller;

import jakarta.validation.Valid;
import org.snakeinc.api.model.PlayerDto;
import org.snakeinc.api.model.PlayerParams;
import org.snakeinc.api.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping
    public ResponseEntity<PlayerDto> createPlayer(@RequestBody @Valid PlayerParams params) {
        PlayerDto player = playerService.createPlayer(params);
        return ResponseEntity.status(HttpStatus.CREATED).body(player);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerDto> getPlayerById(@PathVariable int id) {
        PlayerDto player = playerService.getPlayerById(id);
        if (player == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(player);
    }
}

