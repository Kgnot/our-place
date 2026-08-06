package org.our_place.affection.persistence.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lkp_note_type", schema = "affection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LkpNoteType {

    @Id
    @Column(name = "code", length = 20, nullable = false)
    private String code;

    @Column(name = "name", length = 50, nullable = false)
    private String name;
}