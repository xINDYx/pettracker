package com.example.pettracker.dto.response;

import com.example.pettracker.enums.PetType;
import com.example.pettracker.enums.TrackerType;
import com.example.pettracker.model.Cat;
import com.example.pettracker.model.Pet;

import java.util.UUID;

public record PetResponse(
        UUID id,
        PetType petType,
        TrackerType trackerType,
        Integer ownerId,
        Boolean inZone,
        Boolean lostTracker
) {
    public static PetResponse from(Pet pet) {
        Boolean lostTracker = pet instanceof Cat cat ? cat.getLostTracker() : null;
        return new PetResponse(
                pet.getId(),
                pet.getPetType(),
                pet.getTrackerType(),
                pet.getOwnerId(),
                pet.getInZone(),
                lostTracker
        );
    }
}
