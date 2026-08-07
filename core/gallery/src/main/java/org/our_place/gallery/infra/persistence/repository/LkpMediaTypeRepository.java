package org.our_place.gallery.infra.persistence.repository;

import org.our_place.gallery.domain.entity.LkpMediaType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LkpMediaTypeRepository extends JpaRepository<LkpMediaType, String> {
}