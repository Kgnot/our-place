package org.our_place.affection.persistence.repository;

import org.our_place.affection.persistence.entity.LkpNoteType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LkpNoteTypeRepository extends JpaRepository<LkpNoteType, String> {
    boolean existsByCode(String code);
}