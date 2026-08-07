package org.our_place.calendar.service;

import lombok.RequiredArgsConstructor;
import org.our_place.calendar.persistence.entity.LkpImportantDateType;
import org.our_place.calendar.persistence.repository.LkpImportantDateTypeRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportantDateLKPService {

    private final LkpImportantDateTypeRepository lkpImportantDateTypeRepository;


    // no me interesa hacer un dto porque no es información "importante"
    @Cacheable("lkp-important-date-types")
    public List<LkpImportantDateType> listLkp() {
        return lkpImportantDateTypeRepository.findAll();
    }
}
