package org.our_place.map.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "location_ping",
    schema = "map",
    indexes = {
        @Index(name = "idx_location_ping_room_recorded", columnList = "room_id, recorded_at"),
        @Index(name = "idx_location_ping_user_recorded", columnList = "user_login_id, recorded_at")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationPing {

    /** BIGSERIAL: escritura de altísima frecuencia (ping continuo desde la app móvil), ventana de retención 24h. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** Sin FK real: referencia lógica cross-schema a identity.users_login.id. */
    @Column(name = "user_login_id", nullable = false)
    private UUID userLoginId;

    /** Sin FK real: referencia lógica cross-schema a room.rooms.id. */
    @Column(name = "room_id", nullable = false)
    private UUID roomId;

    /** Tipo geography de PostGIS; se mapea como texto (WKT) a nivel de entidad. */
    @ColumnTransformer(read = "ST_AsText(location)", write = "ST_GeogFromText(?)")
    @Column(name = "location", columnDefinition = "geography")
    private String location;

    @Column(name = "battery_level")
    private Short batteryLevel;

    @Column(name = "recorded_at", nullable = false)
    private OffsetDateTime recordedAt;
}
