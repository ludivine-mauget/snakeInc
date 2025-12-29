package org.snakeinc.api.service;

import org.snakeinc.api.entity.Player;
import org.snakeinc.api.model.Category;
import org.snakeinc.api.model.PlayerDto;
import org.snakeinc.api.model.PlayerListDto;
import org.snakeinc.api.model.PlayerParams;
import org.snakeinc.api.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public PlayerDto createPlayer(PlayerParams params) {
        Category category = determineCategory(params.age());
        LocalDate createdAt = LocalDate.now();

        Player player = new Player(null, params.name(), params.age(), category, createdAt, null);
        Player savedPlayer = playerRepository.save(player);

        return savedPlayer.toDto();
    }

    private Category determineCategory(int age) {
        return age < 50 ? Category.JUNIOR : Category.SENIOR;
    }

    public PlayerDto getPlayerById(Integer id) {
        return playerRepository.findById(id)
                .map(Player::toDto)
                .orElse(null);
    }

    public PlayerListDto getAllPlayers() {
        List<PlayerDto> players = StreamSupport.stream(playerRepository.findAll().spliterator(), false)
                .map(Player::toDto)
                .collect(Collectors.toList());
        return new PlayerListDto(players);
    }
}


