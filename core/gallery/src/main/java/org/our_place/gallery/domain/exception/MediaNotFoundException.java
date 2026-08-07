package org.our_place.gallery.domain.exception;

import org.our_place.shared.infra.http.ResultException;
import org.our_place.shared.utils.ResultIssue;

import java.util.UUID;

public class MediaNotFoundException extends ResultException {
    public MediaNotFoundException(UUID mediaId) {
        super(
                "media not found, id=" + mediaId,
                new ResultIssue("MEDIA_NOT_FOUND", "No encontramos esa foto o video.", ResultIssue.Severity.WARNING)
        );
    }
}