package org.our_place.identity.persistence.repository;

import org.our_place.identity.domain.entity.LkpContactType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LkpContactTypeRepository extends JpaRepository<LkpContactType, String> {
    boolean existsByCode(String code);
}