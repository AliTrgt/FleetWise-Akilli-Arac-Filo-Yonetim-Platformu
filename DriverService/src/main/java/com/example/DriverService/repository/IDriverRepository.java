package com.example.DriverService.repository;

import com.example.DriverService.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IDriverRepository extends JpaRepository<Driver, UUID> {
    Optional<Driver> findByIdAndDeletedFalse(UUID id);
    Optional<Driver> findByPersonIdAndDeletedFalse(int personId);
}
