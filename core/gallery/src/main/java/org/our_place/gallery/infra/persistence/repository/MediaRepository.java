package org.our_place.gallery.infra.persistence.repository;

import org.our_place.gallery.domain.entity.Media;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MediaRepository extends JpaRepository<Media, UUID> {

    Optional<Media> findByIdAndDeletedAtIsNull(UUID id);

    Page<Media> findByRoomIdAndDeletedAtIsNullOrderByTakenAtDescCreatedAtDesc(UUID roomId, Pageable pageable);

    /** Usado por MediaApi para resolver thumbnails de una lista de mediaId (ej. desde `calendar`). */
    List<Media> findByIdInAndDeletedAtIsNull(List<UUID> ids);
}