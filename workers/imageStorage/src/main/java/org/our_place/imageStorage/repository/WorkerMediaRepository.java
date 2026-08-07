package org.our_place.imageStorage.repository;

import org.our_place.imageStorage.entity.WorkerMedia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkerMediaRepository extends JpaRepository<WorkerMedia, UUID> {

    Optional<WorkerMedia> findByIdAndDeletedAtIsNull(UUID id);

    Page<WorkerMedia> findByRoomIdAndDeletedAtIsNullOrderByTakenAtDescCreatedAtDesc(UUID roomId, Pageable pageable);

    /** Usado por MediaApi para resolver thumbnails de una lista de mediaId (ej. desde `calendar`). */
    List<WorkerMedia> findByIdInAndDeletedAtIsNull(List<UUID> ids);
}