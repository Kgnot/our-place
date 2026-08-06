package org.our_place.room.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "rooms", schema = "room")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rooms {

    /** UUID: id de tenant, referenciado desde varios schemas. */
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", length = 150, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_code", referencedColumnName = "code", nullable = false)
    private LkpRoomStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "relationship_type_code", referencedColumnName = "code")
    private LkpRelationshipType relationshipType;

    /** Sin FK real: referencia lógica cross-schema a identity.users_login.id. */
    @Column(name = "owner_user_id", nullable = false)
    private UUID ownerUserId;

    @Column(name = "anniversary_date")
    private LocalDate anniversaryDate;

    @Column(name = "timezone", length = 50)
    private String timezone = "America/Bogota";

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}
