package org.our_place.calendar.service;

import lombok.RequiredArgsConstructor;
import org.our_place.calendar.persistence.entity.ImportantDate;
import org.our_place.calendar.persistence.repository.ImportantDateRepository;
import org.our_place.calendar.service.dto.ImportantDateDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImportantDateQueryService {

    private final ImportantDateRepository importantDateRepository;

    public List<ImportantDateDto> listByRoom(UUID roomId) {
        return importantDateRepository.findByRoomIdOrderByEventDateAsc(roomId).stream()
                .map(this::toDto)
                .toList();
    }

    private ImportantDateDto toDto(ImportantDate d) {
        return new ImportantDateDto(d.getId(), d.getType().getCode(), d.getType().getName(), d.getTitle(), d.getEventDate(), d.isRecurring());
    }
}