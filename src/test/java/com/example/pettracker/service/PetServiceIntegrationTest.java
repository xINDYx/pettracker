package com.example.pettracker.service;

import com.example.pettracker.configuration.TestPetConfig;
import com.example.pettracker.dto.request.PetCreateRequest;
import com.example.pettracker.dto.request.PetUpdateRequest;
import com.example.pettracker.dto.response.OutOfZoneResponse;
import com.example.pettracker.dto.response.PetOutOfZoneStats;
import com.example.pettracker.enums.PetType;
import com.example.pettracker.model.Cat;
import com.example.pettracker.model.Dog;
import com.example.pettracker.model.Pet;
import com.example.pettracker.repository.CatRepository;
import com.example.pettracker.repository.DogRepository;
import com.example.pettracker.repository.PetRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
@Import(TestPetConfig.class)
class PetServiceIntegrationTest {

    @Autowired
    private PetService petService;

    @Autowired
    private CatRepository catRepository;

    @Autowired
    private DogRepository dogRepository;

    @Autowired
    private TestPetConfig.TestPetFactory factory;

    @Test
    void testCreateAndFindCat() {
        PetCreateRequest request = factory.catCreateRequest(true, false);
        Pet created = petService.createPet(request);

        assertNotNull(created.getId());
        assertEquals(PetType.CAT, created.getPetType());

        Cat cat = catRepository.findById(created.getId()).orElseThrow();
        assertEquals(false, cat.getLostTracker());
        assertTrue(cat.getInZone());
    }

    @Test
    void testCreateAndFindDog() {
        PetCreateRequest request = factory.dogCreateRequest(false);
        Pet created = petService.createPet(request);

        assertNotNull(created.getId());
        assertEquals(PetType.DOG, created.getPetType());

        Dog dog = dogRepository.findById(created.getId()).orElseThrow();
        assertFalse(dog.getInZone());
    }

    @Test
    void testUpdatePetStatus() {
        Cat cat = factory.createCat(true, false);
        cat = catRepository.save(cat);

        PetUpdateRequest updateRequest = new PetUpdateRequest(false, true);
        Pet updated = petService.updatePetStatus(cat.getId(), updateRequest);

        assertFalse(updated.getInZone());
        assertTrue(((Cat) updated).getLostTracker());
    }

    @Test
    void testGetOutOfZoneCount() {
        petService.createPet(factory.catCreateRequest(false, false));
        petService.createPet(factory.catCreateRequest(false, true));
        petService.createPet(factory.dogCreateRequest(false));

        OutOfZoneResponse response = petService.getOutOfZoneCount();

        assertEquals(2, response.stats().size());

        PetOutOfZoneStats catStats = response.stats().stream()
                .filter(s -> s.petType().equals(PetType.CAT))
                .findFirst().orElseThrow();
        PetOutOfZoneStats dogStats = response.stats().stream()
                .filter(s -> s.petType().equals(PetType.DOG))
                .findFirst().orElseThrow();

        assertEquals(1, catStats.trackers().size());
        assertEquals(2L, catStats.trackers().get(0).count());
        assertEquals(1, dogStats.trackers().size());
        assertEquals(1L, dogStats.trackers().get(0).count());
    }
}


