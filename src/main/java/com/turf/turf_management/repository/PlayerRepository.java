package com.turf.turf_management.repository;

import com.turf.turf_management.enums.Role;
import com.turf.turf_management.model.Player;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends MongoRepository<Player, String> {
    Optional<Player> findByPlayerId(String playerId);

    Optional<Player> findByEmail(String email);

    long countByRole(Role role);

    boolean existsByEmail(String email);

    List<Player> findByRoleAndActiveTrue(Role role);

    boolean existsByPhone(String phoneNumber);

    List<Player> findByActiveTrue();
}
