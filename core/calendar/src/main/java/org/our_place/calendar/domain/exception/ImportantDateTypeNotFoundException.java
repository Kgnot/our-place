package org.our_place.calendar.domain.exception;

import org.our_place.shared.infra.http.ResultException;
import org.our_place.shared.utils.ResultIssue;

public class ImportantDateTypeNotFoundException extends ResultException {
    public ImportantDateTypeNotFoundException(String code) {
        super(
                "Tipo de fecha importante no encontrado: " + code,
                new ResultIssue("IMPORTANT_DATE_TYPE_NOT_FOUND", "El tipo de fecha indicado no existe", ResultIssue.Severity.WARNING)
        );
    }
}