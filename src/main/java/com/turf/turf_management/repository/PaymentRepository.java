package com.turf.turf_management.repository;

import com.turf.turf_management.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {
    List<Payment> findByContributionId(String contributionId);

    List<Payment> findByBookingId(String bookingId);

    List<Payment> findByPlayerId(String playerId);

    List<Payment> findAllByOrderByPaymentDateDesc();

}
