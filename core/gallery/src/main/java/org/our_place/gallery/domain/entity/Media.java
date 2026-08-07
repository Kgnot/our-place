package org.our_place.gallery.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.domain.Persistable;

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
public class Media implements Persistable<UUID> {

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
    @ColumnTransformer(read = "ST_AsText(location)", write = "ST_GeogFromText(?)")
    @Column(name = "location", columnDefinition = "geography")
    private String location;

    /** Sin FK real: referencia lógica cross-schema a map.saved_place.id. Nullable: se completa async vía job de EXIF. */
    @Column(name = "saved_place_id")
    private UUID savedPlaceId;

    @JdbcTypeCode(SqlTypes.JSON)
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

    @Transient
    private boolean isNew = false;

    public static Media create(UUID roomId, UUID uploadedByUserId, String r2Url,
                               LkpMediaType mediaType, LkpProcessingStatus initialStatus,
                               String mimeType, Long fileSizeBytes,
                               OffsetDateTime takenAt,
                               Double latitude, Double longitude,
                               String caption) {
        Media media = new Media();
        media.id = UUID.randomUUID();
        media.isNew = true;
        media.roomId = roomId;
        media.uploadedByUserId = uploadedByUserId;
        media.r2Url = r2Url;
        media.mediaType = mediaType;
        media.processingStatus = initialStatus;
        media.mimeType = mimeType;
        media.fileSizeBytes = fileSizeBytes;
        media.takenAt = takenAt;
        media.caption = caption;
        media.createdAt = OffsetDateTime.now();

        // Location desde EXIF (si viene)
        if (latitude != null && longitude != null) {
            media.location = "POINT(%s %s)".formatted(longitude, latitude);  // PostGIS: lng lat
        }

        return media;
    }

    /** Llamado por el job de procesamiento async (thumbnail + EXIF), no por el usuario final. */
    public void markProcessingCompleted(String thumbnailUrl, String exifRawPayload,
                                        String location, UUID savedPlaceId, LkpProcessingStatus completedStatus) {
        this.thumbnailUrl = thumbnailUrl;
        this.exifRawPayload = exifRawPayload;
        this.location = location;
        this.savedPlaceId = savedPlaceId;
        this.processingStatus = completedStatus;
        this.errorMessage = null;
    }

    public void markProcessingFailed(String errorMessage, LkpProcessingStatus failedStatus) {
        this.errorMessage = errorMessage;
        this.processingStatus = failedStatus;
        this.retryCount = (short) (this.retryCount + 1);
    }

    public void updateCaption(String caption) {
        this.caption = caption;
    }

    public void softDelete() {
        this.deletedAt = OffsetDateTime.now();
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }

    @Override
    public boolean isNew() { return isNew; }

    @PostPersist
    @PostLoad
    void markNotNew() { this.isNew = false; }
}