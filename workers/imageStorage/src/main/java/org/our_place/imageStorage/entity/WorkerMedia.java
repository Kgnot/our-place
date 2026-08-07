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

    /**
     * UUID: contenido íntimo/privado, evita enumeración entre salas.
     */
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    /**
     * Sin FK real: referencia lógica cross-schema a room.rooms.id.
     */
    @Column(name = "room_id", nullable = false)
    private UUID roomId;

    /**
     * Sin FK real: referencia lógica cross-schema a identity.users_login.id.
     */
    @Column(name = "uploaded_by_user_id", nullable = false)
    private UUID uploadedByUserId;

    @Column(name = "r2_url", length = 500, nullable = false)
    private String r2Url;

    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;

    @Column(name = "media_type_code", length = 30, nullable = false)
    private String media_type_code;

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
     * Tipo geography de PostGIS; se mapea como texto (WKT) a nivel de entidad.
     */
    @ColumnTransformer(read = "ST_AsText(location)", write = "ST_GeogFromText(?)")
    @Column(name = "location", columnDefinition = "geography")
    private String location; // TODO, cambiar esto a Point, hace que sea mas trabajo pero es necesario, recuerda hacerlo en ambas entidades, esta y la "original" que esta en gallery

    /**
     * Sin FK real: referencia lógica cross-schema a map.saved_place.id. Nullable: se completa async vía job de EXIF.
     */
    @Column(name = "saved_place_id")
    private UUID savedPlaceId;

    /**
     * Payload crudo de EXIF, en formato JSON.
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "exif_raw_payload", columnDefinition = "jsonb")
    private String exifRawPayload;

    /**
     * Papelera (soft delete).
     */
    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    /**
     * Borrado definitivo, gestionado por job periódico.
     */
    @Column(name = "purge_at")
    private OffsetDateTime purgeAt;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Transient
    private boolean isNew = false;

    public static WorkerMedia create(UUID roomId, UUID uploadedByUserId, String r2Url,
                                     String mediaType, WorkerLkpProcessingStatus initialStatus,
                                     String mimeType, Long fileSizeBytes, OffsetDateTime takenAt, String caption) {
        WorkerMedia workerMedia = new WorkerMedia();
        workerMedia.id = UUID.randomUUID();
        workerMedia.isNew = true;
        workerMedia.roomId = roomId;
        workerMedia.uploadedByUserId = uploadedByUserId;
        workerMedia.r2Url = r2Url;
        workerMedia.media_type_code = mediaType;
        workerMedia.processingStatus = initialStatus;
        workerMedia.mimeType = mimeType;
        workerMedia.fileSizeBytes = fileSizeBytes;
        workerMedia.takenAt = takenAt;
        workerMedia.caption = caption;
        workerMedia.createdAt = OffsetDateTime.now();
        return workerMedia;
    }

    /**
     * Llamado por el job de procesamiento async (thumbnail + EXIF), no por el usuario final.
     */
    public void markProcessingCompleted(String thumbnailUrl, String exifRawPayload,
                                        String location, UUID savedPlaceId, WorkerLkpProcessingStatus completedStatus) {
        this.thumbnailUrl = thumbnailUrl;
        this.exifRawPayload = exifRawPayload;
        this.location = location;
        this.savedPlaceId = savedPlaceId;
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