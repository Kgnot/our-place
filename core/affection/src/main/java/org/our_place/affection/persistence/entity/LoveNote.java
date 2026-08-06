package org.our_place.affection.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "love_note", schema = "affection")
@Getter
@NoArgsConstructor
public class LoveNote implements Persistable<UUID> {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "room_id", nullable = false)
    private UUID roomId;

    @Column(name = "author_user_id", nullable = false)
    private UUID authorUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_code", referencedColumnName = "code", nullable = false)
    private LkpNoteType type;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Transient
    private boolean isNew = false;

    public static LoveNote create(UUID roomId, UUID authorUserId, LkpNoteType type, String content) {
        LoveNote note = new LoveNote();
        note.id = UUID.randomUUID();
        note.isNew = true;
        note.roomId = roomId;
        note.authorUserId = authorUserId;
        note.type = type;
        note.content = content;
        note.createdAt = OffsetDateTime.now();
        return note;
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