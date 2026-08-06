package org.our_place.affection.usecase;

public interface UseCase<C, R> {
    R execute(C command);
}