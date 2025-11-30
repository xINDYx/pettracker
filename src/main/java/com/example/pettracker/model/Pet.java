package com.example.pettracker.model;

import com.example.pettracker.dto.request.PetUpdateRequest;
import com.example.pettracker.enums.PetType;
import com.example.pettracker.enums.TrackerType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
public abstract class Pet {

    @Id
    private UUID id = UUID.randomUUID();

    @Enumerated(EnumType.STRING)
    private PetType petType;

    @Enumerated(EnumType.STRING)
    private TrackerType trackerType;

    private Integer ownerId;

    private Boolean inZone;

    protected Pet(PetType petType,
                  TrackerType trackerType,
                  Integer ownerId,
                  Boolean inZone) {
        this.petType = petType;
        this.trackerType = trackerType;
        this.ownerId = ownerId;
        this.inZone = inZone;
    }

    public void updateStatus(PetUpdateRequest petUpdateRequest) {
        if (petUpdateRequest.inZone() != null) {
            this.inZone = petUpdateRequest.inZone();
        }
    }
}