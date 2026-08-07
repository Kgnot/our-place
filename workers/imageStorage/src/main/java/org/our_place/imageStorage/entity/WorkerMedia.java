package org.our_place.imageStorage.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class WorkerMedia implements Persistable<UUID> {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "room_id", nullable = false)
    private UUID roomId;

    @Column(name = "uploaded_by_user_id", nullable = false)
    private UUID uploadedByUserId;

    @Column(name = "r2_url", length = 500, nullable = false)
    private String r2Url;

    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;

    @Column(name = "media_type_code", length = 30, nullable = false)
    private String mediaTypeCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processing_status_code", referencedColumnName = "code", nullable = false)
    private WorkerLkpProcessingStatus processingStatus;

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

    /**
     * Tipo geography de PostGIS; se mapea como WKT a nivel de entidad.
     * Se setea en create() desde el EXIF que manda el frontend.
     */
    @ColumnTransformer(read = "ST_AsText(location)", write = "ST_GeogFromText(?)")
    @Column(name = "location", columnDefinition = "geography")
    private String location;

    @Column(name = "saved_place_id")
    private UUID savedPlaceId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "exif_raw_payload", columnDefinition = "jsonb")
    private String exifRawPayload;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @Column(name = "purge_at")
    private OffsetDateTime purgeAt;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Transient
    private boolean isNew = false;

    /**
     * Punto único de construcción. Recibe takenAt y coordenadas del EXIF
     * extraído por el frontend (llega vía /confirm). Si no hay EXIF,
     * takenAt=null (se usa createdAt como fallback) y location=null.
     */
    public static WorkerMedia create(
            UUID roomId,
            UUID uploadedByUserId,
            String r2Url,
            String mediaTypeCode,
            WorkerLkpProcessingStatus initialStatus,
            String mimeType,
            Long fileSizeBytes,
            OffsetDateTime takenAt,
            Double latitude,           // ← del EXIF del frontend
            Double longitude,          // ← del EXIF del frontend
            String caption
    ) {
        WorkerMedia media = new WorkerMedia();
        media.id = UUID.randomUUID();
        media.isNew = true;
        media.roomId = roomId;
        media.uploadedByUserId = uploadedByUserId;
        media.r2Url = r2Url;
        media.mediaTypeCode = mediaTypeCode;
        media.processingStatus = initialStatus;
        media.mimeType = mimeType;
        media.fileSizeBytes = fileSizeBytes;
        media.takenAt = takenAt;
        media.caption = caption;
        media.createdAt = OffsetDateTime.now();

        // Location desde EXIF del frontend (si viene)
        if (latitude != null && longitude != null) {
            media.location = "POINT(%s %s)".formatted(longitude, latitude); // PostGIS: lng lat
        }

        return media;
    }

    /**
     * Worker: solo actualiza thumbnail + exifRawPayload.
     * takenAt y location YA están seteados desde create().
     * savedPlaceId lo resuelve otro job async (GeoNames reverse geocode).
     */
    public void markProcessingCompleted(
            String thumbnailUrl,
            String exifRawPayload,
            WorkerLkpProcessingStatus completedStatus
    ) {
        this.thumbnailUrl = thumbnailUrl;
        this.exifRawPayload = exifRawPayload;
        this.processingStatus = completedStatus;
        this.errorMessage = null;
    }

    public void markProcessingFailed(String errorMessage, WorkerLkpProcessingStatus failedStatus) {
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
    public boolean isNew() {
        return isNew;
    }

    @PostPersist
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }
}