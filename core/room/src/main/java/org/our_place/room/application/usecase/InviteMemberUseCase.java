package org.our_place.room.application.usecase;

import lombok.RequiredArgsConstructor;
import org.our_place.room.api.events.MemberInvitedEvent;
import org.our_place.room.api.external.IdentityExternalApi;
import org.our_place.room.application.usecase.command.InviteMemberCommand;
import org.our_place.room.application.usecase.output.InviteMemberOutput;
import org.our_place.room.domain.entity.RoomInvitation;
import org.our_place.room.domain.entity.RoomMember;
import org.our_place.room.domain.entity.Rooms;
import org.our_place.room.domain.exception.RoomActionForbiddenException;
import org.our_place.room.domain.exception.RoomMemberNotFoundException;
import org.our_place.room.domain.exception.RoomNotFoundException;
import org.our_place.room.domain.exception.UserAlreadyMemberException;
import org.our_place.room.domain.vo.RoomRole;
import org.our_place.room.infra.persistence.repository.RoomInvitationRepository;
import org.our_place.room.infra.persistence.repository.RoomMemberRepository;
import org.our_place.room.infra.persistence.repository.RoomsRepository;
import org.our_place.shared.application.bus.EventBus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Component
@RequiredArgsConstructor
@Transactional
public class InviteMemberUseCase implements UseCase<InviteMemberCommand, InviteMemberOutput> {

    private final RoomsRepository roomsRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final RoomInvitationRepository roomInvitationRepository;
    private final EventBus eventPublisher;
    private final IdentityExternalApi identityExternalApi;

    @Override
    public InviteMemberOutput execute(InviteMemberCommand command) {
        Rooms room = roomsRepository.findById(command.roomId())
                .orElseThrow(() -> new RoomNotFoundException(command.roomId()));

        RoomMember inviter = roomMemberRepository
                .findByIdRoomIdAndIdUserLoginId(command.roomId(), command.invitedByUserId())
                .orElseThrow(() -> new RoomMemberNotFoundException(command.roomId(), command.invitedByUserId()));

        if (!new RoomRole(inviter.getRoleCode()).isOwner()) {
            throw new RoomActionForbiddenException(command.roomId(), command.invitedByUserId());
        }

        // Buscamos el ID del usuario por su email
        UUID invitedUserId = identityExternalApi.findUserLoginIdByEmail(command.invitedEmail());

        roomMemberRepository.findByIdRoomIdAndIdUserLoginId(command.roomId(), invitedUserId)
                .ifPresent(member -> {
                    throw new UserAlreadyMemberException(command.roomId(), invitedUserId);
                });

        RoomInvitation invitation = RoomInvitation.create(
                room, command.invitedEmail(), command.invitedByUserId(), command.roleCode()
        );
        roomInvitationRepository.save(invitation);

        eventPublisher.publish(new MemberInvitedEvent(
                room.getId(), invitation.getInvitedEmail(), invitation.getToken(), command.invitedByUserId()
        ));

        return new InviteMemberOutput(invitation.getId(), invitation.getToken());
    }
}