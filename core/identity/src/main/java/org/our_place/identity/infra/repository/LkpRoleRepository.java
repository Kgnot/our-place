package org.our_place.identity.infra.repository;

import org.our_place.identity.domain.entity.LkpRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LkpRoleRepository extends JpaRepository<LkpRole, String> {
    boolean existsByCode(String code);
}