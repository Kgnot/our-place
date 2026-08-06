package org.our_place.calendar.domain.exception;

import org.our_place.shared.infra.http.ResultException;
import org.our_place.shared.utils.ResultIssue;

import java.util.UUID;

public class DayEntryNotFoundException extends ResultException {
    public DayEntryNotFoundException(UUID roomId, String entryDate) {
        super(
                "Entrada de diario no encontrada",
                new ResultIssue("DAY_ENTRY_NOT_FOUND", "No encontramos una entrada para esa fecha", ResultIssue.Severity.WARNING)
        );
    }
}