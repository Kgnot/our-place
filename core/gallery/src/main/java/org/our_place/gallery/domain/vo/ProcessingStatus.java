package org.our_place.gallery.domain.vo;

import java.util.Objects;

public record ProcessingStatus(String code) {
    public static final ProcessingStatus PENDING = new ProcessingStatus("pending");
    public static final ProcessingStatus PROCESSING = new ProcessingStatus("processing");
    public static final ProcessingStatus COMPLETED = new ProcessingStatus("completed");
    public static final ProcessingStatus FAILED = new ProcessingStatus("failed");

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