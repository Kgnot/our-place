package org.our_place.notification.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "room_invitation",
    schema = "room",
    indexes = @Index(name = "idx_room_invitation_lookup", columnList = "room_id, invited_email, status")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomInvitation {

    /** BIGSERIAL: id interno de sistema; el identificador público real es `token`. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Rooms room;

    @Column(name = "invited_email", length = 255, nullable = false)
    private String invitedEmail;

    /** Sin FK real: referencia lógica cross-schema a identity.users_login.id. */
    @Column(name = "invited_by_user_id", nullable = false)
    private UUID invitedByUserId;

    /** Sin FK real: referencia lógica cross-schema a identity.lkp_role.code. */
    @Column(name = "role_code", length = 30, nullable = false)
    private String roleCode;

    @Column(name = "token", length = 255, nullable = false, unique = true)
    private String token;

    @Column(name = "status", length = 20, nullable = false)
    private String status = "pending";

    @Column(name = "expires_at", nullable = false)
    private OffsetDateTime expiresAt;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "accepted_at")
    private OffsetDateTime acceptedAt;
}
