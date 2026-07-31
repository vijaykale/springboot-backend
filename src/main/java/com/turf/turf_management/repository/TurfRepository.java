package com.turf.turf_management.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.turf.turf_management.model.Turf;

import java.util.Optional;

public interface TurfRepository extends MongoRepository<Turf, String> {
    boolean existsByContactNumber(String contactNumber);
    Optional<Turf> findByTurfId(String turfId);

}
