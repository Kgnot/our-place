package org.our_place.room.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lkp_room_status", schema = "room")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LkpRoomStatus {

    /** CODE natural: active, trial, suspended. */
    @Id
    @Column(name = "code", length = 30, nullable = false)
    private String code;

    @Column(name = "name", length = 100, nullable = false)
    private String name;
}
