// RoomStatus.java
package org.our_place.room.domain.vo;

import java.util.Objects;


public record RoomStatus(String code) {

    public static final RoomStatus TRIAL = new RoomStatus("trial");
    public static final RoomStatus ACTIVE = new RoomStatus("active");
    public static final RoomStatus SUSPENDED = new RoomStatus("suspended");

    public RoomStatus {
        Objects.requireNonNull(code, "code no puede ser null");
    }

    public boolean allowsInvitations() {
        return ACTIVE.equals(this) || TRIAL.equals(this);
    }

    public boolean isSuspended() {
        return SUSPENDED.equals(this);
    }
}