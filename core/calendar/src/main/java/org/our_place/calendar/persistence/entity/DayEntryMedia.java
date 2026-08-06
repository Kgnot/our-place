package org.our_place.calendar.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

/**
 * COMPUESTA: puente N:N puro entre calendar.day_entry y gallery.media.
 * (room_id, entry_date) es FK compuesta real hacia calendar.day_entry, definida
 * a nivel de base de datos; no se modela como @ManyToOne aquí porque el id
 * embebido combina esa pareja con un tercer campo (media_id) de otro schema.
 */
@Entity
@Table(name = "day_entry_media", schema = "calendar")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DayEntryMedia {

    @EmbeddedId
    private DayEntryMediaId id;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}
