package org.our_place.room.persistence.entity;

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
@NoArgsConstructor
public class RoomInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Rooms room;

    @Column(name = "invited_email", length = 255, nullable = false)
    private String invitedEmail;

    @Column(name = "invited_by_user_id", nullable = false)
    private UUID invitedByUserId;

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

    public static RoomInvitation create(Rooms room, String invitedEmail, UUID invitedByUserId, String roleCode) {
        RoomInvitation inv = new RoomInvitation();
        inv.room = room;
        inv.invitedEmail = invitedEmail;
        inv.invitedByUserId = invitedByUserId;
        inv.roleCode = roleCode;
        inv.token = UUID.randomUUID().toString();
        inv.expiresAt = OffsetDateTime.now().plusDays(7);
        inv.createdAt = OffsetDateTime.now();
        return inv;
    }

    public void accept() {
        this.status = "accepted";
        this.acceptedAt = OffsetDateTime.now();
    }

    public boolean isExpired() {
        return OffsetDateTime.now().isAfter(this.expiresAt);
    }
}