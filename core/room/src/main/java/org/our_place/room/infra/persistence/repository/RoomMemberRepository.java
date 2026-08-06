package org.our_place.room.infra.persistence.repository;

import org.our_place.room.domain.entity.RoomMember;
import org.our_place.room.domain.entity.RoomMemberId;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomMemberRepository extends JpaRepository<RoomMember, RoomMemberId> {

    List<RoomMember> findByIdRoomId(UUID roomId);

    Optional<RoomMember> findByIdRoomIdAndIdUserLoginId(UUID roomId, UUID userLoginId);

    boolean existsByIdRoomIdAndIdUserLoginId(UUID roomId, UUID userLoginId);

    long countByIdRoomIdAndStatus(UUID roomId, String status);

    @EntityGraph(value = "RoomMember.withRoom")
    List<RoomMember> findByIdUserLoginId(UUID userLoginId);

    @EntityGraph(value = "RoomMember.withRoom")
    List<RoomMember> findByIdUserLoginIdAndStatus(UUID userLoginId, String status);

}