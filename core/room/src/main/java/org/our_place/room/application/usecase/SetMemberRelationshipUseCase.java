package org.our_place.room.application.usecase;

import lombok.RequiredArgsConstructor;
import org.our_place.room.api.events.MemberRelationshipSetEvent;
import org.our_place.room.api.external.IdentityExternalApi;
import org.our_place.room.domain.exception.RoomMemberNotFoundException;
import org.our_place.room.domain.exception.RoomNotFoundException;
import org.our_place.room.domain.entity.LkpRelationshipType;
import org.our_place.room.domain.entity.MemberRelationship;
import org.our_place.room.domain.entity.Rooms;
import org.our_place.room.infra.persistence.repository.LkpRelationshipTypeRepository;
import org.our_place.room.infra.persistence.repository.MemberRelationshipRepository;
import org.our_place.room.infra.persistence.repository.RoomMemberRepository;
import org.our_place.room.infra.persistence.repository.RoomsRepository;
import org.our_place.room.application.usecase.command.SetMemberRelationshipCommand;
import org.our_place.room.application.usecase.output.SetMemberRelationshipOutput;
import org.our_place.shared.application.bus.EventBus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Registra el tipo de relación entre dos miembros de la misma room (ej. "partner desde tal fecha").
 */
@Component
@RequiredArgsConstructor
@Transactional
public class SetMemberRelationshipUseCase implements UseCase<SetMemberRelationshipCommand, SetMemberRelationshipOutput> {

    private final RoomsRepository roomsRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final MemberRelationshipRepository memberRelationshipRepository;
    private final LkpRelationshipTypeRepository relationshipTypeRepository;
    private final IdentityExternalApi identityExternalApi;
    private final EventBus eventBus;

    @Override
    public SetMemberRelationshipOutput execute(SetMemberRelationshipCommand command) {
        Rooms room = roomsRepository.findById(command.roomId())
                .orElseThrow(() -> new RoomNotFoundException(command.roomId()));

        requireMember(command.roomId(), command.memberAUserId());
        requireMember(command.roomId(), command.memberBUserId());

        LkpRelationshipType type = relationshipTypeRepository.findById(command.relationshipTypeCode())
                .orElseThrow(() -> new IllegalArgumentException(
                        "unknown relationship type: " + command.relationshipTypeCode()));

        MemberRelationship relationship = MemberRelationship.create(
                room, command.memberAUserId(), command.memberBUserId(), type, command.sinceDate());
        memberRelationshipRepository.save(relationship);
        // publicamos el evento
        eventBus.publish(new MemberRelationshipSetEvent(
                new MemberRelationshipSetEvent.Member(
                        command.memberAUserId().toString(),
                        identityExternalApi.findNameUserById(command.memberAUserId())
                ),
                new MemberRelationshipSetEvent.Member(
                        command.memberBUserId().toString(),
                        identityExternalApi.findNameUserById(command.memberBUserId())
                ),
                command.relationshipTypeCode(),
                command.sinceDate()
        ));

        return new SetMemberRelationshipOutput(true);
    }

    private void requireMember(java.util.UUID roomId, java.util.UUID userLoginId) {
        if (!roomMemberRepository.existsByIdRoomIdAndIdUserLoginId(roomId, userLoginId)) {
            throw new RoomMemberNotFoundException(roomId, userLoginId);
        }
    }
}