package org.our_place.room.application.usecase.command;


import java.util.UUID;

public record UpdateNicknameCommand(UUID roomId, UUID userLoginId, String nickname) {
}