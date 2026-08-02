package org.our_place.notification.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "notification",
    schema = "notification",
    indexes = @Index(name = "idx_notification_recipient_feed", columnList = "recipient_user_id, is_read, created_at")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    /** BIGSERIAL: alto volumen de eventos, orden de inserción define el feed, nunca se comparte por URL propia. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** Sin FK real: referencia lógica cross-schema a room.rooms.id. */
    @Column(name = "room_id", nullable = false)
    private UUID roomId;

    /** Sin FK real: referencia lógica cross-schema a identity.users_login.id. */
    @Column(name = "recipient_user_id", nullable = false)
    private UUID recipientUserId;

    /** Sin FK real: referencia lógica cross-schema a identity.users_login.id. */
    @Column(name = "actor_user_id")
    private UUID actorUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_code", referencedColumnName = "code", nullable = false)
    private LkpNotificationType type;

    @Column(name = "entity_type", length = 30)
    private String entityType;

    /** Referencia POLIMÓRFICA sin FK: entity_type indica la tabla, entity_id su identificador (uuid o "room_id:entry_date" según el caso). */
    @Column(name = "entity_id", length = 64)
    private String entityId;

    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}
