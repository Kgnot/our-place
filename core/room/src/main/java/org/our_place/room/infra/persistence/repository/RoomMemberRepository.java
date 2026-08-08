package org.our_place.room.infra.persistence.repository;

import org.our_place.room.domain.entity.RoomMember;
import org.our_place.room.domain.entity.RoomMemberId;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("""
                SELECT m FROM RoomMember m
                JOIN FETCH m.room r
                WHERE m.id.userLoginId = :userId
                  AND m.status = :status
                  AND LOWER(r.name) LIKE LOWER(CONCAT('%', :query, '%'))
                ORDER BY r.name ASC
            """)
    List<RoomMember> searchByUserAndQuery(
            @Param("userId") UUID userId,
            @Param("status") String status,
            @Param("query") String query
    );

    @Query("""
                SELECT m FROM RoomMember m
                JOIN FETCH m.room r
                WHERE m.id.userLoginId = :userId
                  AND LOWER(r.name) LIKE LOWER(CONCAT('%', :query, '%'))
                ORDER BY r.name ASC
            """)
    List<RoomMember> searchByUserAndQueryAllStatuses(
            @Param("userId") UUID userId,
            @Param("query") String query
    );
}