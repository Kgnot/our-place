package org.our_place.gallery.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "media_comment",
    schema = "gallery",
    indexes = @Index(name = "idx_media_comment_media_created", columnList = "media_id, created_at")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MediaComment {

    /** BIGSERIAL: alto volumen, orden de inserción importa, no se expone por URL propia. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_id", nullable = false)
    private Media media;

    /** Sin FK real: referencia lógica cross-schema a identity.users_login.id. */
    @Column(name = "user_login_id", nullable = false)
    private UUID userLoginId;

    @Column(name = "content", nullable = false, columnDefinition = "text")
    private String content;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;
}
