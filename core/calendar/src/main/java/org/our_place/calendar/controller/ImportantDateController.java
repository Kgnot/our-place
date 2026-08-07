package org.our_place.calendar.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.our_place.calendar.controller.request.CreateImportantDateRequest;
import org.our_place.calendar.controller.response.ImportantDateResponse;
import org.our_place.calendar.service.ImportantDateQueryService;
import org.our_place.calendar.service.dto.ImportantDateDto;
import org.our_place.calendar.usecase.CreateImportantDateWithMediaUseCase;
import org.our_place.calendar.usecase.command.CreateImportantDateWithMediaCommand;
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

    private final CreateImportantDateWithMediaUseCase createImportantDateWithMediaUseCase;
    private final ImportantDateQueryService importantDateQueryService;
    private final SecurityContextApi securityContext;

    @PostMapping()
    public ResponseEntity<ImportantDateResponse> create(
            @PathVariable UUID roomId,
            @Valid @RequestBody CreateImportantDateRequest request) {
        UUID userId = securityContext.getCurrentUserId();

        List<CreateImportantDateWithMediaCommand.MediaItem> mediaItems = request.media() != null
                ? request.media().stream()
                  .map(m -> new CreateImportantDateWithMediaCommand.MediaItem(
                          m.r2Key(), m.mediaTypeCode(), m.mimeType(), m.fileSizeBytes(),
                          m.takenAt(), m.latitude(), m.longitude(), m.caption()
                  ))
                  .toList()
                : null;

        CreateImportantDateOutput output = createImportantDateWithMediaUseCase.execute(
                new CreateImportantDateWithMediaCommand(
                        roomId, request.typeCode(), request.title(), request.eventDate(),
                        request.isRecurring(), request.notifyDaysBefore(), userId, mediaItems
                )
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ImportantDateResponse.from(
                        importantDateQueryService.getDetail(output.id())
                ));
    }

    @GetMapping
    public ResponseEntity<List<ImportantDateDto>> list(@PathVariable UUID roomId) {
        return ResponseEntity.ok(importantDateQueryService.listByRoom(roomId));
    }

}