package org.our_place.gallery.infra.persistence.repository;


import org.our_place.gallery.domain.entity.MediaReaction;
import org.our_place.gallery.domain.entity.MediaReactionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MediaReactionRepository extends JpaRepository<MediaReaction, MediaReactionId> {

    Optional<MediaReaction> findByIdMediaIdAndIdUserLoginId(UUID mediaId, UUID userLoginId);

    List<MediaReaction> findByIdMediaId(UUID mediaId);

    long countByIdMediaId(UUID mediaId);

    void deleteByIdMediaIdAndIdUserLoginId(UUID mediaId, UUID userLoginId);
}