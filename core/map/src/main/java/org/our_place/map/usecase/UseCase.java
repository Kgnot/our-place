package org.our_place.map.usecase;

public interface UseCase <C,O>{

    O execute(C command);
}

