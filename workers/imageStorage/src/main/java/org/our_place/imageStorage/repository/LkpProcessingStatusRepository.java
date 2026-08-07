package org.our_place.imageStorage.repository;

import org.our_place.imageStorage.entity.LkpProcessingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LkpProcessingStatusRepository extends JpaRepository<LkpProcessingStatus, String> {
}