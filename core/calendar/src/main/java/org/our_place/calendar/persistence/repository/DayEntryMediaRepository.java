package org.our_place.calendar.persistence.repository;

import org.our_place.calendar.persistence.entity.DayEntryMedia;
import org.our_place.calendar.persistence.entity.DayEntryMediaId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface DayEntryMediaRepository extends JpaRepository<DayEntryMedia, DayEntryMediaId> {
    List<DayEntryMedia> findByIdRoomIdAndIdEntryDate(UUID roomId, LocalDate entryDate);
}