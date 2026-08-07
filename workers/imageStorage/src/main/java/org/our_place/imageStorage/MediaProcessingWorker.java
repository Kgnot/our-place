package org.our_place.imageStorage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.our_place.gallery.api.events.MediaUploadedEvent;
import org.our_place.imageStorage.entity.WorkerLkpProcessingStatus;
import org.our_place.imageStorage.entity.WorkerMedia;
import org.our_place.imageStorage.entity.vo.ProcessingStatus;
import org.our_place.imageStorage.repository.WorkerLkpProcessingStatusRepository;
import org.our_place.imageStorage.repository.WorkerMediaRepository;
import org.our_place.imageStorage.utils.ExifData;
import org.our_place.imageStorage.utils.ExifExtractor;
import org.our_place.imageStorage.utils.R2StorageService;
import org.our_place.imageStorage.utils.ThumbnailGenerator;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class MediaProcessingWorker {

    private final WorkerMediaRepository workerMediaRepository;
    private final R2StorageService r2StorageService;
    private final ThumbnailGenerator thumbnailGenerator;
    private final ExifExtractor exifExtractor;
    private final WorkerLkpProcessingStatusRepository processingStatusRepository;

    @Async("mediaProcessingExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onMediaUploaded(MediaUploadedEvent event) {
        log.info("Processing media {} from R2 key {}", event.mediaId(), event.r2Key());

        try {
            // 1. Descarga original de R2
            byte[] originalBytes = r2StorageService.download(event.r2Key());

            // 2. Genera thumbnail
            byte[] thumbnailBytes = thumbnailGenerator.generate(originalBytes, event.mimeType());
            String thumbnailKey = event.r2Key().replace("/media/", "/thumbnails/");
            r2StorageService.upload(thumbnailKey, thumbnailBytes, "image/jpeg");

            // 3. Extrae EXIF
            var exif = exifExtractor.extract(originalBytes, event.mimeType());

            // 4. Busca status COMPLETED
            WorkerLkpProcessingStatus completed = processingStatusRepository
                    .findById(ProcessingStatus.COMPLETED.code())
                    .orElseThrow(() -> new IllegalStateException("lkp_processing_status seed missing: COMPLETED"));

            // 5. Location WKT (PostGIS geography) — null si no tiene GPS
            String locationWkt = null;
            if (exif.hasLocation()) {
                locationWkt = "POINT(%s %s)".formatted(exif.longitude(), exif.latitude());
            }

            // 6. EXIF raw como JSON
            String exifJson = buildExifJson(exif);

            // 7. Actualiza entidad
            WorkerMedia workerMedia = workerMediaRepository.findById(event.mediaId()).orElseThrow();
            workerMedia.markProcessingCompleted(thumbnailKey, exifJson, locationWkt, null, completed);
            //                                                 exifJson ↑    WKT ↑   savedPlaceId=null (lo resuelve otro job)

            log.info("Media {} processed successfully", event.mediaId());

        } catch (Exception e) {
            log.error("Failed to process media {}", event.mediaId(), e);

            WorkerLkpProcessingStatus failed = processingStatusRepository
                    .findById(ProcessingStatus.FAILED.code())
                    .orElseThrow(() -> new IllegalStateException("lkp_processing_status seed missing: FAILED"));

            WorkerMedia workerMedia = workerMediaRepository.findById(event.mediaId()).orElseThrow();
            workerMedia.markProcessingFailed(e.getMessage(), failed);
        }
    }

    private String buildExifJson(ExifData exif) {
        if (exif.takenAt() == null && !exif.hasLocation()) return null;

        StringBuilder sb = new StringBuilder("{");
        boolean first = true;

        if (exif.takenAt() != null) {
            sb.append("\"takenAt\":\"").append(exif.takenAt()).append("\"");
            first = false;
        }
        if (exif.latitude() != null) {
            if (!first) sb.append(",");
            sb.append("\"latitude\":").append(exif.latitude());
        }
        if (exif.longitude() != null) {
            sb.append(",\"longitude\":").append(exif.longitude());
        }
        sb.append("}");

        return sb.toString();
    }
}