package org.our_place.gallery.domain.exception;

import org.our_place.shared.infra.http.ResultException;
import org.our_place.shared.utils.ResultIssue;

import java.util.UUID;

/** Solo el autor de un comentario puede borrarlo. */
public class MediaCommentAccessForbiddenException extends ResultException {
    public MediaCommentAccessForbiddenException(UUID commentId, UUID userId) {
        super(
                "user is not the comment author, commentId=" + commentId + ", userId=" + userId,
                new ResultIssue("MEDIA_COMMENT_ACCESS_FORBIDDEN", "Solo puedes borrar tus propios comentarios.", ResultIssue.Severity.WARNING)
        );
    }
}