package org.our_place.identity.domain.vo;

public record UserStatus(String code) {
    public static final UserStatus ACTIVE = new UserStatus("active");
    public static final UserStatus PENDING_VERIFICATION = new UserStatus("pending_verification");
    public static final UserStatus DISABLED = new UserStatus("disabled");
    public static final UserStatus LOCKED = new UserStatus("locked");

    public boolean allowsLogin() {
        return ACTIVE.equals(this);
    }
}