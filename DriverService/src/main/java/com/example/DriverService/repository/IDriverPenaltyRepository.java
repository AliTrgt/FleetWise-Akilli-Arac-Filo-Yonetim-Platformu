package com.example.DriverService.repository;

import com.example.DriverService.entity.DriverPenalty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IDriverPenaltyRepository extends JpaRepository<DriverPenalty, UUID> {
    List<DriverPenalty> findAllByDriver_IdAndDeletedFalse(UUID id);
    Optional<DriverPenalty> findByIdAndDeletedFalse(UUID id);
}
