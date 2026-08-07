package org.our_place.map.persistence.repository;

import org.our_place.map.persistence.entity.LocationPing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LocationPingRepository extends JpaRepository<LocationPing, Long> {

    /** Último ping conocido de un usuario dentro de una sala. */
    Optional<LocationPing> findFirstByRoomIdAndUserLoginIdOrderByRecordedAtDesc(UUID roomId, UUID userLoginId);

    /** Último ping de cada miembro de la sala se resuelve en el QueryService agrupando este resultado. */
    List<LocationPing> findByRoomIdOrderByRecordedAtDesc(UUID roomId);
}