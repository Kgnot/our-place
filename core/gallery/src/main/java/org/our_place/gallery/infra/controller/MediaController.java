package org.our_place.gallery.infra.controller;

import lombok.RequiredArgsConstructor;
import org.our_place.gallery.application.service.MediaQueryService;
import org.our_place.gallery.application.usecase.DeleteMediaUseCase;
import org.our_place.gallery.application.usecase.UpdateMediaCaptionUseCase;
import org.our_place.gallery.application.usecase.command.DeleteMediaCommand;
import org.our_place.gallery.application.usecase.command.UpdateMediaCaptionCommand;
import org.our_place.gallery.infra.controller.guard.RoomMembershipGuard;
import org.our_place.gallery.infra.controller.request.UpdateCaptionRequest;
import org.our_place.gallery.infra.controller.response.MediaDetailResponse;
import org.our_place.gallery.infra.controller.response.MediaSummaryResponse;
import org.our_place.shared.infra.web.PageDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/rooms/{roomId}/media")
@RequiredArgsConstructor
public class MediaController {

    private final DeleteMediaUseCase deleteMediaUseCase;
    private final UpdateMediaCaptionUseCase updateMediaCaptionUseCase;
    private final MediaQueryService mediaQueryService;
    private final RoomMembershipGuard roomMembershipGuard;

    // ─── Todas las fotos (paginado) ───────────────────────

    @GetMapping
    public ResponseEntity<PageDto<MediaSummaryResponse>> listMedia(
            @PathVariable UUID roomId,
            @PageableDefault(size = 30, sort = "takenAt", direction = Sort.Direction.DESC) Pageable pageable) {
        roomMembershipGuard.requireMember(roomId);
        PageDto<MediaSummaryResponse> page = PageDto.from(
                mediaQueryService.listByRoom(roomId, pageable).map(MediaSummaryResponse::from)
        );
        return ResponseEntity.ok(page);
    }

    // ─── Últimas fotos subidas ────────────────────────────

    @GetMapping("/latest")
    public ResponseEntity<PageDto<MediaSummaryResponse>> listLatest(
            @PathVariable UUID roomId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        roomMembershipGuard.requireMember(roomId);
        PageDto<MediaSummaryResponse> page = PageDto.from(
                mediaQueryService.listLatestByRoom(roomId, pageable).map(MediaSummaryResponse::from)
        );
        return ResponseEntity.ok(page);
    }

    // ─── Fotos de un rango de fechas ─────────────────────

    @GetMapping("/by-date")
    public ResponseEntity<PageDto<MediaSummaryResponse>> listByDateRange(
            @PathVariable UUID roomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @PageableDefault(size = 30) Pageable pageable) {
        roomMembershipGuard.requireMember(roomId);
        OffsetDateTime start = from.atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime end = to.plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC);

        PageDto<MediaSummaryResponse> page = PageDto.from(
                mediaQueryService.listByRoomAndDateRange(roomId, start, end, pageable)
                        .map(MediaSummaryResponse::from)
        );
        return ResponseEntity.ok(page);
    }

    // ─── Fotos de este mes ───────────────────────────────

    @GetMapping("/this-month")
    public ResponseEntity<PageDto<MediaSummaryResponse>> listThisMonth(
            @PathVariable UUID roomId,
            @RequestParam int year,
            @RequestParam int month,
            @PageableDefault(size = 30) Pageable pageable) {
        roomMembershipGuard.requireMember(roomId);
        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDate lastDay = firstDay.with(java.time.temporal.TemporalAdjusters.lastDayOfMonth());

        OffsetDateTime start = firstDay.atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime end = lastDay.plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC);

        PageDto<MediaSummaryResponse> page = PageDto.from(
                mediaQueryService.listByRoomAndDateRange(roomId, start, end, pageable)
                        .map(MediaSummaryResponse::from)
        );
        return ResponseEntity.ok(page);
    }

    // ─── Fotos de esta semana ────────────────────────────

    @GetMapping("/this-week")
    public ResponseEntity<PageDto<MediaSummaryResponse>> listThisWeek(
            @PathVariable UUID roomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate referenceDate,
            @PageableDefault(size = 30) Pageable pageable) {
        roomMembershipGuard.requireMember(roomId);
        // Semana de Lunes a Domingo basada en referenceDate
        LocalDate monday = referenceDate.with(java.time.DayOfWeek.MONDAY);
        LocalDate sunday = monday.plusDays(6);

        OffsetDateTime start = monday.atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime end = sunday.plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC);

        PageDto<MediaSummaryResponse> page = PageDto.from(
                mediaQueryService.listByRoomAndDateRange(roomId, start, end, pageable)
                        .map(MediaSummaryResponse::from)
        );
        return ResponseEntity.ok(page);
    }

    // ─── Detalle de una foto ─────────────────────────────

    @GetMapping("/{mediaId}")
    public ResponseEntity<MediaDetailResponse> getMedia(
            @PathVariable UUID roomId, @PathVariable UUID mediaId) {
        UUID userId = roomMembershipGuard.requireMember(roomId);
        return ResponseEntity.ok(MediaDetailResponse.from(mediaQueryService.getDetail(mediaId, userId)));
    }

    // ─── Modificar caption ───────────────────────────────

    @PatchMapping("/{mediaId}/caption")
    public ResponseEntity<Void> updateCaption(
            @PathVariable UUID roomId, @PathVariable UUID mediaId,
            @RequestBody UpdateCaptionRequest request) {
        UUID userId = roomMembershipGuard.requireMember(roomId);
        updateMediaCaptionUseCase.execute(new UpdateMediaCaptionCommand(mediaId, userId, request.caption()));
        return ResponseEntity.ok().build();
    }

    // ─── Eliminar foto ───────────────────────────────────

    @DeleteMapping("/{mediaId}")
    public ResponseEntity<Void> deleteMedia(
            @PathVariable UUID roomId, @PathVariable UUID mediaId) {
        UUID userId = roomMembershipGuard.requireMember(roomId);
        deleteMediaUseCase.execute(new DeleteMediaCommand(mediaId, userId));
        return ResponseEntity.ok().build();
    }
}