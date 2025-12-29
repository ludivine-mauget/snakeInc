package org.snakeinc.api.service;

import org.snakeinc.api.model.Category;
import org.snakeinc.api.model.Player;
import org.snakeinc.api.model.PlayerParams;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PlayerService {
    private final Map<Integer, Player> players = new HashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    public Player createPlayer(PlayerParams params) {
        Integer id = idGenerator.getAndIncrement();
        Category category = determineCategory(params.age());
        LocalDate createdAt = LocalDate.now();

        Player player = new Player(id, params.name(), params.age(), category, createdAt);
        players.put(id, player);

        return player;
    }

    private Category determineCategory(int age) {
        return age < 50 ? Category.JUNIOR : Category.SENIOR;
    }
}

