package org.our_place.gallery.domain.vo;

import java.util.Objects;

public record MediaType(String code) {
    public static final MediaType IMAGE = new MediaType("image");
    public static final MediaType VIDEO = new MediaType("video");

    public MediaType {
        Objects.requireNonNull(code, "code no puede ser null");
    }

    public boolean isVideo() {
        return VIDEO.equals(this);
    }
}