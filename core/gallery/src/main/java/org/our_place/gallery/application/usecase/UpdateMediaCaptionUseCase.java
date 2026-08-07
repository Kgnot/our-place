package org.our_place.gallery.application.usecase;

import lombok.RequiredArgsConstructor;
import org.our_place.gallery.application.usecase.command.UpdateMediaCaptionCommand;
import org.our_place.gallery.application.usecase.output.UpdateMediaCaptionOutput;
import org.our_place.gallery.domain.entity.Media;
import org.our_place.gallery.domain.exception.MediaAccessForbiddenException;
import org.our_place.gallery.domain.exception.MediaNotFoundException;
import org.our_place.gallery.infra.persistence.repository.MediaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Solo quien subió la foto puede editar su caption.
 */
@Component
@RequiredArgsConstructor
@Transactional
public class UpdateMediaCaptionUseCase implements UseCase<UpdateMediaCaptionCommand, UpdateMediaCaptionOutput> {

    private final MediaRepository mediaRepository;

    @Override
    public UpdateMediaCaptionOutput execute(UpdateMediaCaptionCommand command) {
        Media media = mediaRepository.findByIdAndDeletedAtIsNull(command.mediaId())
                .orElseThrow(() -> new MediaNotFoundException(command.mediaId()));

        if (!media.getUploadedByUserId().equals(command.requestingUserId())) {
            throw new MediaAccessForbiddenException(command.mediaId(), command.requestingUserId());
        }

        media.updateCaption(command.caption());

        return new UpdateMediaCaptionOutput(media.getCaption());
    }
}