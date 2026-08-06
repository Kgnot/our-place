package org.our_place.calendar.usecase;

public interface UseCase<C, R> {
    R execute(C command);
}