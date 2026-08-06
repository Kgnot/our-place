package org.our_place.calendar.persistence.repository;

import org.our_place.calendar.persistence.entity.DayEntry;
import org.our_place.calendar.persistence.entity.DayEntryId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface DayEntryRepository extends JpaRepository<DayEntry, DayEntryId> {
    List<DayEntry> findByIdRoomIdOrderByIdEntryDateDesc(UUID roomId);
    List<DayEntry> findByIdRoomIdAndIdEntryDateBetween(UUID roomId, LocalDate from, LocalDate to);
}