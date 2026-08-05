package org.our_place.identity.infra.repository;

import org.our_place.identity.domain.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProfileRepository extends JpaRepository<Profile, UUID> {
    Profile findByUsersLogin_Email(String email);
}