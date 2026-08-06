package org.our_place.calendar.usecase;

import lombok.RequiredArgsConstructor;
import org.our_place.calendar.api.events.DayEntryCreatedEvent;
import org.our_place.calendar.domain.exception.DayEntryAlreadyExistsException;
import org.our_place.calendar.persistence.entity.DayEntry;
import org.our_place.calendar.persistence.entity.DayEntryId;
import org.our_place.calendar.persistence.repository.DayEntryRepository;
import org.our_place.calendar.usecase.command.CreateDayEntryCommand;
import org.our_place.calendar.usecase.output.CreateDayEntryOutput;
import org.our_place.shared.application.bus.EventBus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateDayEntryUseCase implements UseCase<CreateDayEntryCommand, CreateDayEntryOutput> {

    private final DayEntryRepository dayEntryRepository;
    private final EventBus eventBus;

    @Override
    @Transactional
    public CreateDayEntryOutput execute(CreateDayEntryCommand command) {
        DayEntryId id = new DayEntryId(command.roomId(), command.entryDate());
        if (dayEntryRepository.existsById(id)) {
            throw new DayEntryAlreadyExistsException();
        }

        DayEntry entry = DayEntry.create(
                command.roomId(), command.entryDate(),
                command.createdByUserId(), command.content(), command.moodEmoji()
        );
        dayEntryRepository.save(entry);

        eventBus.publish(new DayEntryCreatedEvent(command.roomId(), command.entryDate(), command.createdByUserId()));

        return new CreateDayEntryOutput(command.roomId(), command.entryDate());
    }
}