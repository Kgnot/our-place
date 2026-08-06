package org.our_place.room.domain.vo;

import java.util.Objects;

/** VO sobre status de room_member. */
public record MembershipStatus(String code) {

    public static final MembershipStatus ACTIVE = new MembershipStatus("active");
    public static final MembershipStatus LEFT = new MembershipStatus("left");

    public MembershipStatus {
        Objects.requireNonNull(code, "code no puede ser null");
    }

    public boolean isActive() {
        return ACTIVE.equals(this);
    }
}