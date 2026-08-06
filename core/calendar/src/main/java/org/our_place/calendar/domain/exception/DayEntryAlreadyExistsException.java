package org.our_place.calendar.domain.exception;

import org.our_place.shared.infra.http.ResultException;
import org.our_place.shared.utils.ResultIssue;

public class DayEntryAlreadyExistsException extends ResultException {
    public DayEntryAlreadyExistsException() {
        super(
                "Ya existe una entrada de diario para esta sala en esta fecha",
                new ResultIssue("DAY_ENTRY_ALREADY_EXISTS", "Ya escribiste algo para este día — edítalo en vez de crear otro", ResultIssue.Severity.WARNING)
        );
    }
}