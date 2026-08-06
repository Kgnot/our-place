package org.our_place.calendar.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lkp_important_date_type", schema = "calendar")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LkpImportantDateType {

    @Id
    @Column(name = "code", length = 30, nullable = false)
    private String code;

    @Column(name = "name", length = 100, nullable = false)
    private String name;
}