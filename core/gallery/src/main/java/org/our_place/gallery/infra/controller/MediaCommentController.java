package org.our_place.gallery.infra.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.our_place.gallery.application.service.MediaQueryService;
import org.our_place.gallery.application.usecase.AddMediaCommentUseCase;
import org.our_place.gallery.application.usecase.DeleteMediaCommentUseCase;
import org.our_place.gallery.application.usecase.command.AddMediaCommentCommand;
import org.our_place.gallery.application.usecase.command.DeleteMediaCommentCommand;
import org.our_place.gallery.infra.controller.guard.RoomMembershipGuard;
import org.our_place.gallery.infra.controller.request.AddCommentRequest;
import org.our_place.gallery.infra.controller.response.MediaCommentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("api/v1/rooms/{roomId}/media/{mediaId}/comments")
@RequiredArgsConstructor
public class MediaCommentController {

    private final AddMediaCommentUseCase addMediaCommentUseCase;
    private final DeleteMediaCommentUseCase deleteMediaCommentUseCase;
    private final MediaQueryService mediaQueryService;
    private final RoomMembershipGuard roomMembershipGuard;

    @GetMapping
    public ResponseEntity<List<MediaCommentResponse>> listComments(
            @PathVariable UUID roomId, @PathVariable UUID mediaId) {
        roomMembershipGuard.requireMember(roomId);
        List<MediaCommentResponse> comments = mediaQueryService.listComments(mediaId).stream()
                .map(MediaCommentResponse::from)
                .toList();
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    public ResponseEntity<Void> addComment(
            @PathVariable UUID roomId, @PathVariable UUID mediaId, @Valid @RequestBody AddCommentRequest request) {
        UUID userId = roomMembershipGuard.requireMember(roomId);
        addMediaCommentUseCase.execute(new AddMediaCommentCommand(mediaId, userId, request.content()));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable UUID roomId, @PathVariable UUID mediaId, @PathVariable UUID commentId) {
        UUID userId = roomMembershipGuard.requireMember(roomId);
        deleteMediaCommentUseCase.execute(new DeleteMediaCommentCommand(commentId, userId));
        return ResponseEntity.ok().build();
    }
}