package org.our_place.room.infra.persistence.repository;

import org.our_place.room.domain.entity.MemberRelationship;
import org.our_place.room.domain.entity.MemberRelationshipId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRelationshipRepository extends JpaRepository<MemberRelationship, MemberRelationshipId> {
}