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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/rooms/{roomId}/media")
@RequiredArgsConstructor
public class MediaController {

    private final DeleteMediaUseCase deleteMediaUseCase;
    private final UpdateMediaCaptionUseCase updateMediaCaptionUseCase;
    private final MediaQueryService mediaQueryService;
    private final RoomMembershipGuard roomMembershipGuard;

    @GetMapping
    public ResponseEntity<PageDto<MediaSummaryResponse>> listMedia(
            @PathVariable UUID roomId,
            @PageableDefault(size = 30, sort = "takenAt", direction = Sort.Direction.DESC) Pageable pageable) {
        roomMembershipGuard.requireMember(roomId);
        PageDto<MediaSummaryResponse> page = PageDto.from(
                mediaQueryService.listByRoom(roomId, pageable)
                        .map(MediaSummaryResponse::from)
        );
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{mediaId}")
    public ResponseEntity<MediaDetailResponse> getMedia(@PathVariable UUID roomId, @PathVariable UUID mediaId) {
        UUID userId = roomMembershipGuard.requireMember(roomId);
        return ResponseEntity.ok(MediaDetailResponse.from(mediaQueryService.getDetail(mediaId, userId)));
    }

    @PatchMapping("/{mediaId}/caption")
    public ResponseEntity<Void> updateCaption(
            @PathVariable UUID roomId, @PathVariable UUID mediaId, @RequestBody UpdateCaptionRequest request) {
        UUID userId = roomMembershipGuard.requireMember(roomId);
        updateMediaCaptionUseCase.execute(new UpdateMediaCaptionCommand(mediaId, userId, request.caption()));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{mediaId}")
    public ResponseEntity<Void> deleteMedia(@PathVariable UUID roomId, @PathVariable UUID mediaId) {
        UUID userId = roomMembershipGuard.requireMember(roomId);
        deleteMediaUseCase.execute(new DeleteMediaCommand(mediaId, userId));
        return ResponseEntity.ok().build();
    }
}