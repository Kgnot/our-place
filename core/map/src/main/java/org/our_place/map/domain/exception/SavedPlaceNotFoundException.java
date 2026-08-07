package org.our_place.map.domain.exception;


import org.our_place.shared.infra.http.ResultException;
import org.our_place.shared.utils.ResultIssue;

import java.util.UUID;

public class SavedPlaceNotFoundException extends ResultException {
    public SavedPlaceNotFoundException(UUID savedPlaceId, UUID roomId) {
        super(
                "saved_place not found. id=" + savedPlaceId + " roomId=" + roomId,
                new ResultIssue(
                        "SAVED_PLACE_NOT_FOUND",
                        "No encontramos ese lugar guardado.",
                        ResultIssue.Severity.WARNING
                )
        );
    }
}