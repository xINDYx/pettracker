package com.example.pettracker.enums;

import lombok.Getter;

@Getter
public enum TrackerType {
    SMALL_CAT(PetType.CAT),
    BIG_CAT(PetType.CAT),
    SMALL_DOG(PetType.DOG),
    MEDIUM_DOG(PetType.DOG),
    BIG_DOG(PetType.DOG);

    private final PetType petType;

    TrackerType(PetType petType) {
        this.petType = petType;
    }
}
