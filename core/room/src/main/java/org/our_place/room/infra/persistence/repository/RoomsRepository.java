package org.our_place.room.infra.persistence.repository;

import org.our_place.room.domain.entity.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface RoomsRepository extends JpaRepository<Rooms, UUID> {
    @Query("""
                SELECT r FROM Rooms r
                JOIN RoomMember m ON m.room = r
                WHERE m.id.userLoginId = :userId
                  AND m.status = :status
                  AND LOWER(r.name) LIKE LOWER(CONCAT('%', :query, '%'))
                ORDER BY r.name ASC
            """)
    List<Rooms> searchByUserAndQuery(
            @Param("userId") UUID userId,
            @Param("status") String status,
            @Param("query") String query
    );

    @Query("""
                SELECT r FROM Rooms r
                JOIN RoomMember m ON m.room = r
                WHERE m.id.userLoginId = :userId
                  AND LOWER(r.name) LIKE LOWER(CONCAT('%', :query, '%'))
                ORDER BY r.name ASC
            """)
    List<Rooms> searchByUserAndQueryAllStatuses(
            @Param("userId") UUID userId,
            @Param("query") String query
    );
}