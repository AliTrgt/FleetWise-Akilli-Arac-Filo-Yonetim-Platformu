package com.example.DriverService.repository;

import com.example.DriverService.entity.DriverPenalty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DriverPenaltyRepository extends JpaRepository<DriverPenalty, UUID> {
}
