package org.our_place.calendar.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.our_place.calendar.controller.request.CreateDayEntryRequest;
import org.our_place.calendar.service.DayEntryQueryService;
import org.our_place.calendar.service.dto.DayEntryDto;
import org.our_place.calendar.usecase.CreateDayEntryUseCase;
import org.our_place.calendar.usecase.command.CreateDayEntryCommand;
import org.our_place.calendar.usecase.output.CreateDayEntryOutput;
import org.our_place.identity.api.SecurityContextApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rooms/{roomId}/day-entries")
@RequiredArgsConstructor
public class DayEntryController {

    private final CreateDayEntryUseCase createDayEntryUseCase;
    private final DayEntryQueryService dayEntryQueryService;
    private final SecurityContextApi securityContext;

    @PostMapping
    public ResponseEntity<CreateDayEntryOutput> create(
            @PathVariable UUID roomId,
            @Valid @RequestBody CreateDayEntryRequest request
    ) {
        UUID userId = securityContext.getCurrentUserId();
        CreateDayEntryOutput output = createDayEntryUseCase.execute(new CreateDayEntryCommand(
                roomId, request.entryDate(), userId, request.content(), request.moodEmoji()
        ));
        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }

    @GetMapping
    public ResponseEntity<List<DayEntryDto>> list(@PathVariable UUID roomId) {
        return ResponseEntity.ok(dayEntryQueryService.listByRoom(roomId));
    }
}