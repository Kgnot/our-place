package org.our_place.gallery.infra.persistence.repository;

import org.our_place.gallery.domain.entity.Media;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MediaRepository extends JpaRepository<Media, UUID> {

    Optional<Media> findByIdAndDeletedAtIsNull(UUID id);

    Page<Media> findByRoomIdAndDeletedAtIsNullOrderByTakenAtDescCreatedAtDesc(UUID roomId, Pageable pageable);

    /**
     * Usado por MediaApi para resolver thumbnails de una lista de mediaId (ej. desde `calendar`).
     */
    List<Media> findByIdInAndDeletedAtIsNull(List<UUID> ids);

    /**
     * Usado por MediaApi para buscar medias por ID de habitación y rango de fechas.
     */
    List<Media> findByRoomIdAndTakenAtBetweenAndDeletedAtIsNullOrderByTakenAtDesc(UUID roomId, OffsetDateTime start, OffsetDateTime end);

    /**
     * Fotos de un room en un rango de fechas (paginado).
     */
    Page<Media> findByRoomIdAndTakenAtBetweenAndDeletedAtIsNullOrderByTakenAtDesc(
            UUID roomId, OffsetDateTime start, OffsetDateTime end, Pageable pageable);

    /**
     * Últimas N fotos de un room (por createdAt).
     */
    Page<Media> findByRoomIdAndDeletedAtIsNullOrderByCreatedAtDesc(UUID roomId, Pageable pageable);
}