package org.our_place.identity.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "user_contact", schema = "identity")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserContact {

    @EmbeddedId
    private UserContactId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userLoginId")
    @JoinColumn(name = "user_login_id")
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("contactTypeCode")
    @JoinColumn(name = "contact_type_code", referencedColumnName = "code")
    private LkpContactType contactType;

    @Column(name = "is_primary", nullable = false)
    private boolean isPrimary = false;

    @Column(name = "is_verified", nullable = false)
    private boolean isVerified = false;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    // --- Comportamiento del value object ---

    public void verify() {
        this.isVerified = true;
    }

    public void markAsPrimary() {
        this.isPrimary = true;
    }

    public void unmarkAsPrimary() {
        this.isPrimary = false;
    }
}