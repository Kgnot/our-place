package org.our_place.room.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

/** COMPUESTA: la fila ES "este usuario pertenece a esta sala". */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RoomMemberId implements Serializable {

    @Column(name = "room_id")
    private UUID roomId;

    /** Sin FK real: referencia lógica cross-schema a identity.users_login.id. */
    @Column(name = "user_login_id")
    private UUID userLoginId;
}
