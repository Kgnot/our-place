package org.our_place.calendar.usecase;

import lombok.RequiredArgsConstructor;
import org.our_place.calendar.api.events.ImportantDateCreatedEvent;
import org.our_place.calendar.api.external.GalleryExternalApi;
import org.our_place.calendar.domain.exception.ImportantDateTypeNotFoundException;
import org.our_place.calendar.persistence.entity.ImportantDate;
import org.our_place.calendar.persistence.entity.ImportantDateMedia;
import org.our_place.calendar.persistence.entity.LkpImportantDateType;
import org.our_place.calendar.persistence.repository.ImportantDateMediaRepository;
import org.our_place.calendar.persistence.repository.ImportantDateRepository;
import org.our_place.calendar.persistence.repository.LkpImportantDateTypeRepository;
import org.our_place.calendar.usecase.command.CreateImportantDateWithMediaCommand;
import org.our_place.calendar.usecase.output.CreateImportantDateOutput;
import org.our_place.gallery.api.dto_shared.UploadMediaCommandShared;
import org.our_place.gallery.api.dto_shared.UploadMediaOutputShared;
import org.our_place.gallery.domain.vo.MediaType;
import org.our_place.shared.application.bus.EventBus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateImportantDateWithMediaUseCase
        implements UseCase<CreateImportantDateWithMediaCommand, CreateImportantDateOutput> {

    private final ImportantDateRepository importantDateRepository;
    private final ImportantDateMediaRepository importantDateMediaRepository;
    private final LkpImportantDateTypeRepository lkpImportantDateTypeRepository;
    private final GalleryExternalApi uploadMediaUseCase;
    private final EventBus eventBus;

    @Override
    @Transactional
    public CreateImportantDateOutput execute(CreateImportantDateWithMediaCommand command) {
        LkpImportantDateType type = lkpImportantDateTypeRepository.findById(command.typeCode())
                .orElseThrow(() -> new ImportantDateTypeNotFoundException(command.typeCode()));

        ImportantDate date = ImportantDate.create(
                command.roomId(), type, command.title(), command.eventDate(),
                command.isRecurring(), command.notifyDaysBefore(), command.createdByUserId()
        );
        importantDateRepository.save(date);

        // Registrar fotos en Gallery + linkear
        if (command.mediaItems() != null) {
            for (var item : command.mediaItems()) {
                // llamamos al gallery api
                UploadMediaOutputShared uploadOutput = uploadMediaUseCase.uploadMedia(new UploadMediaCommandShared(
                        command.roomId(), command.createdByUserId(),
                        item.r2Key(), MediaType.fromValue(item.mediaTypeCode()).code(),
                        item.mimeType(), item.fileSizeBytes(), item.takenAt(),
                        item.latitude(), item.longitude(), item.caption()
                ));
                importantDateMediaRepository.save(
                        ImportantDateMedia.create(date.getId(), uploadOutput.mediaId())
                );
            }
        }
        // Publicar evento de ImportantDate
        eventBus.publish(new ImportantDateCreatedEvent(
                date.getId(), command.roomId(), type.getCode(), command.eventDate()
        ));

        return new CreateImportantDateOutput(date.getId());
    }
}