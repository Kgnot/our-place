package org.our_place.map.service;


import lombok.RequiredArgsConstructor;
import org.our_place.map.service.dto.LocationPingDto;
import org.our_place.map.service.mapper.MapMapper;
import org.our_place.map.persistence.entity.LocationPing;
import org.our_place.map.persistence.repository.LocationPingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LocationPingQueryService {

    private final LocationPingRepository locationPingRepository;

    /**
     * Último ping de cada miembro de la sala (para pintar el mapa en vivo).
     */
    public List<LocationPingDto> findLatestPerUser(UUID roomId) {
        List<LocationPing> pings = locationPingRepository.findByRoomIdOrderByRecordedAtDesc(roomId);

        // Agrupar por usuario, tomar solo el más reciente de cada uno
        Map<UUID, LocationPing> latestByUser = new LinkedHashMap<>();
        for (LocationPing ping : pings) {
            latestByUser.putIfAbsent(ping.getUserLoginId(), ping);
        }

        return latestByUser.values().stream()
                .map(MapMapper::toDto)
                .toList();
    }

    /**
     * Último ping de un usuario específico.
     */
    public Optional<LocationPingDto> findLatestByUser(UUID roomId, UUID userLoginId) {
        return locationPingRepository
                .findFirstByRoomIdAndUserLoginIdOrderByRecordedAtDesc(roomId, userLoginId)
                .map(MapMapper::toDto);
    }
}