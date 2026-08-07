package org.our_place.gallery.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lkp_processing_status", schema = "gallery")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LkpProcessingStatus {

    /** CODE natural: pending, processing, completed, failed. */
    @Id
    @Column(name = "code", length = 30, nullable = false)
    private String code;

    @Column(name = "name", length = 100, nullable = false)
    private String name;
}
