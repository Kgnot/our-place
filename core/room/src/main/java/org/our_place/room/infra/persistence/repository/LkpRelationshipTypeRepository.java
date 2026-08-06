package org.our_place.room.infra.persistence.repository;

import org.our_place.room.domain.entity.LkpRelationshipType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LkpRelationshipTypeRepository extends JpaRepository<LkpRelationshipType, String> {
}