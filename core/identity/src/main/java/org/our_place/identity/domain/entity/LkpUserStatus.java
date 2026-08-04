package org.our_place.identity.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lkp_user_status", schema = "identity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LkpUserStatus {

    /** CODE natural: active, pending_verification, disabled, locked. */
    @Id
    @Column(name = "code", length = 30, nullable = false)
    private String code;

    @Column(name = "name", length = 100, nullable = false)
    private String name;
}
