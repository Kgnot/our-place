package org.our_place.calendar.persistence.repository;

import org.our_place.calendar.persistence.entity.ImportantDateMedia;
import org.our_place.calendar.persistence.entity.ImportantDateMediaId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ImportantDateMediaRepository extends JpaRepository<ImportantDateMedia, ImportantDateMediaId> {

    List<ImportantDateMedia> findByIdImportantDateId(UUID importantDateId);

    List<ImportantDateMedia> findByIdImportantDateIdIn(List<UUID> importantDateIds);


    long countByIdImportantDateId(UUID importantDateId);
}