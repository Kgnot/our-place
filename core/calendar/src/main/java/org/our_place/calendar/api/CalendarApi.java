package org.our_place.calendar.api;

import org.our_place.calendar.service.dto.ImportantDateDto;
import org.our_place.calendar.usecase.command.CreateImportantDateCommand;
import org.our_place.calendar.usecase.output.CreateImportantDateOutput;
import org.our_place.common.shared.SharedApi;

import java.util.List;
import java.util.UUID;

@SharedApi(description = "API interna del módulo calendar, consumible in-process por otros módulos")
public interface CalendarApi {
    CreateImportantDateOutput createImportantDate(CreateImportantDateCommand command);
    List<ImportantDateDto> listImportantDates(UUID roomId);
}