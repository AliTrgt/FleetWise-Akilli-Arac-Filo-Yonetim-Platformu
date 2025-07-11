package com.example.MaintenanceService.entity;

public enum MaintenanceType {
    OIL_CHANGE(1),
    BRAKE_REPLACEMENT(2),
    TIRE_ROTATION(1),
    ENGINE_REPAIR(7),
    BATTERY_REPLACEMENT(2),
    INSPECTION(1),
    AIR_FILTER_REPLACEMENT(1),
    TRANSMISSION_REPAIR(5),
    COOLANT_FLUSH(2),
    SPARK_PLUG_REPLACEMENT(2),
    TIMING_BELT_REPLACEMENT(4),
    AC_REPAIR(3),
    SUSPENSION_CHECK(2),
    ALIGNMENT(1),
    FUEL_SYSTEM_CLEANING(2),
    CLUTCH_REPLACEMENT(4),
    EXHAUST_REPAIR(2),
    RADIATOR_REPLACEMENT(3),
    HEADLIGHT_REPLACEMENT(1),
    WINDOW_REPAIR(1),
    SOFTWARE_UPDATE(1),
    SENSOR_CALIBRATION(1),
    WHEEL_BALANCING(1),
    DIFFERENTIAL_SERVICE(3),
    POWER_STEERING_REPAIR(2),
    BRAKE_FLUID_FLUSH(1),
    HVAC_SERVICE(2),
    EGR_CLEANING(2),
    OTHER(3);

    private final int estimatedDays;


    MaintenanceType(int estimatedDays) {
        this.estimatedDays = estimatedDays;
    }

    public int getEstimatedDays(){
            return estimatedDays;
    }
}
