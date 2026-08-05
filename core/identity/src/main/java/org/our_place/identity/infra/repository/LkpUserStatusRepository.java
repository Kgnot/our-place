package org.our_place.identity.infra.repository;

import org.our_place.identity.domain.entity.LkpUserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LkpUserStatusRepository extends JpaRepository<LkpUserStatus, String> {
    boolean existsByCode(String code);
}