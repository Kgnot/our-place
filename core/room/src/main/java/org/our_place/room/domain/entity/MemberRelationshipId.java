package org.our_place.room.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MemberRelationshipId implements Serializable {

    @Column(name = "room_id")
    private UUID roomId;

    /** Sin FK formal: referencia lógica a (room_id, user_login_id) de room.room_member (PK compuesta). */
    @Column(name = "member_a_user_id")
    private UUID memberAUserId;

    /** Mismo caso que memberAUserId. */
    @Column(name = "member_b_user_id")
    private UUID memberBUserId;
}
