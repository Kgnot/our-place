package org.our_place.pet.persistence.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "pets", schema = "pet")
@Getter
@NoArgsConstructor
public class Pet implements Persistable<UUID> {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "room_id", nullable = false)
    private UUID roomId; // SIN FK cross-schema -> room.rooms.id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "species_code", referencedColumnName = "code", nullable = false)
    private LkpSpecies species;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "breed", length = 100)
    private String breed;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Column(name = "created_by_user_id", nullable = false)
    private UUID createdByUserId; // SIN FK cross-schema -> identity.users_login.id

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Transient
    private boolean isNew = false;

    public static Pet create(UUID roomId, LkpSpecies species, String name, String breed,
                             LocalDate birthDate, UUID createdByUserId) {
        Pet pet = new Pet();
        pet.id = UUID.randomUUID();
        pet.isNew = true;
        pet.roomId = roomId;
        pet.species = species;
        pet.name = name;
        pet.breed = breed;
        pet.birthDate = birthDate;
        pet.createdByUserId = createdByUserId;
        pet.createdAt = OffsetDateTime.now();
        return pet;
    }

    public void updateAvatar(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostPersist
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }
}