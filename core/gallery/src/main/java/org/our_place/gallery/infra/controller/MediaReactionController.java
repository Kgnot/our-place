package org.our_place.gallery.infra.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.our_place.gallery.application.usecase.ReactToMediaUseCase;
import org.our_place.gallery.application.usecase.RemoveMediaReactionUseCase;
import org.our_place.gallery.application.usecase.command.ReactToMediaCommand;
import org.our_place.gallery.application.usecase.command.RemoveMediaReactionCommand;
import org.our_place.gallery.infra.controller.guard.RoomMembershipGuard;
import org.our_place.gallery.infra.controller.request.ReactRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@RequestMapping("api/v1/rooms/{roomId}/media/{mediaId}/reactions/me")
@RequiredArgsConstructor
public class MediaReactionController {

    private final ReactToMediaUseCase reactToMediaUseCase;
    private final RemoveMediaReactionUseCase removeMediaReactionUseCase;
    private final RoomMembershipGuard roomMembershipGuard;

    @PutMapping
    public ResponseEntity<Void> react(
            @PathVariable UUID roomId, @PathVariable UUID mediaId, @Valid @RequestBody ReactRequest request) {
        UUID userId = roomMembershipGuard.requireMember(roomId);
        reactToMediaUseCase.execute(new ReactToMediaCommand(mediaId, userId, request.reactionType()));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeReaction(@PathVariable UUID roomId, @PathVariable UUID mediaId) {
        UUID userId = roomMembershipGuard.requireMember(roomId);
        removeMediaReactionUseCase.execute(new RemoveMediaReactionCommand(mediaId, userId));
        return ResponseEntity.ok().build();
    }
}