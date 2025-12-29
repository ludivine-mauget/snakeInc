package org.snakeinc.api.service;

import org.snakeinc.api.model.Category;
import org.snakeinc.api.model.PlayerDto;
import org.snakeinc.api.model.PlayerParams;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PlayerService {
    private Map<Integer, PlayerDto> players = new HashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    public PlayerDto createPlayer(PlayerParams params) {
        Integer id = idGenerator.getAndIncrement();
        Category category = determineCategory(params.age());
        LocalDate createdAt = LocalDate.now();

        PlayerDto player = new PlayerDto(id, params.name(), params.age(), category, createdAt);
        players.put(id, player);

        return player;
    }

    private Category determineCategory(int age) {
        return age < 50 ? Category.JUNIOR : Category.SENIOR;
    }

    public PlayerDto getPlayerById(Integer id) {
        return players.get(id);
    }
}

