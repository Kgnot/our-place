package org.our_place.room.application.usecase.command;

import java.util.UUID;

public record LeaveRoomCommand(UUID roomId, UUID userLoginId) {
}