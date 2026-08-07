package org.our_place.gallery.application.usecase;

public interface UseCase<C, O> {

    O execute(C command);
}
