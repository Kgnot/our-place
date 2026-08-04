package org.our_place.identity.domain.vo;

public record ContactType(String code) {
    public static final ContactType EMAIL = new ContactType("email");
    public static final ContactType PHONE = new ContactType("phone");
    public static final ContactType WHATSAPP = new ContactType("whatsapp");
}