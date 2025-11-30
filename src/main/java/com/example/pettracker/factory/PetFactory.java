package com.example.pettracker.factory;

import com.example.pettracker.dto.request.PetCreateRequest;
import com.example.pettracker.exception.InvalidTrackerTypeException;
import com.example.pettracker.model.Cat;
import com.example.pettracker.model.Dog;
import com.example.pettracker.model.Pet;
import org.springframework.stereotype.Component;

@Component
public class PetFactory {

    public Pet create(PetCreateRequest r) {
        validateTrackerType(r);

        return switch (r.petType()) {
            case CAT -> new Cat(r.trackerType(), r.ownerId(), r.inZone(), r.lostTracker());
            case DOG -> new Dog(r.trackerType(), r.ownerId(), r.inZone());
        };
    }

    private void validateTrackerType(PetCreateRequest r) {
        if (r.trackerType().getPetType() != r.petType()) {
            throw new InvalidTrackerTypeException(r.trackerType(), r.petType());
        }
    }
}
