package org.our_place.imageStorage.repository;

import org.our_place.imageStorage.entity.WorkerLkpProcessingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkerLkpProcessingStatusRepository extends JpaRepository<WorkerLkpProcessingStatus, String> {
}