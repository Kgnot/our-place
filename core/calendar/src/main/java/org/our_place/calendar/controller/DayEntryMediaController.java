package org.our_place.calendar.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.our_place.calendar.controller.request.LinkMediaToDayEntryRequest;
import org.our_place.calendar.usecase.LinkMediaToDayEntryUseCase;
import org.our_place.calendar.usecase.command.LinkMediaToDayEntryCommand;
import org.our_place.calendar.usecase.output.LinkMediaToDayEntryOutput;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rooms/{roomId}/day-entries/{entryDate}/media")
@RequiredArgsConstructor
public class DayEntryMediaController {

    private final LinkMediaToDayEntryUseCase linkMediaToDayEntryUseCase;

    @PostMapping
    public ResponseEntity<LinkMediaToDayEntryOutput> link(
            @PathVariable UUID roomId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate entryDate,
            @Valid @RequestBody LinkMediaToDayEntryRequest request
    ) {
        LinkMediaToDayEntryOutput output = linkMediaToDayEntryUseCase.execute(
                new LinkMediaToDayEntryCommand(roomId, entryDate, request.mediaId())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }
}