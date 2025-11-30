package com.example.pettracker.model;

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
public class Dog extends Pet {

    @Builder
    public Dog(TrackerType trackerType,
               Integer ownerId,
               Boolean inZone) {
        super(PetType.DOG, trackerType, ownerId, inZone);
    }
}
