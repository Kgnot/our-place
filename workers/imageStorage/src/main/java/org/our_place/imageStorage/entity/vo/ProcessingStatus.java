package org.our_place.imageStorage.entity.vo;

import java.util.Objects;

public record ProcessingStatus(String code) {
    public static final org.our_place.gallery.domain.vo.ProcessingStatus PENDING = new org.our_place.gallery.domain.vo.ProcessingStatus("pending");
    public static final org.our_place.gallery.domain.vo.ProcessingStatus PROCESSING = new org.our_place.gallery.domain.vo.ProcessingStatus("processing");
    public static final org.our_place.gallery.domain.vo.ProcessingStatus COMPLETED = new org.our_place.gallery.domain.vo.ProcessingStatus("completed");
    public static final org.our_place.gallery.domain.vo.ProcessingStatus FAILED = new org.our_place.gallery.domain.vo.ProcessingStatus("failed");

    public ProcessingStatus {
        Objects.requireNonNull(code, "code no puede ser null");
    }

    public boolean isCompleted() {
        return COMPLETED.equals(this);
    }

    public boolean isFailed() {
        return FAILED.equals(this);
    }
}