package com.turf.turf_management.util;

import com.turf.turf_management.dto.player.PlayerRequestDTO;
import com.turf.turf_management.dto.player.PlayerResponseDTO;
import com.turf.turf_management.model.Player;

public class PlayerMapperUtil {

    public static Player toPlayer(PlayerRequestDTO playerRequestDTO) {
        if (playerRequestDTO == null) {
            return null;
        }
        Player player = new Player();
        player.setName(playerRequestDTO.getName());
        player.setPhone(playerRequestDTO.getPhone());
        player.setEmail(playerRequestDTO.getEmail());
        return player;
    }

    public static PlayerResponseDTO  toPlayerRequestDTO(Player player) {
        if (player == null) {
            return null;
        }
        PlayerResponseDTO playerResponseDTO = new PlayerResponseDTO();

        playerResponseDTO.setPlayerId(player.getPlayerId());
        playerResponseDTO.setName(player.getName());
        playerResponseDTO.setPhone(player.getPhone());
        playerResponseDTO.setEmail(player.getEmail());
        return playerResponseDTO;
    }
}
