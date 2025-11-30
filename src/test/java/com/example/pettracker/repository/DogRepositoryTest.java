package com.example.pettracker.repository;

import com.example.pettracker.configuration.TestPetConfig;
import com.example.pettracker.model.Dog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
@Import(TestPetConfig.class)
class DogRepositoryTest {

    @Autowired
    private DogRepository dogRepository;

    @Autowired
    private TestPetConfig.TestPetFactory factory;

    @Test
    void testSaveAndFindById() {
        Dog dog = factory.createDog(true);

        Dog savedDog = dogRepository.save(dog);

        assertNotNull(savedDog.getId());

        Optional<Dog> foundDog = dogRepository.findById(savedDog.getId());
        assertTrue(foundDog.isPresent());
        assertEquals(savedDog.getId(), foundDog.get().getId());
        assertEquals(savedDog.getTrackerType(), foundDog.get().getTrackerType());
        assertEquals(savedDog.getInZone(), foundDog.get().getInZone());
    }

    @Test
    void testDelete() {
        Dog dog = factory.createDog(true);

        Dog savedDog = dogRepository.save(dog);
        UUID id = savedDog.getId();

        dogRepository.deleteById(id);
        assertFalse(dogRepository.findById(id).isPresent());
    }
}

