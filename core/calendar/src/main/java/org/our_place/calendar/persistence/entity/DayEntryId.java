package org.our_place.calendar.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/** COMPUESTA (llave de negocio natural): una sala tiene como máximo una entrada por fecha. */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DayEntryId implements Serializable {

    /** Sin FK real: referencia lógica cross-schema a room.rooms.id. */
    @Column(name = "room_id")
    private UUID roomId;

    @Column(name = "entry_date")
    private LocalDate entryDate;
}
