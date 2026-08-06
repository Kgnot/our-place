package org.our_place.calendar.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DayEntryMediaId implements Serializable {

    @Column(name = "room_id")
    private UUID roomId;

    @Column(name = "entry_date")
    private LocalDate entryDate;

    @Column(name = "media_id")
    private UUID mediaId;
}