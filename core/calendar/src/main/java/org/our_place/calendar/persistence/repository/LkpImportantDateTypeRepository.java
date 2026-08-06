package org.our_place.calendar.persistence.repository;

import org.our_place.calendar.persistence.entity.LkpImportantDateType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LkpImportantDateTypeRepository extends JpaRepository<LkpImportantDateType, String> {
}