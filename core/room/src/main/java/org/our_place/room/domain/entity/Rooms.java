package org.our_place.room.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;


@Entity
@Table(name = "rooms", schema = "room")
@Getter
@Setter
@NoArgsConstructor
public class Rooms implements Persistable<UUID> {

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

    @Transient
    private boolean isNew = false;

    public static Rooms create(String name, LkpRoomStatus status, LkpRelationshipType relationshipType,
                               UUID ownerUserId, LocalDate anniversaryDate, String timezone) {
        Rooms room = new Rooms();
        room.id = UUID.randomUUID();
        room.isNew = true;
        room.name = name;
        room.status = status;
        room.relationshipType = relationshipType;
        room.ownerUserId = ownerUserId;
        room.anniversaryDate = anniversaryDate;
        if (timezone != null && !timezone.isBlank()) {
            room.timezone = timezone;
        }
        room.createdAt = OffsetDateTime.now();
        return room;
    }

    public void changeStatus(LkpRoomStatus newStatus) {
        this.status = newStatus;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostPersist
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }
}