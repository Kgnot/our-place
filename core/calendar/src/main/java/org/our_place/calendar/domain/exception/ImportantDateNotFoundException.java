package org.our_place.calendar.domain.exception;

import java.util.UUID;

public class ImportantDateNotFoundException extends RuntimeException {
    public ImportantDateNotFoundException(UUID id) {
        super("Important date not found: " + id);
    }
}