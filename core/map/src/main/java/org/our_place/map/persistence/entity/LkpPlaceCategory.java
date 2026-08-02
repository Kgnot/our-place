package org.our_place.map.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lkp_place_category", schema = "map")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LkpPlaceCategory {

    /** CODE natural: restaurant, park, hotel, first_date. */
    @Id
    @Column(name = "code", length = 30, nullable = false)
    private String code;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "icon_url", length = 255)
    private String iconUrl;
}
