package org.our_place.calendar.persistence.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import java.util.List;
import lombok.*;

@Entity
@Table(name = "lkp_important_date_type", schema = "calendar")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LkpImportantDateType {
    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "type")
    @JsonBackReference
    private List<ImportantDate> importantDates;
}
