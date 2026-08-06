package org.our_place.room.domain.vo;

import java.util.Objects;

/** VO sobre role_code de room_member / room_invitation. */
public record RoomRole(String code) {

    public static final RoomRole OWNER = new RoomRole("owner");
    public static final RoomRole MEMBER = new RoomRole("member");

    public RoomRole {
        Objects.requireNonNull(code, "code no puede ser null");
    }

    public boolean isOwner() {
        return OWNER.equals(this);
    }
}