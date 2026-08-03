package org.our_place.identity.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

/** COMPUESTA: la fila ES "este usuario tiene este valor de contacto de este tipo". */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserContactId implements Serializable {

    @Column(name = "user_login_id")
    private UUID userLoginId;

    @Column(name = "contact_type_code", length = 20)
    private String contactTypeCode;

    @Column(name = "value", length = 255)
    private String value;
}
