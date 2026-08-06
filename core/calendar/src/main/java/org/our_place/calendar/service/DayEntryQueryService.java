package org.our_place.calendar.service;

import lombok.RequiredArgsConstructor;
import org.our_place.calendar.persistence.entity.DayEntry;
import org.our_place.calendar.persistence.repository.DayEntryRepository;
import org.our_place.calendar.service.dto.DayEntryDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DayEntryQueryService {

    private final DayEntryRepository dayEntryRepository;

    public List<DayEntryDto> listByRoom(UUID roomId) {
        return dayEntryRepository.findByIdRoomIdOrderByIdEntryDateDesc(roomId).stream()
                .map(this::toDto)
                .toList();
    }

    private DayEntryDto toDto(DayEntry e) {
        return new DayEntryDto(e.getId().getRoomId(), e.getId().getEntryDate(), e.getCreatedByUserId(), e.getContent(), e.getMoodEmoji());
    }
}