package org.our_place.calendar.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DayEntryMediaId implements Serializable {

    /** Junto con entryDate, FK compuesta real hacia calendar.day_entry (room_id, entry_date). */
    @Column(name = "room_id")
    private UUID roomId;

    @Column(name = "entry_date")
    private LocalDate entryDate;

    /** Sin FK real: referencia lógica cross-schema a gallery.media.id. */
    @Column(name = "media_id")
    private UUID mediaId;
}
