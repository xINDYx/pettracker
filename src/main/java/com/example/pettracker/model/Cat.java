package com.example.pettracker.model;

import com.example.pettracker.dto.request.PetUpdateRequest;
import com.example.pettracker.enums.PetType;
import com.example.pettracker.enums.TrackerType;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Cat extends Pet {

    private Boolean lostTracker;

    @Builder
    public Cat(TrackerType trackerType,
               Integer ownerId,
               Boolean inZone,
               Boolean lostTracker) {
        super(PetType.CAT, trackerType, ownerId, inZone);
        this.lostTracker = lostTracker;
    }

    @Override
    public void updateStatus(PetUpdateRequest petUpdateRequest) {
        super.updateStatus(petUpdateRequest);
        if (petUpdateRequest.lostTracker() != null) {
            this.lostTracker = petUpdateRequest.lostTracker();
        }
    }
}