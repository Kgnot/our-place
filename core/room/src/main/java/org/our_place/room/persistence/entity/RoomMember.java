package org.our_place.room.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "room_member", schema = "room")
@Getter
@NoArgsConstructor
public class RoomMember {

    @EmbeddedId
    private RoomMemberId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roomId")
    @JoinColumn(name = "room_id")
    private Rooms room;

    @Column(name = "role_code", nullable = false, length = 30)
    private String roleCode;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "active";

    @Column(name = "invited_by_user_id")
    private UUID invitedByUserId;

    @Column(name = "nickname", length = 50)
    private String nickname;

    @Column(name = "joined_at", nullable = false)
    private OffsetDateTime joinedAt;

    public static RoomMember join(Rooms room, UUID userLoginId, String roleCode, UUID invitedByUserId) {
        RoomMember member = new RoomMember();
        member.id = new RoomMemberId(room.getId(), userLoginId);
        member.room = room;
        member.roleCode = roleCode;
        member.invitedByUserId = invitedByUserId;
        member.joinedAt = OffsetDateTime.now();
        return member;
    }

    public void updateNickname(String nickname) { this.nickname = nickname; }

    public void leave() { this.status = "left"; }
}