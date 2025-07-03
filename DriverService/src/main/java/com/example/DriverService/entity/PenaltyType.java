package com.example.DriverService.entity;

public enum PenaltyType {
    SPEEDING(3),
    HARSH_BRAKING(5),
    LANE_VIOLATION(4),
    RED_LIGHT_VIOLATION(7),
    YELLOW_LIGHT_VIOLATION(3),
    PHONE_USAGE(10),
    SEAT_BELT(7);

    private final int penaltyPoint;

    PenaltyType(int penaltyPoint) {
        this.penaltyPoint = penaltyPoint;
    }

    public int getPenaltyPoints() {
        return penaltyPoint;
    }
}
