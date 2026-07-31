package com.turf.turf_management.service.impl;

import com.turf.turf_management.dto.player.PlayerRequestDTO;
import com.turf.turf_management.dto.player.PlayerResponseDTO;
import com.turf.turf_management.dto.player.PlayerUpdateRequestDTO;
import com.turf.turf_management.enums.SequenceConstants;
import com.turf.turf_management.exception.DuplicateResourceException;
import com.turf.turf_management.exception.ResourceNotFoundException;
import com.turf.turf_management.mapper.PlayerMapper;
import com.turf.turf_management.model.Player;
import com.turf.turf_management.enums.Role;
import com.turf.turf_management.enums.Status;
import com.turf.turf_management.repository.PlayerRepository;
import com.turf.turf_management.service.NotificationService;
import com.turf.turf_management.service.PlayerService;
import com.turf.turf_management.service.SequenceGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlayerServiceImpl implements PlayerService {

    private final PlayerMapper playerMapper;
    private final SequenceGeneratorService sequenceGeneratorService;
    private final PlayerRepository playerRepository;
    private final NotificationService notificationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public PlayerResponseDTO createPlayer(PlayerRequestDTO request) {
        log.info("Creating player with email: {}", request.getEmail());



        if (playerRepository.existsByEmail(request.getEmail())) {
            log.error("Player with email {} already exists", request.getEmail());
            throw new DuplicateResourceException("Player with email " + request.getEmail() + " already exists");
        }
        if (playerRepository.existsByPhone(request.getPhone())) {
            log.error("Player with phone number {} already exists", request.getPhone());
            throw new DuplicateResourceException("Player with phone number " + request.getPhone() + " already exists");
        }

        Player player = playerMapper.toEntity(request);
        String playerId = "PL" +
                String.format("%03d",
                        sequenceGeneratorService.generateSequence(SequenceConstants.PLAYER_SEQUENCE));
        player.setPlayerId(playerId);
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            player.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        player.setRole(Role.PLAYER);
        player.setStatus(Status.ACTIVE);
        player.setJoinedDate(request.getJoinedDate());
        player.setDob(request.getDob());

        Player savedPlayer = playerRepository.save(player);
        log.info("Player created successfully with ID: {}", savedPlayer.getPlayerId());

        notificationService.playerCreated(
                player.getEmail(),
                player.getName()
        );

        return playerMapper.playerResponseDTO(savedPlayer);

    }

    @Override
    public PlayerResponseDTO getPlayerById(String playerId) {
        Player player = playerRepository.findByPlayerId(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("Player with ID " + playerId + " not found"));
        log.info("Player found with ID: {}", playerId);
        return playerMapper.playerResponseDTO(player);
    }

    @Override
    public List<PlayerResponseDTO> getAllPlayers() {
        return playerRepository.findByRoleAndActiveTrue(Role.PLAYER)
                .stream()
                .map(playerMapper::playerResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PlayerResponseDTO updatePlayer(String playerId, PlayerUpdateRequestDTO request) {

        Player player = playerRepository.findByPlayerId(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found"));

        player.setName(request.getName());
        player.setPhone(request.getPhone());
        player.setEmail(request.getEmail());

        if (request.getDob() != null) {
            player.setDob(request.getDob());
        }

        if (request.getJoinedDate() != null) {
            player.setJoinedDate(request.getJoinedDate());
        }

        player.setStatus(Status.ACTIVE);
        player.setActive(true);

        return playerMapper.playerResponseDTO(playerRepository.save(player));
    }

    @Override
    public void updatePlayerStatus(String playerId, boolean active) {

        Player player = playerRepository.findByPlayerId(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found"));

        // 🔥 SET BOTH FIELDS
        player.setActive(active);
        player.setStatus(active ? Status.ACTIVE : Status.INACTIVE);

        playerRepository.save(player);
    }

    @Override
    public void deletePlayer(String playerId) {
        Player player = playerRepository.findByPlayerId(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("Player with ID " + playerId + " not found"));
        player.setActive(false);
        player.setStatus(Status.INACTIVE);
        playerRepository.save(player);
    }

    public List<Player> getBirthdayPlayersOfMonth() {
        List<Player> players = playerRepository.findByActiveTrue();
        int currentMonth = LocalDate.now().getMonthValue();
        return players.stream()
                .filter(p -> p.getDob() != null &&
                        p.getDob().getMonthValue() == currentMonth)
                .sorted(((p1,p2)->p1.getDob().getDayOfMonth() - p2.getDob().getDayOfMonth()))
                .toList();
    }

    public String sendBirthdayWishes(String playerId) {
        Player player = playerRepository.findByPlayerId(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("Player with ID " + playerId + " not found"));
        notificationService.sendBirthdayWish(player.getEmail(), player.getName());

        return "Birthday wishes sent to " + player.getName();
    }

}
