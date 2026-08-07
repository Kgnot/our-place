package org.our_place.gallery.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "media_reaction", schema = "gallery")
@Getter
@NoArgsConstructor
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

    public static MediaReaction create(Media media, java.util.UUID userLoginId, String reactionType) {
        MediaReaction reaction = new MediaReaction();
        reaction.id = new MediaReactionId(media.getId(), userLoginId);
        reaction.media = media;
        reaction.reactionType = (reactionType != null) ? reactionType : "heart";
        reaction.createdAt = OffsetDateTime.now();
        return reaction;
    }

    public void updateReactionType(String reactionType) {
        this.reactionType = reactionType;
    }
}