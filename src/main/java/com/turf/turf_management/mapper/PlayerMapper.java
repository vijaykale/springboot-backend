package com.turf.turf_management.mapper;

import com.turf.turf_management.dto.player.PlayerRequestDTO;
import com.turf.turf_management.dto.player.PlayerResponseDTO;
import com.turf.turf_management.model.Player;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PlayerMapper {
    public Player toEntity(PlayerRequestDTO dto){
        Player player = new Player();
        player.setName(dto.getName());
        player.setPhone(dto.getPhone());
        player.setEmail(dto.getEmail());
        player.setDob(dto.getDob());
        player.setStatus(dto.getStatus());
        player.setRole(dto.getRole());
        return player;
    }


    public PlayerResponseDTO playerResponseDTO(Player player) {
        if (player == null) {
            return null;
        }
        PlayerResponseDTO dto = new PlayerResponseDTO();
        dto.setPlayerId(player.getPlayerId());
        dto.setName(player.getName());
        dto.setEmail(player.getEmail());
        dto.setPhone(player.getPhone());
        dto.setDob(player.getDob());
        dto.setStatus(player.getStatus());
        dto.setRole(player.getRole());
        dto.setJoinedDate(player.getJoinedDate());

        return dto;
    }
}
