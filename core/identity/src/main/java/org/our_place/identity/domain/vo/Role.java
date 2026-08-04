package org.our_place.identity.domain.vo;

public record Role(String code) {
    public static final Role ROOM_OWNER = new Role("room_owner");
    public static final Role ROOM_MEMBER = new Role("room_member");
    public static final Role ROOM_GUEST = new Role("room_guest");

    public Role {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("El código de rol no puede estar vacío");
        }
    }
}
