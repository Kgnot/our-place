package org.our_place.notification.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;

/**
 * COMPUESTA: la fila ES "estos dos miembros de esta sala tienen esta relación".
 * Solo relevante al escalar a N miembros; para pareja (2 personas) usar
 * Rooms.relationshipType / Rooms.anniversaryDate y dejar esta tabla vacía.
 */
@Entity
@Table(name = "member_relationship", schema = "room")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRelationship {

    @EmbeddedId
    private MemberRelationshipId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roomId")
    @JoinColumn(name = "room_id")
    private Rooms room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "relationship_type_code", referencedColumnName = "code", nullable = false)
    private LkpRelationshipType relationshipType;

    @Column(name = "since_date")
    private LocalDate sinceDate;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}
