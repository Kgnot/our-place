
package org.our_place.map.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.our_place.identity.api.SecurityContextApi;
import org.our_place.map.controller.request.RecordLocationPingRequest;
import org.our_place.map.controller.response.LocationPingResponse;
import org.our_place.map.service.LocationPingQueryService;
import org.our_place.map.usecase.RecordLocationPingUseCase;
import org.our_place.map.usecase.command.RecordLocationPingCommand;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * SOLO capa HTTP. El endpoint de creación se pensó para escritura de altísima
 * frecuencia desde la app móvil (ver comentario en LocationPing) — sin
 * validaciones pesadas ni eventos de dominio.
 */
@RestController
@RequestMapping("api/v1/map/room/{roomId}/location-pings")
@RequiredArgsConstructor
public class LocationPingController {

    private final RecordLocationPingUseCase recordLocationPingUseCase;
    private final LocationPingQueryService locationPingQueryService;
    private final SecurityContextApi securityContextApi;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void record(@PathVariable UUID roomId, @Valid @RequestBody RecordLocationPingRequest request) {
        recordLocationPingUseCase.execute(new RecordLocationPingCommand(
                roomId,
                securityContextApi.getCurrentUserId(),
                request.locationWkt(),
                request.batteryLevel()
        ));
    }

    /**
     * Último ping conocido de cada miembro de la sala, para pintar el mapa en vivo.
     */
    @GetMapping("/latest")
    public List<LocationPingResponse> latestPerUser(@PathVariable UUID roomId) {
        return locationPingQueryService.findLatestPerUser(roomId).stream()
                .map(LocationPingResponse::from)
                .toList();
    }
}
