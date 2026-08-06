package org.our_place.room.application.usecase;

public interface UseCase<C, O> {
    O execute(C command);
}
