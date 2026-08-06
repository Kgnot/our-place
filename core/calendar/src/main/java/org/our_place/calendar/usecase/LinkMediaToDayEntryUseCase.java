package org.our_place.calendar.usecase;

import lombok.RequiredArgsConstructor;
import org.our_place.calendar.api.events.MediaLinkedToDayEntryEvent;
import org.our_place.calendar.domain.exception.DayEntryNotFoundException;
import org.our_place.calendar.persistence.entity.DayEntryId;
import org.our_place.calendar.persistence.entity.DayEntryMedia;
import org.our_place.calendar.persistence.repository.DayEntryMediaRepository;
import org.our_place.calendar.persistence.repository.DayEntryRepository;
import org.our_place.calendar.usecase.command.LinkMediaToDayEntryCommand;
import org.our_place.calendar.usecase.output.LinkMediaToDayEntryOutput;
import org.our_place.shared.application.bus.EventBus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class LinkMediaToDayEntryUseCase implements UseCase<LinkMediaToDayEntryCommand, LinkMediaToDayEntryOutput> {

    private final DayEntryRepository dayEntryRepository;
    private final DayEntryMediaRepository dayEntryMediaRepository;
    private final EventBus eventBus;

    @Override
    @Transactional
    public LinkMediaToDayEntryOutput execute(LinkMediaToDayEntryCommand command) {
        DayEntryId dayEntryId = new DayEntryId(command.roomId(), command.entryDate());
        if (!dayEntryRepository.existsById(dayEntryId)) {
            throw new DayEntryNotFoundException();
        }

        DayEntryMedia link = DayEntryMedia.create(command.roomId(), command.entryDate(), command.mediaId());
        dayEntryMediaRepository.save(link);

        eventBus.publish(new MediaLinkedToDayEntryEvent(command.roomId(), command.entryDate(), command.mediaId()));

        return new LinkMediaToDayEntryOutput(true);
    }
}