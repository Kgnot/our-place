package org.our_place.map.domain.vo;

public record BatteryLevel(short value) {

    public BatteryLevel {
        if (value < 0 || value > 100) {
            throw new IllegalArgumentException("battery level must be between 0 and 100, got " + value);
        }
    }

    public boolean isLow() {
        return value <= 15;
    }
}