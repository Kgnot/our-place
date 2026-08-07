package org.our_place.imageStorage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lkp_processing_status", schema = "gallery")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkerLkpProcessingStatus {

    /** CODE natural: pending, processing, completed, failed. */
    @Id
    @Column(name = "code", length = 30, nullable = false)
    private String code;

    @Column(name = "name", length = 100, nullable = false)
    private String name;
}
