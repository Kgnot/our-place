package org.our_place.affection.service;

import lombok.RequiredArgsConstructor;
import org.our_place.affection.persistence.entity.LoveNote;
import org.our_place.affection.persistence.repository.LoveNoteRepository;
import org.our_place.affection.service.dto.LoveNoteDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoveNoteQueryService {

    private final LoveNoteRepository loveNoteRepository;

    public List<LoveNoteDto> listByRoom(UUID roomId) {
        return loveNoteRepository.findByRoomIdOrderByCreatedAtDesc(roomId).stream()
                .map(this::toDto)
                .toList();
    }

    private LoveNoteDto toDto(LoveNote note) {
        return new LoveNoteDto(
                note.getId(), note.getAuthorUserId(),
                note.getType().getCode(), note.getType().getName(),
                note.getContent(), note.getCreatedAt()
        );
    }
}