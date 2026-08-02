package org.our_place.gallery.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lkp_media_type", schema = "gallery")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LkpMediaType {

    /** CODE natural: image, video. */
    @Id
    @Column(name = "code", length = 20, nullable = false)
    private String code;

    @Column(name = "name", length = 50, nullable = false)
    private String name;
}
