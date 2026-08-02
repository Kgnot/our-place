package org.our_place.notification.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "room_member", schema = "room")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomMember {

    @EmbeddedId
    private RoomMemberId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roomId")
    @JoinColumn(name = "room_id")
    private Rooms room;

    /** Sin FK real: referencia lógica cross-schema a identity.lkp_role.code. */
    @Column(name = "role_code", length = 30, nullable = false)
    private String roleCode;

    @Column(name = "status", length = 20, nullable = false)
    private String status = "active";

    /** Sin FK real: referencia lógica cross-schema a identity.users_login.id. */
    @Column(name = "invited_by_user_id")
    private UUID invitedByUserId;

    @Column(name = "nickname", length = 50)
    private String nickname;

    @Column(name = "joined_at", nullable = false)
    private OffsetDateTime joinedAt;
}
