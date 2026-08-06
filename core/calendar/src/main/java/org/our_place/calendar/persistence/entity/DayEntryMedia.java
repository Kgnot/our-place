package org.our_place.calendar.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "day_entry_media", schema = "calendar")
@Getter
@NoArgsConstructor
public class DayEntryMedia {

    @EmbeddedId
    private DayEntryMediaId id;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public static DayEntryMedia create(UUID roomId, LocalDate entryDate, UUID mediaId) {
        DayEntryMedia link = new DayEntryMedia();
        link.id = new DayEntryMediaId(roomId, entryDate, mediaId);
        link.createdAt = OffsetDateTime.now();
        return link;
    }
}