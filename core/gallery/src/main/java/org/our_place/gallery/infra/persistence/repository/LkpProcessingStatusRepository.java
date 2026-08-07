package org.our_place.gallery.infra.persistence.repository;

import org.our_place.gallery.domain.entity.LkpProcessingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LkpProcessingStatusRepository extends JpaRepository<LkpProcessingStatus, String> {
}