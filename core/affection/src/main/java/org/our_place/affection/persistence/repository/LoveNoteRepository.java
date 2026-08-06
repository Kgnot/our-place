package org.our_place.affection.persistence.repository;

import org.our_place.affection.persistence.entity.LoveNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LoveNoteRepository extends JpaRepository<LoveNote, UUID> {
    List<LoveNote> findByRoomIdOrderByCreatedAtDesc(UUID roomId);
}