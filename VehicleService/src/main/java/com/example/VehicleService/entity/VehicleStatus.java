package com.example.VehicleService.entity;

public enum VehicleStatus {
    ACTIVE,   // çalışır durumda bekliyor
    UNDER_MAINTENANCE,
    OUT_OF_SERVICE,   //hurdaklık
    ASSIGNED,
    UNASSIGNED,
    IN_TRIP   //yolda zaten
}
