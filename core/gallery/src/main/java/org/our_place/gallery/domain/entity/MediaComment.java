package org.our_place.gallery.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "media_comment", schema = "gallery")
@Getter
@NoArgsConstructor
public class MediaComment implements Persistable<UUID> {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "media_id", nullable = false)
    private UUID mediaId;

    @Column(name = "user_login_id", nullable = false)
    private UUID userLoginId;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @Transient
    private boolean isNew = false;

    public static MediaComment create(UUID mediaId, UUID userLoginId, String content) {
        MediaComment comment = new MediaComment();
        comment.id = UUID.randomUUID();
        comment.isNew = true;
        comment.mediaId = mediaId;
        comment.userLoginId = userLoginId;
        comment.content = content;
        comment.createdAt = OffsetDateTime.now();
        return comment;
    }

    public void softDelete() {
        this.deletedAt = OffsetDateTime.now();
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