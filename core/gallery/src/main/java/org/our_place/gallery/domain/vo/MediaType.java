package org.our_place.gallery.domain.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

public record MediaType(String code) {
    public static final MediaType IMAGE = new MediaType("image");
    public static final MediaType VIDEO = new MediaType("video");

    public MediaType {
        Objects.requireNonNull(code, "code no puede ser null");
    }

    @JsonCreator
    public static MediaType fromValue(String code) {
        return new MediaType(code);
    }

    @JsonValue
    public String toJson() {
        return code;
    }

    public boolean isVideo() {
        return VIDEO.equals(this);
    }
}