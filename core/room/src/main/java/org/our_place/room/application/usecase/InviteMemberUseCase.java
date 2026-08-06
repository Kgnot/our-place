package org.our_place.room.application.usecase;

import lombok.RequiredArgsConstructor;
import org.our_place.room.api.events.MemberInvitedEvent;
import org.our_place.room.application.usecase.command.InviteMemberCommand;
import org.our_place.room.application.usecase.output.InviteMemberOutput;
import org.our_place.room.domain.entity.RoomInvitation;
import org.our_place.room.domain.entity.RoomMember;
import org.our_place.room.domain.entity.Rooms;
import org.our_place.room.domain.exception.RoomActionForbiddenException;
import org.our_place.room.domain.exception.RoomMemberNotFoundException;
import org.our_place.room.domain.exception.RoomNotFoundException;
import org.our_place.room.domain.vo.RoomRole;
import org.our_place.room.infra.persistence.repository.RoomInvitationRepository;
import org.our_place.room.infra.persistence.repository.RoomMemberRepository;
import org.our_place.room.infra.persistence.repository.RoomsRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Solo el owner de la room puede invitar (regla de negocio simple, se valida acá porque
 * todavía no amerita moverla a la entidad). Requiere que quien invita ya sea miembro activo.
 */
@Component
@RequiredArgsConstructor
@Transactional
public class InviteMemberUseCase implements UseCase<InviteMemberCommand, InviteMemberOutput> {

    private final RoomsRepository roomsRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final RoomInvitationRepository roomInvitationRepository;
    private final ApplicationEventPublisher eventPublisher;

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

        // Nota: acá solo tenemos el email invitado, no un user_login_id — no podemos validar
        // "ya es miembro" en este punto. Esa validación real ocurre en AcceptInvitationUseCase,
        // contra el userLoginId de quien efectivamente acepta.

        RoomInvitation invitation = RoomInvitation.create(
                room, command.invitedEmail(), command.invitedByUserId(), command.roleCode()
        );
        roomInvitationRepository.save(invitation);

        eventPublisher.publishEvent(new MemberInvitedEvent(
                room.getId(), invitation.getInvitedEmail(), invitation.getToken(), command.invitedByUserId()
        ));

        return new InviteMemberOutput(invitation.getId(), invitation.getToken());
    }
}