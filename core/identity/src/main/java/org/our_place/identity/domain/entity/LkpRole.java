package org.our_place.identity.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lkp_role", schema = "identity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LkpRole {

    /** CODE natural: room_owner, room_member, room_guest. */
    @Id
    @Column(name = "code", length = 30, nullable = false)
    private String code;

    @Column(name = "name", length = 100, nullable = false)
    private String name;
}
