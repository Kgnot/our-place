package org.our_place.room.infra.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.our_place.identity.api.SecurityContextApi;
import org.our_place.room.infra.controller.request.CreateRoomRequest;
import org.our_place.room.infra.controller.request.InviteMemberRequest;
import org.our_place.room.infra.controller.request.SetRelationshipRequest;
import org.our_place.room.infra.controller.request.UpdateNicknameRequest;
import org.our_place.room.infra.controller.response.InvitationResponse;
import org.our_place.room.infra.controller.response.RoomMemberResponse;
import org.our_place.room.infra.controller.response.RoomResponse;
import org.our_place.room.application.service.RoomQueryService;
import org.our_place.room.application.usecase.AcceptInvitationUseCase;
import org.our_place.room.application.usecase.CreateRoomUseCase;
import org.our_place.room.application.usecase.InviteMemberUseCase;
import org.our_place.room.application.usecase.LeaveRoomUseCase;
import org.our_place.room.application.usecase.SetMemberRelationshipUseCase;
import org.our_place.room.application.usecase.UpdateNicknameUseCase;
import org.our_place.room.application.usecase.command.AcceptInvitationCommand;
import org.our_place.room.application.usecase.command.CreateRoomCommand;
import org.our_place.room.application.usecase.command.InviteMemberCommand;
import org.our_place.room.application.usecase.command.LeaveRoomCommand;
import org.our_place.room.application.usecase.command.SetMemberRelationshipCommand;
import org.our_place.room.application.usecase.command.UpdateNicknameCommand;
import org.our_place.room.application.usecase.output.CreateRoomOutput;
import org.our_place.room.application.usecase.output.InviteMemberOutput;
import org.our_place.room.infra.controller.response.UserRoomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * SOLO capa HTTP (§8): no implementa RoomApi, inyecta los casos de uso directo. El id de quien
 * ejecuta la acción sale siempre de SecurityContextApi, nunca del body ni del path — así nadie
 * puede suplantar a otro usuario. El recurso sobre el que se actúa (roomId) va en la ruta.
 */
@RestController
@RequestMapping("api/v1/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final CreateRoomUseCase createRoomUseCase;
    private final InviteMemberUseCase inviteMemberUseCase;
    private final AcceptInvitationUseCase acceptInvitationUseCase;
    private final LeaveRoomUseCase leaveRoomUseCase;
    private final UpdateNicknameUseCase updateNicknameUseCase;
    private final SetMemberRelationshipUseCase setMemberRelationshipUseCase;
    private final RoomQueryService roomQueryService;
    private final SecurityContextApi securityContextApi;

    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(@Valid @RequestBody CreateRoomRequest request) {
        UUID ownerUserId = securityContextApi.getCurrentUserId();
        CreateRoomOutput output = createRoomUseCase.execute(new CreateRoomCommand(
                request.name(), request.relationshipTypeCode(), request.anniversaryDate(),
                request.timezone(), ownerUserId
        ));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RoomResponse.from(roomQueryService.getRoomDetail(output.roomId())));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomResponse> getRoom(@PathVariable UUID roomId) {
        return ResponseEntity.ok(RoomResponse.from(roomQueryService.getRoomDetail(roomId)));
    }

    @GetMapping("/{roomId}/members")
    public ResponseEntity<List<RoomMemberResponse>> listMembers(@PathVariable UUID roomId) {
        List<RoomMemberResponse> members = roomQueryService.listMembers(roomId).stream()
                .map(RoomMemberResponse::from)
                .toList();
        return ResponseEntity.ok(members);
    }

    @PostMapping("/{roomId}/invitations")
    public ResponseEntity<InvitationResponse> inviteMember(
            @PathVariable UUID roomId, @Valid @RequestBody InviteMemberRequest request) {
        UUID invitedByUserId = securityContextApi.getCurrentUserId();
        InviteMemberOutput output = inviteMemberUseCase.execute(new InviteMemberCommand(
                roomId, request.invitedEmail(), request.roleCode(), invitedByUserId
        ));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new InvitationResponse(output.invitationId(), output.token()));
    }

    @PostMapping("/invitations/{token}/accept")
    public ResponseEntity<RoomResponse> acceptInvitation(@PathVariable String token) {
        UUID acceptingUserId = securityContextApi.getCurrentUserId();
        var output = acceptInvitationUseCase.execute(new AcceptInvitationCommand(token, acceptingUserId));
        return ResponseEntity.ok(RoomResponse.from(roomQueryService.getRoomDetail(output.roomId())));
    }

    @DeleteMapping("/{roomId}/members/me")
    public ResponseEntity<Void> leaveRoom(@PathVariable UUID roomId) {
        UUID userLoginId = securityContextApi.getCurrentUserId();
        leaveRoomUseCase.execute(new LeaveRoomCommand(roomId, userLoginId));
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{roomId}/members/me/nickname")
    public ResponseEntity<Void> updateNickname(
            @PathVariable UUID roomId, @Valid @RequestBody UpdateNicknameRequest request) {
        UUID userLoginId = securityContextApi.getCurrentUserId();
        updateNicknameUseCase.execute(new UpdateNicknameCommand(roomId, userLoginId, request.nickname()));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{roomId}/relationships")
    public ResponseEntity<Void> setRelationship(
            @PathVariable UUID roomId, @Valid @RequestBody SetRelationshipRequest request) {
        setMemberRelationshipUseCase.execute(new SetMemberRelationshipCommand(
                roomId, request.memberAUserId(), request.memberBUserId(),
                request.relationshipTypeCode(), request.sinceDate()
        ));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/mine")
    public ResponseEntity<List<UserRoomResponse>> listMyRooms() {
        UUID userLoginId = securityContextApi.getCurrentUserId();
        List<UserRoomResponse> rooms = roomQueryService.listActiveRoomsForUser(userLoginId).stream()
                .map(UserRoomResponse::from)
                .toList();
        return ResponseEntity.ok(rooms);
    }
}