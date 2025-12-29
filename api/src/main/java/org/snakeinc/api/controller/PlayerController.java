package org.snakeinc.api.controller;

import org.snakeinc.api.model.Player;
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
    public ResponseEntity<Player> createPlayer(@RequestBody PlayerParams params) {
        Player player = playerService.createPlayer(params);
        return ResponseEntity.status(HttpStatus.CREATED).body(player);
    }
}

