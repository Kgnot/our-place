package org.our_place.calendar.persistence.repository;

import org.our_place.calendar.persistence.entity.ImportantDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ImportantDateRepository extends JpaRepository<ImportantDate, UUID> {
    List<ImportantDate> findByRoomIdOrderByEventDateAsc(UUID roomId);
    List<ImportantDate> findByIsRecurringTrueAndEventDateBetween(LocalDate from, LocalDate to);
}