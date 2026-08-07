package org.our_place.map.usecase;

import lombok.RequiredArgsConstructor;
import org.our_place.map.persistence.entity.LocationPing;
import org.our_place.map.persistence.repository.LocationPingRepository;
import org.our_place.map.usecase.command.RecordLocationPingCommand;
import org.our_place.map.usecase.outout.RecordLocationPingOutput;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RecordLocationPingUseCase implements UseCase<RecordLocationPingCommand, RecordLocationPingOutput> {

    private final LocationPingRepository locationPingRepository;

    @Override
    @Transactional
    public RecordLocationPingOutput execute(RecordLocationPingCommand command) {
        LocationPing ping = new LocationPing();
        ping.setUserLoginId(command.userLoginId());
        ping.setRoomId(command.roomId());
        ping.setLocation(command.locationWkt());
        ping.setBatteryLevel(command.batteryLevel());
        ping.setRecordedAt(java.time.OffsetDateTime.now());

        LocationPing saved = locationPingRepository.save(ping);
        return new RecordLocationPingOutput(saved.getId());
    }
}