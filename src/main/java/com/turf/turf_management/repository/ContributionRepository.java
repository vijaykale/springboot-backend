package com.turf.turf_management.repository;

import com.turf.turf_management.model.Contribution;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContributionRepository extends MongoRepository<Contribution, String> {
    Optional<Contribution> findByContributionId(String contributionId);

    List<Contribution> findByBookingIdAndActiveTrue(String bookingId);

    List<Contribution> findByPlayerIdAndActiveTrue(String playerId);

    boolean existsByBookingIdAndActiveTrue(String bookingId);


}
