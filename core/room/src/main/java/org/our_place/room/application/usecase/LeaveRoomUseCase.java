package org.our_place.room.application.usecase;

import lombok.RequiredArgsConstructor;
import org.our_place.room.api.events.MemberLeftEvent;
import org.our_place.room.domain.exception.RoomMemberNotFoundException;
import org.our_place.room.domain.entity.RoomMember;
import org.our_place.room.infra.persistence.repository.RoomMemberRepository;
import org.our_place.room.application.usecase.command.LeaveRoomCommand;
import org.our_place.room.application.usecase.output.LeaveRoomOutput;
import org.our_place.shared.application.bus.EventBus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Un solo save, apoyado en dirty checking — igual se deja @Transactional explícito por
 * consistencia con el resto de casos de uso de escritura del módulo (ver §3).
 */
@Component
@RequiredArgsConstructor
@Transactional
public class LeaveRoomUseCase implements UseCase<LeaveRoomCommand, LeaveRoomOutput> {

    private final RoomMemberRepository roomMemberRepository;
    private final EventBus eventPublisher;

    @Override
    public LeaveRoomOutput execute(LeaveRoomCommand command) {
        RoomMember member = roomMemberRepository
                .findByIdRoomIdAndIdUserLoginId(command.roomId(), command.userLoginId())
                .orElseThrow(() -> new RoomMemberNotFoundException(command.roomId(), command.userLoginId()));

        member.leave();

        eventPublisher.publish(new MemberLeftEvent(command.roomId(), command.userLoginId()));

        return new LeaveRoomOutput(true);
    }
}