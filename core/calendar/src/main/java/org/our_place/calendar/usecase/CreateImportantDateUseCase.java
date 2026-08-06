package org.our_place.calendar.usecase;

import lombok.RequiredArgsConstructor;
import org.our_place.calendar.api.events.ImportantDateCreatedEvent;
import org.our_place.calendar.domain.exception.ImportantDateTypeNotFoundException;
import org.our_place.calendar.persistence.entity.ImportantDate;
import org.our_place.calendar.persistence.entity.LkpImportantDateType;
import org.our_place.calendar.persistence.repository.ImportantDateRepository;
import org.our_place.calendar.persistence.repository.LkpImportantDateTypeRepository;
import org.our_place.calendar.usecase.command.CreateImportantDateCommand;
import org.our_place.calendar.usecase.output.CreateImportantDateOutput;
import org.our_place.shared.application.bus.EventBus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateImportantDateUseCase implements UseCase<CreateImportantDateCommand, CreateImportantDateOutput> {

    private final ImportantDateRepository importantDateRepository;
    private final LkpImportantDateTypeRepository lkpImportantDateTypeRepository;
    private final EventBus eventBus;

    @Override
    @Transactional
    public CreateImportantDateOutput execute(CreateImportantDateCommand command) {
        LkpImportantDateType type = lkpImportantDateTypeRepository.findById(command.typeCode())
                .orElseThrow(() -> new ImportantDateTypeNotFoundException(command.typeCode()));

        ImportantDate date = ImportantDate.create(
                command.roomId(), type, command.title(), command.eventDate(),
                command.isRecurring(), command.notifyDaysBefore(), command.createdByUserId()
        );
        importantDateRepository.save(date);

        eventBus.publish(new ImportantDateCreatedEvent(date.getId(), command.roomId(), type.getCode(), command.eventDate()));

        return new CreateImportantDateOutput(date.getId());
    }
}