package org.our_place.calendar.controller.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record LinkMediaToDayEntryRequest(@NotNull UUID mediaId) {}