package com.example.pettracker.repository;

import com.example.pettracker.configuration.TestPetConfig;
import com.example.pettracker.enums.TrackerType;
import com.example.pettracker.model.Cat;
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
class CatRepositoryTest {

    @Autowired
    private CatRepository catRepository;

    @Autowired
    private TestPetConfig.TestPetFactory factory;

    @Test
    void testSaveAndFindById() {
        Cat cat = factory.createCat(true, false);

        Cat savedCat = catRepository.save(cat);

        assertNotNull(savedCat.getId());

        Optional<Cat> foundCat = catRepository.findById(savedCat.getId());
        assertTrue(foundCat.isPresent());
        assertEquals(savedCat.getId(), foundCat.get().getId());
        assertEquals(savedCat.getTrackerType(), foundCat.get().getTrackerType());
        assertEquals(savedCat.getLostTracker(), foundCat.get().getLostTracker());
    }

    @Test
    void testDelete() {
        Cat cat = factory.createCat(true, false);

        Cat savedCat = catRepository.save(cat);
        UUID id = savedCat.getId();

        catRepository.deleteById(id);
        assertFalse(catRepository.findById(id).isPresent());
    }
}

