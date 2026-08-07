package org.our_place.gallery.application.usecase;

import lombok.RequiredArgsConstructor;
import org.our_place.gallery.api.events.MediaUploadedEvent;
import org.our_place.gallery.application.usecase.command.UploadMediaCommand;
import org.our_place.gallery.application.usecase.output.UploadMediaOutput;
import org.our_place.gallery.domain.entity.LkpMediaType;
import org.our_place.gallery.domain.entity.LkpProcessingStatus;
import org.our_place.gallery.domain.entity.Media;
import org.our_place.gallery.domain.vo.ProcessingStatus;
import org.our_place.gallery.infra.persistence.repository.LkpMediaTypeRepository;
import org.our_place.gallery.infra.persistence.repository.LkpProcessingStatusRepository;
import org.our_place.gallery.infra.persistence.repository.MediaRepository;
import org.our_place.shared.application.bus.EventBus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class UploadMediaUseCase implements UseCase<UploadMediaCommand, UploadMediaOutput> {

    private final MediaRepository mediaRepository;
    private final LkpMediaTypeRepository mediaTypeRepository;
    private final LkpProcessingStatusRepository processingStatusRepository;
    private final EventBus eventPublisher;

    @Override
    public UploadMediaOutput execute(UploadMediaCommand command) {
        LkpMediaType mediaType = mediaTypeRepository.findById(command.mediaTypeCode())
                .orElseThrow(() -> new IllegalArgumentException("unknown media type: " + command.mediaTypeCode()));

        LkpProcessingStatus pending = processingStatusRepository.findById(ProcessingStatus.PENDING.code())
                .orElseThrow(() -> new IllegalStateException(
                        "lkp_processing_status seed missing: " + ProcessingStatus.PENDING.code()));

        Media media = Media.create(
                command.roomId(), command.uploadedByUserId(), command.r2Url(), mediaType, pending,
                command.mimeType(), command.fileSizeBytes(), command.takenAt(), command.latitude(),
                command.longitude(), command.caption()
        );
        mediaRepository.save(media);
        eventPublisher.publish(new MediaUploadedEvent(
                media.getId(), media.getRoomId(), media.getR2Url(),
                media.getMediaType().getCode(), media.getMimeType()
        ));

        return new UploadMediaOutput(media.getId());
    }
}