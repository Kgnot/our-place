package org.our_place.affection.usecase;

import lombok.RequiredArgsConstructor;
import org.our_place.affection.api.events.LoveNoteCreatedEvent;
import org.our_place.affection.domain.exception.NoteTypeNotFoundException;
import org.our_place.affection.persistence.entity.LkpNoteType;
import org.our_place.affection.persistence.entity.LoveNote;
import org.our_place.affection.persistence.repository.LkpNoteTypeRepository;
import org.our_place.affection.persistence.repository.LoveNoteRepository;
import org.our_place.affection.usecase.command.CreateLoveNoteCommand;
import org.our_place.affection.usecase.output.CreateLoveNoteOutput;
import org.our_place.shared.application.bus.EventBus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateLoveNoteUseCase implements UseCase<CreateLoveNoteCommand, CreateLoveNoteOutput> {

    private final LoveNoteRepository loveNoteRepository;
    private final LkpNoteTypeRepository lkpNoteTypeRepository;
    private final EventBus eventBus;

    @Override
    @Transactional
    public CreateLoveNoteOutput execute(CreateLoveNoteCommand command) {
        LkpNoteType type = lkpNoteTypeRepository.findById(command.typeCode())
                .orElseThrow(() -> new NoteTypeNotFoundException(command.typeCode()));

        LoveNote note = LoveNote.create(command.roomId(), command.authorUserId(), type, command.content());
        loveNoteRepository.save(note);

        eventBus.publish(new LoveNoteCreatedEvent(
                note.getId(),
                note.getRoomId(),
                note.getAuthorUserId(),
                type.getCode()
        ));

        return new CreateLoveNoteOutput(note.getId(), type.getCode(), note.getCreatedAt());
    }
}