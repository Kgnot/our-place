package org.our_place.calendar.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "important_date_media", schema = "calendar")
@Getter
@NoArgsConstructor
public class ImportantDateMedia {

    @EmbeddedId
    private ImportantDateMediaId id;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public static ImportantDateMedia create(UUID importantDateId, UUID mediaId) {
        ImportantDateMedia link = new ImportantDateMedia();
        link.id = new ImportantDateMediaId(importantDateId, mediaId);
        link.createdAt = OffsetDateTime.now();
        return link;
    }
}