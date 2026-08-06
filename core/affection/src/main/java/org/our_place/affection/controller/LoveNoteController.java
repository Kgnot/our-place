package org.our_place.affection.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.our_place.affection.controller.reponse.LoveNoteResponse;
import org.our_place.affection.controller.request.CreateLoveNoteRequest;
import org.our_place.affection.usecase.CreateLoveNoteUseCase;
import org.our_place.affection.usecase.command.CreateLoveNoteCommand;
import org.our_place.affection.usecase.output.CreateLoveNoteOutput;
import org.our_place.affection.service.LoveNoteQueryService;
import org.our_place.identity.api.SecurityContextApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rooms/{roomId}/love-notes")
@RequiredArgsConstructor
public class LoveNoteController {

    private final CreateLoveNoteUseCase createLoveNoteUseCase;
    private final LoveNoteQueryService loveNoteQueryService;
    private final SecurityContextApi securityContext;

    @PostMapping
    public ResponseEntity<CreateLoveNoteOutput> create(
            @PathVariable UUID roomId,
            @Valid @RequestBody CreateLoveNoteRequest request
    ) {
        UUID authorUserId = securityContext.getCurrentUserId();
        CreateLoveNoteOutput output = createLoveNoteUseCase.execute(
                new CreateLoveNoteCommand(roomId, authorUserId, request.typeCode(), request.content())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }

    @GetMapping
    public ResponseEntity<List<LoveNoteResponse>> list(@PathVariable UUID roomId) {
        List<LoveNoteResponse> notes = loveNoteQueryService.listByRoom(roomId).stream()
                .map(LoveNoteResponse::from)
                .toList();
        return ResponseEntity.ok(notes);
    }
}