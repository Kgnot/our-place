package org.our_place.room.infra.persistence.repository;

import org.our_place.room.domain.entity.RoomInvitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomInvitationRepository extends JpaRepository<RoomInvitation, Long> {

    Optional<RoomInvitation> findByToken(String token);
}