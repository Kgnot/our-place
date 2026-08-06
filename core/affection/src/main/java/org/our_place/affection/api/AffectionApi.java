package org.our_place.affection.api;

import org.our_place.affection.service.dto.LoveNoteDto;
import org.our_place.affection.usecase.command.CreateLoveNoteCommand;
import org.our_place.affection.usecase.output.CreateLoveNoteOutput;
import org.our_place.common.shared.SharedApi;

import java.util.List;
import java.util.UUID;

@SharedApi(description = "API interna del módulo affection, consumible in-process por otros módulos")
public interface AffectionApi {
    CreateLoveNoteOutput createLoveNote(CreateLoveNoteCommand command);
    List<LoveNoteDto> listLoveNotes(UUID roomId);
}