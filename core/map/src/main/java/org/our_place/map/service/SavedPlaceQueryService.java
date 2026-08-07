package org.our_place.map.service;


import lombok.RequiredArgsConstructor;
import org.our_place.map.service.dto.SavedPlaceDto;
import org.our_place.map.service.mapper.MapMapper;
import org.our_place.map.domain.exception.SavedPlaceNotFoundException;
import org.our_place.map.persistence.entity.SavedPlace;
import org.our_place.map.persistence.repository.SavedPlaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SavedPlaceQueryService {

    private final SavedPlaceRepository savedPlaceRepository;

    public SavedPlaceDto findByIdAndRoom(UUID savedPlaceId, UUID roomId) {
        SavedPlace place = savedPlaceRepository.findByIdAndRoomId(savedPlaceId, roomId)
                .orElseThrow(() -> new SavedPlaceNotFoundException(savedPlaceId,roomId));
        return MapMapper.toDto(place);
    }

    public List<SavedPlaceDto> findByRoom(UUID roomId) {
        return savedPlaceRepository.findByRoomIdOrderByCreatedAtDesc(roomId).stream()
                .map(MapMapper::toDto)
                .toList();
    }
}