package com.example.pettracker.configuration;

import com.example.pettracker.dto.request.PetCreateRequest;
import com.example.pettracker.enums.PetType;
import com.example.pettracker.enums.TrackerType;
import com.example.pettracker.model.Cat;
import com.example.pettracker.model.Dog;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestPetConfig {

    @Bean
    public TestPetFactory testPetFactory() {
        return new TestPetFactory();
    }

    public static class TestPetFactory {

        public Cat createCat(Boolean inZone, Boolean lostTracker) {
            return Cat.builder()
                    .trackerType(TrackerType.SMALL_CAT)
                    .ownerId(1)
                    .inZone(inZone)
                    .lostTracker(lostTracker)
                    .build();
        }

        public Dog createDog(Boolean inZone) {
            return Dog.builder()
                    .trackerType(TrackerType.MEDIUM_DOG)
                    .ownerId(1)
                    .inZone(inZone)
                    .build();
        }

        public PetCreateRequest catCreateRequest(Boolean inZone, Boolean lostTracker) {
            return new PetCreateRequest(PetType.CAT, TrackerType.SMALL_CAT, 1, inZone, lostTracker);
        }

        public PetCreateRequest dogCreateRequest(Boolean inZone) {
            return new PetCreateRequest(PetType.DOG, TrackerType.MEDIUM_DOG, 1, inZone, null);
        }
    }
}
