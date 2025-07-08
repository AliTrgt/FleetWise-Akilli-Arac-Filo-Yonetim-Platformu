package com.example.TripService.repository;

import com.example.TripService.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ITripRepository extends JpaRepository<Trip, UUID> {
}
