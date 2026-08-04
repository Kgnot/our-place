package org.our_place.identity.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lkp_contact_type", schema = "identity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LkpContactType {

    /** CODE natural: email, phone, whatsapp. */
    @Id
    @Column(name = "code", length = 20, nullable = false)
    private String code;

    @Column(name = "name", length = 50, nullable = false)
    private String name;
}
