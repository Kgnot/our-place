package org.our_place.room.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "member_relationship", schema = "room")
@Getter
@NoArgsConstructor
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

    public static MemberRelationship create(Rooms room, java.util.UUID memberAUserId, java.util.UUID memberBUserId,
                                            LkpRelationshipType type, LocalDate sinceDate) {
        MemberRelationship rel = new MemberRelationship();
        rel.id = new MemberRelationshipId(room.getId(), memberAUserId, memberBUserId);
        rel.room = room;
        rel.relationshipType = type;
        rel.sinceDate = sinceDate;
        rel.createdAt = OffsetDateTime.now();
        return rel;
    }
}