package org.our_place.gallery.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "media",
    schema = "gallery",
    indexes = {
        @Index(name = "idx_media_room_taken_at", columnList = "room_id, taken_at"),
        @Index(name = "idx_media_room_processing_status", columnList = "room_id, processing_status_code"),
        @Index(name = "idx_media_room_deleted_at", columnList = "room_id, deleted_at")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Media {

    /** UUID: contenido íntimo/privado, evita enumeración entre salas. */
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    /** Sin FK real: referencia lógica cross-schema a room.rooms.id. */
    @Column(name = "room_id", nullable = false)
    private UUID roomId;

    /** Sin FK real: referencia lógica cross-schema a identity.users_login.id. */
    @Column(name = "uploaded_by_user_id", nullable = false)
    private UUID uploadedByUserId;

    @Column(name = "r2_url", length = 500, nullable = false)
    private String r2Url;

    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_type_code", referencedColumnName = "code", nullable = false)
    private LkpMediaType mediaType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processing_status_code", referencedColumnName = "code", nullable = false)
    private LkpProcessingStatus processingStatus;

    @Column(name = "retry_count", nullable = false)
    private short retryCount = 0;

    @Column(name = "error_message", columnDefinition = "text")
    private String errorMessage;

    @Column(name = "file_size_bytes")
    private Long fileSizeBytes;

    @Column(name = "mime_type", length = 100)
    private String mimeType;

    @Column(name = "caption", columnDefinition = "text")
    private String caption;

    @Column(name = "taken_at")
    private OffsetDateTime takenAt;

    /** Tipo geography de PostGIS; se mapea como texto (WKT) a nivel de entidad. */
    @Column(name = "location", columnDefinition = "geography")
    private String location;

    /** Sin FK real: referencia lógica cross-schema a map.saved_place.id. Nullable: se completa async vía job de EXIF. */
    @Column(name = "saved_place_id")
    private UUID savedPlaceId;

    @Column(name = "exif_raw_payload", columnDefinition = "jsonb")
    private String exifRawPayload;

    /** Papelera (soft delete). */
    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    /** Borrado definitivo, gestionado por job periódico. */
    @Column(name = "purge_at")
    private OffsetDateTime purgeAt;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}
