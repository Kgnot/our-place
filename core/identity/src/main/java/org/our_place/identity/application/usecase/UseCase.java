package org.our_place.identity.application.usecase;

public interface UseCase<C, R> {

    R execute(C command);
}
