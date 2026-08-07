package org.our_place.gallery.application.usecase;

import lombok.RequiredArgsConstructor;
import org.our_place.gallery.api.events.MediaDeletedEvent;
import org.our_place.gallery.api.external.RoomExternalApi;
import org.our_place.gallery.application.usecase.command.DeleteMediaCommand;
import org.our_place.gallery.application.usecase.output.DeleteMediaOutput;
import org.our_place.gallery.domain.entity.Media;
import org.our_place.gallery.domain.exception.MediaAccessForbiddenException;
import org.our_place.gallery.domain.exception.MediaNotFoundException;
import org.our_place.gallery.infra.persistence.repository.MediaRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Borrado lógico. Puede borrar quien subió la foto o el owner de la room (moderación).
 */
@Component
@RequiredArgsConstructor
@Transactional
public class DeleteMediaUseCase implements UseCase<DeleteMediaCommand, DeleteMediaOutput> {

    private final MediaRepository mediaRepository;
    private final RoomExternalApi roomApi;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public DeleteMediaOutput execute(DeleteMediaCommand command) {
        Media media = mediaRepository.findByIdAndDeletedAtIsNull(command.mediaId())
                .orElseThrow(() -> new MediaNotFoundException(command.mediaId()));

        boolean isUploader = media.getUploadedByUserId().equals(command.requestingUserId());
        boolean isRoomOwner = roomApi.getRoomOwnerId(media.getRoomId()).equals(command.requestingUserId());

        if (!isUploader && !isRoomOwner) {
            throw new MediaAccessForbiddenException(command.mediaId(), command.requestingUserId());
        }

        media.softDelete();

        eventPublisher.publishEvent(new MediaDeletedEvent(media.getId(), media.getRoomId()));

        return new DeleteMediaOutput(true);
    }
}