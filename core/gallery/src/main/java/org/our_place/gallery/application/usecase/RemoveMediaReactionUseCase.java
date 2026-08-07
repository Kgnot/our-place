package org.our_place.gallery.application.usecase;

import lombok.RequiredArgsConstructor;
import org.our_place.gallery.application.usecase.command.RemoveMediaReactionCommand;
import org.our_place.gallery.application.usecase.output.RemoveMediaReactionOutput;
import org.our_place.gallery.infra.persistence.repository.MediaReactionRepository;
import org.our_place.shared.application.bus.EventBus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class RemoveMediaReactionUseCase implements UseCase<RemoveMediaReactionCommand, RemoveMediaReactionOutput> {

    private final MediaReactionRepository mediaReactionRepository;
    private final EventBus eventPublisher;

    @Override
    public RemoveMediaReactionOutput execute(RemoveMediaReactionCommand command) {
        mediaReactionRepository.deleteByIdMediaIdAndIdUserLoginId(command.mediaId(), command.userLoginId());
        // TODO, crear evento,, igual depende
        return new RemoveMediaReactionOutput(true);
    }
}