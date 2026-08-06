package org.our_place.affection.api;

import lombok.RequiredArgsConstructor;
import org.our_place.affection.service.LoveNoteQueryService;
import org.our_place.affection.service.dto.LoveNoteDto;
import org.our_place.affection.usecase.CreateLoveNoteUseCase;
import org.our_place.affection.usecase.command.CreateLoveNoteCommand;
import org.our_place.affection.usecase.output.CreateLoveNoteOutput;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AffectionApiImpl implements AffectionApi {

    private final CreateLoveNoteUseCase createLoveNoteUseCase;
    private final LoveNoteQueryService loveNoteQueryService;

    @Override
    public CreateLoveNoteOutput createLoveNote(CreateLoveNoteCommand command) {
        return createLoveNoteUseCase.execute(command);
    }

    @Override
    public List<LoveNoteDto> listLoveNotes(UUID roomId) {
        return loveNoteQueryService.listByRoom(roomId);
    }
}