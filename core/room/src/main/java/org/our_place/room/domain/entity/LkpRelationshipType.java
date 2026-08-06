package org.our_place.room.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lkp_relationship_type", schema = "room")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LkpRelationshipType {

    /** CODE natural: partner, family, friend. */
    @Id
    @Column(name = "code", length = 40, nullable = false)
    private String code;

    @Column(name = "name", length = 100, nullable = false)
    private String name;
}
