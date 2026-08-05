package org.our_place.identity.infra.repository;

import org.our_place.identity.domain.entity.UserRole;
import org.our_place.identity.domain.entity.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
    List<UserRole> findByUsersLogin_Id(UUID userLoginId);
}