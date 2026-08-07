package org.our_place.map.usecase;

import lombok.RequiredArgsConstructor;
import org.our_place.map.domain.exception.SavedPlaceNotFoundException;
import org.our_place.map.persistence.entity.SavedPlace;
import org.our_place.map.persistence.repository.SavedPlaceRepository;
import org.our_place.map.usecase.command.DeleteSavedPlaceCommand;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DeleteSavedPlaceUseCase implements UseCase<DeleteSavedPlaceCommand, Void> {

    private final SavedPlaceRepository savedPlaceRepository;

    @Override
    @Transactional
    public Void execute(DeleteSavedPlaceCommand command) {
        SavedPlace place = savedPlaceRepository.findByIdAndRoomId(
                        command.savedPlaceId(), command.roomId())
                .orElseThrow(() -> new SavedPlaceNotFoundException(command.savedPlaceId(), command.roomId()));

        savedPlaceRepository.delete(place);
        return null;
    }
}