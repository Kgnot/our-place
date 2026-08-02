package org.our_place.gallery.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

/** COMPUESTA: la fila ES "este usuario reaccionó a esta foto" (1 reacción por usuario por foto). */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MediaReactionId implements Serializable {

    @Column(name = "media_id")
    private UUID mediaId;

    /** Sin FK real: referencia lógica cross-schema a identity.users_login.id. */
    @Column(name = "user_login_id")
    private UUID userLoginId;
}
