package org.our_place.gallery.infra.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.our_place.gallery.api.external.RoomExternalApi;
import org.our_place.gallery.application.service.MediaQueryService;
import org.our_place.gallery.application.usecase.AddMediaCommentUseCase;
import org.our_place.gallery.application.usecase.DeleteMediaCommentUseCase;
import org.our_place.gallery.application.usecase.DeleteMediaUseCase;
import org.our_place.gallery.application.usecase.ReactToMediaUseCase;
import org.our_place.gallery.application.usecase.RemoveMediaReactionUseCase;
import org.our_place.gallery.application.usecase.UpdateMediaCaptionUseCase;
import org.our_place.gallery.application.usecase.UploadMediaUseCase;
import org.our_place.gallery.application.usecase.command.AddMediaCommentCommand;
import org.our_place.gallery.application.usecase.command.DeleteMediaCommand;
import org.our_place.gallery.application.usecase.command.DeleteMediaCommentCommand;
import org.our_place.gallery.application.usecase.command.ReactToMediaCommand;
import org.our_place.gallery.application.usecase.command.RemoveMediaReactionCommand;
import org.our_place.gallery.application.usecase.command.UpdateMediaCaptionCommand;
import org.our_place.gallery.application.usecase.command.UploadMediaCommand;
import org.our_place.gallery.application.usecase.output.UploadMediaOutput;
import org.our_place.gallery.domain.exception.GalleryAccessForbiddenException;
import org.our_place.gallery.infra.controller.request.*;
import org.our_place.gallery.infra.controller.response.BatchUploadResponse;
import org.our_place.gallery.infra.controller.response.MediaCommentResponse;
import org.our_place.gallery.infra.controller.response.MediaDetailResponse;
import org.our_place.gallery.infra.controller.response.MediaSummaryResponse;
import org.our_place.gallery.infra.r2.R2PresignedUrlGenerator;
import org.our_place.identity.api.SecurityContextApi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * SOLO capa HTTP (§8). roomId va en la ruta, el actor sale de SecurityContextApi. Valida
 * membresía contra RoomApi (§7) antes de cualquier operación de lectura o escritura.
 */
@RestController
@RequestMapping("/rooms/{roomId}/media")
@RequiredArgsConstructor
public class MediaController {

    private final UploadMediaUseCase uploadMediaUseCase;
    private final DeleteMediaUseCase deleteMediaUseCase;
    private final UpdateMediaCaptionUseCase updateMediaCaptionUseCase;
    private final AddMediaCommentUseCase addMediaCommentUseCase;
    private final DeleteMediaCommentUseCase deleteMediaCommentUseCase;
    private final ReactToMediaUseCase reactToMediaUseCase;
    private final RemoveMediaReactionUseCase removeMediaReactionUseCase;
    private final MediaQueryService mediaQueryService;
    private final SecurityContextApi securityContextApi;
    private final R2PresignedUrlGenerator r2PresignedUrlGenerator;
    private final RoomExternalApi roomApi;


    @PostMapping("/presign")
    public ResponseEntity<BatchUploadResponse> presignUpload(
            @PathVariable UUID roomId, @Valid @RequestBody BatchUploadRequest request) {
        requireMember(roomId);

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
        UUID userId = requireMember(roomId);

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
        UUID userId = requireMember(roomId);
        UploadMediaOutput output = uploadMediaUseCase.execute(new UploadMediaCommand(
                roomId, userId, request.r2Url(), request.mediaTypeCode(), request.mimeType(),
                request.fileSizeBytes(), request.takenAt(), request.caption()
        ));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MediaDetailResponse.from(mediaQueryService.getDetail(output.mediaId(), userId)));
    }

    @GetMapping
    public ResponseEntity<Page<MediaSummaryResponse>> listMedia(
            @PathVariable UUID roomId,
            @PageableDefault(size = 30, sort = "takenAt", direction = Sort.Direction.DESC) Pageable pageable) {
        requireMember(roomId);
        Page<MediaSummaryResponse> page = mediaQueryService.listByRoom(roomId, pageable)
                .map(MediaSummaryResponse::from);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{mediaId}")
    public ResponseEntity<MediaDetailResponse> getMedia(@PathVariable UUID roomId, @PathVariable UUID mediaId) {
        UUID userId = requireMember(roomId);
        return ResponseEntity.ok(MediaDetailResponse.from(mediaQueryService.getDetail(mediaId, userId)));
    }

    @PatchMapping("/{mediaId}/caption")
    public ResponseEntity<Void> updateCaption(
            @PathVariable UUID roomId, @PathVariable UUID mediaId, @RequestBody UpdateCaptionRequest request) {
        UUID userId = requireMember(roomId);
        updateMediaCaptionUseCase.execute(new UpdateMediaCaptionCommand(mediaId, userId, request.caption()));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{mediaId}")
    public ResponseEntity<Void> deleteMedia(@PathVariable UUID roomId, @PathVariable UUID mediaId) {
        UUID userId = requireMember(roomId);
        deleteMediaUseCase.execute(new DeleteMediaCommand(mediaId, userId));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{mediaId}/comments")
    public ResponseEntity<List<MediaCommentResponse>> listComments(
            @PathVariable UUID roomId, @PathVariable UUID mediaId) {
        requireMember(roomId);
        List<MediaCommentResponse> comments = mediaQueryService.listComments(mediaId).stream()
                .map(MediaCommentResponse::from)
                .toList();
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/{mediaId}/comments")
    public ResponseEntity<Void> addComment(
            @PathVariable UUID roomId, @PathVariable UUID mediaId, @Valid @RequestBody AddCommentRequest request) {
        UUID userId = requireMember(roomId);
        addMediaCommentUseCase.execute(new AddMediaCommentCommand(mediaId, userId, request.content()));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{mediaId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable UUID roomId, @PathVariable UUID mediaId, @PathVariable UUID commentId) {
        UUID userId = requireMember(roomId);
        deleteMediaCommentUseCase.execute(new DeleteMediaCommentCommand(commentId, userId));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{mediaId}/reactions/me")
    public ResponseEntity<Void> react(
            @PathVariable UUID roomId, @PathVariable UUID mediaId, @Valid @RequestBody ReactRequest request) {
        UUID userId = requireMember(roomId);
        reactToMediaUseCase.execute(new ReactToMediaCommand(mediaId, userId, request.reactionType()));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{mediaId}/reactions/me")
    public ResponseEntity<Void> removeReaction(@PathVariable UUID roomId, @PathVariable UUID mediaId) {
        UUID userId = requireMember(roomId);
        removeMediaReactionUseCase.execute(new RemoveMediaReactionCommand(mediaId, userId));
        return ResponseEntity.ok().build();
    }

    private UUID requireMember(UUID roomId) {
        UUID userId = securityContextApi.getCurrentUserId();
        if (!roomApi.isMember(roomId, userId)) {
            throw new GalleryAccessForbiddenException(roomId, userId);
        }
        return userId;
    }
}