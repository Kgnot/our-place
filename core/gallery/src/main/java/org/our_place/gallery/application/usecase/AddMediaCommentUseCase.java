package org.our_place.gallery.application.usecase;

import lombok.RequiredArgsConstructor;
import org.our_place.gallery.api.events.MediaCommentAddedEvent;
import org.our_place.gallery.application.usecase.command.AddMediaCommentCommand;
import org.our_place.gallery.application.usecase.output.AddMediaCommentOutput;
import org.our_place.gallery.domain.entity.Media;
import org.our_place.gallery.domain.entity.MediaComment;
import org.our_place.gallery.domain.exception.MediaNotFoundException;
import org.our_place.gallery.infra.persistence.repository.MediaCommentRepository;
import org.our_place.gallery.infra.persistence.repository.MediaRepository;
import org.our_place.shared.application.bus.EventBus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class AddMediaCommentUseCase implements UseCase<AddMediaCommentCommand, AddMediaCommentOutput> {

    private final MediaRepository mediaRepository;
    private final MediaCommentRepository mediaCommentRepository;
    private final EventBus eventPublisher;

    @Override
    public AddMediaCommentOutput execute(AddMediaCommentCommand command) {
        Media media = mediaRepository.findByIdAndDeletedAtIsNull(command.mediaId())
                .orElseThrow(() -> new MediaNotFoundException(command.mediaId()));

        MediaComment comment = MediaComment.create(media.getId(), command.userLoginId(), command.content());
        mediaCommentRepository.save(comment);

        eventPublisher.publish(new MediaCommentAddedEvent(
                media.getId(), media.getRoomId(), comment.getContent(), command.userLoginId()
        ));

        return new AddMediaCommentOutput(comment.getId());
    }
}