package org.our_place.gallery.application.usecase;

import lombok.RequiredArgsConstructor;
import org.our_place.gallery.application.usecase.command.ReactToMediaCommand;
import org.our_place.gallery.application.usecase.output.ReactToMediaOutput;
import org.our_place.gallery.domain.entity.Media;
import org.our_place.gallery.domain.entity.MediaReaction;
import org.our_place.gallery.domain.exception.MediaNotFoundException;
import org.our_place.gallery.infra.persistence.repository.MediaReactionRepository;
import org.our_place.gallery.infra.persistence.repository.MediaRepository;
import org.our_place.shared.application.bus.EventBus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** Upsert: una reacción por usuario por foto (PK compuesta), así que crea o actualiza el tipo. */
@Component
@RequiredArgsConstructor
@Transactional
public class ReactToMediaUseCase implements UseCase<ReactToMediaCommand, ReactToMediaOutput> {

    private final MediaRepository mediaRepository;
    private final MediaReactionRepository mediaReactionRepository;
    private final EventBus eventBus;

    @Override
    public ReactToMediaOutput execute(ReactToMediaCommand command) {
        Media media = mediaRepository.findByIdAndDeletedAtIsNull(command.mediaId())
                .orElseThrow(() -> new MediaNotFoundException(command.mediaId()));

        MediaReaction reaction = mediaReactionRepository
                .findByIdMediaIdAndIdUserLoginId(command.mediaId(), command.userLoginId())
                .orElse(null);

        if (reaction == null) {
            reaction = MediaReaction.create(media, command.userLoginId(), command.reactionType());
        } else {
            reaction.updateReactionType(command.reactionType());
        }
        mediaReactionRepository.save(reaction);
        // todo evento para que otros le lleguen la notificación

        return new ReactToMediaOutput(reaction.getReactionType());
    }
}