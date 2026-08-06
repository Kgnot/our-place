package org.our_place.calendar.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.our_place.calendar.controller.request.CreateImportantDateRequest;
import org.our_place.calendar.service.ImportantDateQueryService;
import org.our_place.calendar.service.dto.ImportantDateDto;
import org.our_place.calendar.usecase.CreateImportantDateUseCase;
import org.our_place.calendar.usecase.command.CreateImportantDateCommand;
import org.our_place.calendar.usecase.output.CreateImportantDateOutput;
import org.our_place.identity.api.SecurityContextApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rooms/{roomId}/important-dates")
@RequiredArgsConstructor
public class ImportantDateController {

    private final CreateImportantDateUseCase createImportantDateUseCase;
    private final ImportantDateQueryService importantDateQueryService;
    private final SecurityContextApi securityContext;

    @PostMapping
    public ResponseEntity<CreateImportantDateOutput> create(
            @PathVariable UUID roomId,
            @Valid @RequestBody CreateImportantDateRequest request
    ) {
        UUID userId = securityContext.getCurrentUserId();
        CreateImportantDateOutput output = createImportantDateUseCase.execute(new CreateImportantDateCommand(
                roomId, request.typeCode(), request.title(), request.eventDate(),
                request.isRecurring(), request.notifyDaysBefore(), userId
        ));
        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }

    @GetMapping
    public ResponseEntity<List<ImportantDateDto>> list(@PathVariable UUID roomId) {
        return ResponseEntity.ok(importantDateQueryService.listByRoom(roomId));
    }
}