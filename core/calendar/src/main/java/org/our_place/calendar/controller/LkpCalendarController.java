package org.our_place.calendar.controller;

import lombok.RequiredArgsConstructor;
import org.our_place.calendar.persistence.entity.LkpImportantDateType;
import org.our_place.calendar.service.ImportantDateLKPService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/calendar")
@RequiredArgsConstructor
public class LkpCalendarController {

    private final ImportantDateLKPService service;


    @GetMapping("/important-dates/lkp")
    public ResponseEntity<List<LkpImportantDateType>> listLkp() {
        return ResponseEntity.ok(service.listLkp());
    }

}
