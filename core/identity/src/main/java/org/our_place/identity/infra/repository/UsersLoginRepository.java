package org.our_place.identity.infra.repository;

import org.our_place.identity.domain.entity.UsersLogin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsersLoginRepository extends JpaRepository<UsersLogin, UUID> {
    UsersLogin findByEmail(String email);

    boolean existsByEmail(String email);

    UsersLogin findByProviderUserIdAndAuthProvider(String providerUserId, String authProvider);
}