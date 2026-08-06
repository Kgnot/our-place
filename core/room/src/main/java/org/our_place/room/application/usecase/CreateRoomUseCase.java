package org.our_place.room.application.usecase;


import lombok.RequiredArgsConstructor;
import org.our_place.room.api.events.RoomCreatedEvent;
import org.our_place.room.application.usecase.command.CreateRoomCommand;
import org.our_place.room.application.usecase.output.CreateRoomOutput;
import org.our_place.room.domain.entity.LkpRelationshipType;
import org.our_place.room.domain.entity.LkpRoomStatus;
import org.our_place.room.domain.entity.RoomMember;
import org.our_place.room.domain.entity.Rooms;
import org.our_place.room.domain.vo.RoomRole;
import org.our_place.room.domain.vo.RoomStatus;
import org.our_place.room.infra.persistence.repository.LkpRelationshipTypeRepository;
import org.our_place.room.infra.persistence.repository.LkpRoomStatusRepository;
import org.our_place.room.infra.persistence.repository.RoomMemberRepository;
import org.our_place.room.infra.persistence.repository.RoomsRepository;
import org.our_place.shared.application.bus.EventBus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
@Transactional
public class CreateRoomUseCase implements UseCase<CreateRoomCommand, CreateRoomOutput> {

    private final RoomsRepository roomsRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final LkpRoomStatusRepository roomStatusRepository;
    private final LkpRelationshipTypeRepository relationshipTypeRepository;
    private final EventBus eventPublisher;

    @Override
    public CreateRoomOutput execute(CreateRoomCommand command) {
        LkpRoomStatus initialStatus = roomStatusRepository.findById(RoomStatus.ACTIVE.code()) // iniciamos con Active
                .orElseThrow(() -> new IllegalStateException("lkp_room_status seed missing: " + RoomStatus.TRIAL.code()));

        LkpRelationshipType relationshipType = null;
        if (command.relationshipTypeCode() != null) {
            relationshipType = relationshipTypeRepository.findById(command.relationshipTypeCode())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "unknown relationship type: " + command.relationshipTypeCode()));
        }

        Rooms room = Rooms.create(
                command.name(),
                initialStatus,
                relationshipType,
                command.ownerUserId(),
                command.anniversaryDate(),
                command.timezone()
        );
        roomsRepository.save(room);

        RoomMember owner = RoomMember.join(room, command.ownerUserId(), RoomRole.OWNER.code(), null);
        roomMemberRepository.save(owner);

        eventPublisher.publish(new RoomCreatedEvent(room.getId(), room.getOwnerUserId(), command.relationshipTypeCode()));

        return new CreateRoomOutput(room.getId());
    }
}