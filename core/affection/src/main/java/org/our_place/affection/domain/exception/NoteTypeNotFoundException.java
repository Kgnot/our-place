package org.our_place.affection.domain.exception;


import org.our_place.shared.infra.http.ResultException;
import org.our_place.shared.utils.ResultIssue;

public class NoteTypeNotFoundException extends ResultException {
    public NoteTypeNotFoundException(String code) {
        super(
                "Tipo de nota no encontrado: " + code,
                new ResultIssue("NOTE_TYPE_NOT_FOUND",
                        "El tipo de nota indicado no existe",
                        ResultIssue.Severity.WARNING)
        );
    }
}