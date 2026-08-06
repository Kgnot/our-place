package org.our_place.room.application.usecase;

import lombok.RequiredArgsConstructor;
import org.our_place.room.api.events.MemberJoinedEvent;
import org.our_place.room.domain.exception.InvitationAlreadyProcessedException;
import org.our_place.room.domain.exception.InvitationExpiredException;
import org.our_place.room.domain.exception.InvitationNotFoundException;
import org.our_place.room.domain.exception.UserAlreadyMemberException;
import org.our_place.room.domain.vo.InvitationStatus;
import org.our_place.room.domain.entity.RoomInvitation;
import org.our_place.room.domain.entity.RoomMember;
import org.our_place.room.domain.entity.Rooms;
import org.our_place.room.infra.persistence.repository.RoomInvitationRepository;
import org.our_place.room.infra.persistence.repository.RoomMemberRepository;
import org.our_place.room.application.usecase.command.AcceptInvitationCommand;
import org.our_place.room.application.usecase.output.AcceptInvitationOutput;
import org.our_place.shared.application.bus.EventBus;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


/**
 * Acepta una invitación pendiente y da de alta al usuario como room_member.
 */
@Component
@RequiredArgsConstructor
@Transactional
public class AcceptInvitationUseCase implements UseCase<AcceptInvitationCommand, AcceptInvitationOutput> {

    private final RoomInvitationRepository roomInvitationRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final EventBus eventPublisher;

    @Override
    public AcceptInvitationOutput execute(AcceptInvitationCommand command) {
        RoomInvitation invitation = roomInvitationRepository.findByToken(command.token())
                .orElseThrow(() -> new InvitationNotFoundException(command.token()));

        if (!new InvitationStatus(invitation.getStatus()).isPending()) {
            throw new InvitationAlreadyProcessedException(invitation.getId());
        }
        if (invitation.isExpired()) {
            throw new InvitationExpiredException(invitation.getId());
        }

        Rooms room = invitation.getRoom();

        boolean alreadyMember = roomMemberRepository.existsByIdRoomIdAndIdUserLoginId(
                room.getId(), command.acceptingUserId());
        if (alreadyMember) {
            throw new UserAlreadyMemberException(room.getId(), command.acceptingUserId());
        }

        invitation.accept();
        roomInvitationRepository.save(invitation);

        RoomMember member = RoomMember.join(
                room, command.acceptingUserId(), invitation.getRoleCode(), invitation.getInvitedByUserId());
        roomMemberRepository.save(member);

        eventPublisher.publish(new MemberJoinedEvent(room.getId(), command.acceptingUserId(), invitation.getRoleCode()));

        return new AcceptInvitationOutput(room.getId(), invitation.getRoleCode());
    }
}