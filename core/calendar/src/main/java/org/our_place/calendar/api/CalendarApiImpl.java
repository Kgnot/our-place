package org.our_place.calendar.api;

import lombok.RequiredArgsConstructor;
import org.our_place.calendar.service.ImportantDateQueryService;
import org.our_place.calendar.service.dto.ImportantDateDto;
import org.our_place.calendar.usecase.CreateImportantDateUseCase;
import org.our_place.calendar.usecase.command.CreateImportantDateCommand;
import org.our_place.calendar.usecase.output.CreateImportantDateOutput;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CalendarApiImpl implements CalendarApi {

    private final CreateImportantDateUseCase createImportantDateUseCase;
    private final ImportantDateQueryService importantDateQueryService;

    @Override
    public CreateImportantDateOutput createImportantDate(CreateImportantDateCommand command) {
        return createImportantDateUseCase.execute(command);
    }

    @Override
    public List<ImportantDateDto> listImportantDates(UUID roomId) {
        return importantDateQueryService.listByRoom(roomId);
    }
}