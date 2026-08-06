package org.our_place.calendar.persistence.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "important_date", schema = "calendar")
@Getter
@NoArgsConstructor
public class ImportantDate implements Persistable<UUID> {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "room_id", nullable = false)
    private UUID roomId; // SIN FK cross-schema -> room.rooms.id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_code", referencedColumnName = "code", nullable = false)
    @JsonManagedReference
    private LkpImportantDateType type;

    @Column(name = "title", length = 150, nullable = false)
    private String title;

    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    @Column(name = "is_recurring", nullable = false)
    private boolean isRecurring = true;

    @Column(name = "notify_days_before", nullable = false)
    private short notifyDaysBefore = 0;

    @Column(name = "created_by_user_id", nullable = false)
    private UUID createdByUserId;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Transient
    private boolean isNew = false;

    public static ImportantDate create(UUID roomId, LkpImportantDateType type, String title,
                                       LocalDate eventDate, boolean isRecurring,
                                       short notifyDaysBefore, UUID createdByUserId) {
        ImportantDate d = new ImportantDate();
        d.id = UUID.randomUUID();
        d.isNew = true;
        d.roomId = roomId;
        d.type = type;
        d.title = title;
        d.eventDate = eventDate;
        d.isRecurring = isRecurring;
        d.notifyDaysBefore = notifyDaysBefore;
        d.createdByUserId = createdByUserId;
        d.createdAt = OffsetDateTime.now();
        return d;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostPersist
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }
}