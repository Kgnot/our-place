package org.our_place.room.domain.vo;

import java.util.Objects;

/** VO sobre status de room_invitation. */
public record InvitationStatus(String code) {

    public static final InvitationStatus PENDING = new InvitationStatus("pending");
    public static final InvitationStatus ACCEPTED = new InvitationStatus("accepted");

    public InvitationStatus {
        Objects.requireNonNull(code, "code no puede ser null");
    }

    public boolean isPending() {
        return PENDING.equals(this);
    }
}