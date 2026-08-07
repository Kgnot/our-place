package org.our_place.calendar.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ImportantDateMediaId implements Serializable {

    @Column(name = "important_date_id")
    private UUID importantDateId;

    @Column(name = "media_id")
    private UUID mediaId;
}