package org.our_place.map.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.our_place.identity.api.SecurityContextApi;
import org.our_place.map.controller.request.CreateSavedPlaceRequest;
import org.our_place.map.controller.response.SavedPlaceResponse;
import org.our_place.map.service.SavedPlaceQueryService;
import org.our_place.map.usecase.CreateSavedPlaceUseCase;
import org.our_place.map.usecase.DeleteSavedPlaceUseCase;
import org.our_place.map.usecase.command.CreateSavedPlaceCommand;

import org.our_place.map.usecase.command.DeleteSavedPlaceCommand;
import org.our_place.map.usecase.outout.CreateSavedPlaceOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/map/room/{roomId}/saved-places")
@RequiredArgsConstructor
public class SavedPlaceController {

    private final CreateSavedPlaceUseCase createSavedPlaceUseCase;
    private final DeleteSavedPlaceUseCase deleteSavedPlaceUseCase;
    private final SavedPlaceQueryService savedPlaceQueryService;
    private final SecurityContextApi securityContextApi;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SavedPlaceResponse create(@PathVariable UUID roomId, @Valid @RequestBody CreateSavedPlaceRequest request) {
        CreateSavedPlaceOutput output = createSavedPlaceUseCase.execute(new CreateSavedPlaceCommand(
                roomId,
                securityContextApi.getCurrentUserId(),
                request.categoryCode(),
                request.name(),
                request.description(),
                request.locationWkt(),
                false,
                null,
                request.visitedAt()
        ));

        return SavedPlaceResponse.from(savedPlaceQueryService.findByIdAndRoom(output.id(), roomId));
    }

    @GetMapping
    public List<SavedPlaceResponse> list(@PathVariable UUID roomId) {
        return savedPlaceQueryService.findByRoom(roomId).stream()
                .map(SavedPlaceResponse::from)
                .toList();
    }

    @GetMapping("/{savedPlaceId}")
    public SavedPlaceResponse get(@PathVariable UUID roomId, @PathVariable UUID savedPlaceId) {
        return SavedPlaceResponse.from(savedPlaceQueryService.findByIdAndRoom(savedPlaceId, roomId));
    }

    @DeleteMapping("/{savedPlaceId}")
    public ResponseEntity<Void> delete(@PathVariable UUID roomId, @PathVariable UUID savedPlaceId) {
        deleteSavedPlaceUseCase.execute(new DeleteSavedPlaceCommand(roomId, savedPlaceId));
        return ResponseEntity.ok().build();
    }
}