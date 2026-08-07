package org.our_place.gallery.domain.exception;

import org.our_place.shared.infra.http.ResultException;
import org.our_place.shared.utils.ResultIssue;

import java.util.UUID;

public class MediaCommentNotFoundException extends ResultException {
    public MediaCommentNotFoundException(UUID commentId) {
        super(
                "media comment not found, id=" + commentId,
                new ResultIssue("MEDIA_COMMENT_NOT_FOUND", "No encontramos ese comentario.", ResultIssue.Severity.WARNING)
        );
    }
}