package org.our_place.calendar.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

/** content nullable: la entrada puede ser solo fotos vinculadas, solo texto, o ambos. */
@Entity
@Table(name = "day_entry", schema = "calendar")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DayEntry {

    @EmbeddedId
    private DayEntryId id;

    /** Sin FK real: referencia lógica cross-schema a identity.users_login.id. */
    @Column(name = "created_by_user_id")
    private UUID createdByUserId;

    @Column(name = "content", columnDefinition = "text")
    private String content;

    @Column(name = "mood_emoji", length = 10)
    private String moodEmoji;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}
