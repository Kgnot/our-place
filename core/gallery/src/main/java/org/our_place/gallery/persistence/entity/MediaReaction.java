package org.our_place.gallery.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "media_reaction", schema = "gallery")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MediaReaction {

    @EmbeddedId
    private MediaReactionId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("mediaId")
    @JoinColumn(name = "media_id")
    private Media media;

    @Column(name = "reaction_type", length = 20, nullable = false)
    private String reactionType = "heart";

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}
