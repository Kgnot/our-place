package org.our_place.gallery.application.usecase;

import lombok.RequiredArgsConstructor;
import org.our_place.gallery.application.usecase.command.DeleteMediaCommentCommand;
import org.our_place.gallery.application.usecase.output.DeleteMediaCommentOutput;
import org.our_place.gallery.domain.entity.MediaComment;
import org.our_place.gallery.domain.exception.MediaCommentAccessForbiddenException;
import org.our_place.gallery.domain.exception.MediaCommentNotFoundException;
import org.our_place.gallery.infra.persistence.repository.MediaCommentRepository;
import org.our_place.shared.application.bus.EventBus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Solo el autor del comentario puede borrarlo (no el owner de la room — a diferencia de DeleteMediaUseCase).
 */
@Component
@RequiredArgsConstructor
@Transactional
public class DeleteMediaCommentUseCase implements UseCase<DeleteMediaCommentCommand, DeleteMediaCommentOutput> {

    private final MediaCommentRepository mediaCommentRepository;
    private final EventBus eventBus;

    @Override
    public DeleteMediaCommentOutput execute(DeleteMediaCommentCommand command) {
        MediaComment comment = mediaCommentRepository.findByIdAndDeletedAtIsNull(command.commentId())
                .orElseThrow(() -> new MediaCommentNotFoundException(command.commentId()));

        if (!comment.getUserLoginId().equals(command.requestingUserId())) {
            throw new MediaCommentAccessForbiddenException(command.commentId(), command.requestingUserId());
        }

        comment.softDelete();
        //posible evento
        return new DeleteMediaCommentOutput(true);
    }
}