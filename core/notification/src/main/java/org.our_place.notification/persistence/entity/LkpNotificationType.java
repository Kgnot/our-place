package org.our_place.notification.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lkp_notification_type", schema = "notification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LkpNotificationType {

    /** CODE natural: media_uploaded, media_comment_added, media_reaction_added, day_entry_added, place_added, member_joined. */
    @Id
    @Column(name = "code", length = 40, nullable = false)
    private String code;

    @Column(name = "name", length = 100, nullable = false)
    private String name;
}
