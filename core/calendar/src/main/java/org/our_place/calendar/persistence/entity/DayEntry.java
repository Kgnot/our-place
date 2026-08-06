package org.our_place.calendar.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "day_entry", schema = "calendar")
@Getter
@NoArgsConstructor
public class DayEntry {

    @EmbeddedId
    private DayEntryId id;

    @Column(name = "created_by_user_id")
    private UUID createdByUserId;

    @Column(name = "content", columnDefinition = "text")
    private String content;

    @Column(name = "mood_emoji", length = 10)
    private String moodEmoji;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public static DayEntry create(UUID roomId, java.time.LocalDate entryDate,
                                  UUID createdByUserId, String content, String moodEmoji) {
        DayEntry entry = new DayEntry();
        entry.id = new DayEntryId(roomId, entryDate);
        entry.createdByUserId = createdByUserId;
        entry.content = content;
        entry.moodEmoji = moodEmoji;
        entry.createdAt = OffsetDateTime.now();
        return entry;
    }

    public void updateContent(String content, String moodEmoji) {
        this.content = content;
        this.moodEmoji = moodEmoji;
    }
}