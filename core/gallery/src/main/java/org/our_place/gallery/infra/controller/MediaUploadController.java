package org.our_place.gallery.infra.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.our_place.gallery.application.service.MediaQueryService;
import org.our_place.gallery.application.usecase.UploadMediaUseCase;
import org.our_place.gallery.application.usecase.command.UploadMediaCommand;
import org.our_place.gallery.application.usecase.output.UploadMediaOutput;
import org.our_place.gallery.infra.controller.guard.RoomMembershipGuard;
import org.our_place.gallery.infra.controller.request.BatchUploadRequest;
import org.our_place.gallery.infra.controller.request.ConfirmUploadRequest;
import org.our_place.gallery.infra.controller.request.UploadMediaRequest;
import org.our_place.gallery.infra.controller.response.BatchUploadResponse;
import org.our_place.gallery.infra.controller.response.MediaDetailResponse;
import org.our_place.gallery.infra.controller.response.MediaSummaryResponse;
import org.our_place.gallery.infra.r2.R2PresignedUrlGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("api/v1/rooms/{roomId}/media")
@RequiredArgsConstructor
public class MediaUploadController {

    private final UploadMediaUseCase uploadMediaUseCase;
    private final MediaQueryService mediaQueryService;
    private final R2PresignedUrlGenerator r2PresignedUrlGenerator;
    private final RoomMembershipGuard roomMembershipGuard;

    @PostMapping("/presign")
    public ResponseEntity<BatchUploadResponse> presignUpload(
            @PathVariable UUID roomId, @Valid @RequestBody BatchUploadRequest request) {
        roomMembershipGuard.requireMember(roomId);

        List<BatchUploadResponse.UploadItem> items = request.entries().stream().map(entry -> {
            UUID mediaId = UUID.randomUUID();
            var presigned = r2PresignedUrlGenerator.generate(roomId, mediaId, entry.mimeType());
            return new BatchUploadResponse.UploadItem(mediaId, presigned.uploadUrl(), presigned.r2Key());
        }).toList();

        return ResponseEntity.ok(new BatchUploadResponse(items));
    }

    @PostMapping("/confirm")
    public ResponseEntity<List<MediaSummaryResponse>> confirmUpload(
            @PathVariable UUID roomId, @Valid @RequestBody ConfirmUploadRequest request) {
        UUID userId = roomMembershipGuard.requireMember(roomId);

        List<MediaSummaryResponse> result = request.items().stream()
                .map(item -> {
                    UploadMediaOutput output = uploadMediaUseCase.execute(new UploadMediaCommand(
                            roomId, userId, item.r2Key(), item.mediaTypeCode().code(),
                            item.mimeType(), item.fileSizeBytes(),
                            item.takenAt(), item.caption()
                    ));
                    return MediaSummaryResponse.from(mediaQueryService.getMediaById(output.mediaId()));
                })
                .toList();

        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<MediaDetailResponse> uploadMedia(
            @PathVariable UUID roomId, @Valid @RequestBody UploadMediaRequest request) {
        UUID userId = roomMembershipGuard.requireMember(roomId);
        UploadMediaOutput output = uploadMediaUseCase.execute(new UploadMediaCommand(
                roomId, userId, request.r2Url(), request.mediaTypeCode(), request.mimeType(),
                request.fileSizeBytes(), request.takenAt(), request.caption()
        ));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MediaDetailResponse.from(mediaQueryService.getDetail(output.mediaId(), userId)));
    }
}