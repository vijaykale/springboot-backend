package com.turf.turf_management.service;

import com.turf.turf_management.dto.player.PlayerRequestDTO;
import com.turf.turf_management.dto.player.PlayerResponseDTO;
import com.turf.turf_management.dto.player.PlayerUpdateRequestDTO;
import com.turf.turf_management.model.Player;

import java.util.List;

public interface PlayerService {
    PlayerResponseDTO createPlayer(PlayerRequestDTO request);

    PlayerResponseDTO getPlayerById(String playerId);

    List<PlayerResponseDTO> getAllPlayers();

    PlayerResponseDTO updatePlayer(String playerId, PlayerUpdateRequestDTO request);

    void deletePlayer(String playerId);

    void updatePlayerStatus(String playerId, boolean active);

    List<Player> getBirthdayPlayersOfMonth();

    String sendBirthdayWishes(String playerId);
}
