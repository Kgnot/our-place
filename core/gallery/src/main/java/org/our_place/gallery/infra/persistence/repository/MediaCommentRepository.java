package org.our_place.gallery.infra.persistence.repository;

import org.our_place.gallery.domain.entity.MediaComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MediaCommentRepository extends JpaRepository<MediaComment, UUID> {

    Optional<MediaComment> findByIdAndDeletedAtIsNull(UUID id);

    List<MediaComment> findByMediaIdAndDeletedAtIsNullOrderByCreatedAtAsc(UUID mediaId);

    long countByMediaIdAndDeletedAtIsNull(UUID mediaId);
}