package org.our_place.room.application.usecase;

import lombok.RequiredArgsConstructor;
import org.our_place.room.domain.exception.RoomMemberNotFoundException;
import org.our_place.room.domain.entity.RoomMember;
import org.our_place.room.infra.persistence.repository.RoomMemberRepository;
import org.our_place.room.application.usecase.command.UpdateNicknameCommand;
import org.our_place.room.application.usecase.output.UpdateNicknameOutput;
import org.our_place.shared.application.bus.EventBus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class UpdateNicknameUseCase implements UseCase<UpdateNicknameCommand, UpdateNicknameOutput> {

    private final RoomMemberRepository roomMemberRepository;
    private final EventBus eventPublisher;

    @Override
    public UpdateNicknameOutput execute(UpdateNicknameCommand command) {
        RoomMember member = roomMemberRepository
                .findByIdRoomIdAndIdUserLoginId(command.roomId(), command.userLoginId())
                .orElseThrow(() -> new RoomMemberNotFoundException(command.roomId(), command.userLoginId()));

        member.updateNickname(command.nickname());
        //!info: no hacemos evento aqui, al ser tan simple
        return new UpdateNicknameOutput(member.getNickname());
    }
}