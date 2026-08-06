package org.our_place.pet.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lkp_species", schema = "pet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LkpSpecies {

    @Id
    @Column(name = "code", length = 20, nullable = false)
    private String code;

    @Column(name = "name", length = 50, nullable = false)
    private String name;
}