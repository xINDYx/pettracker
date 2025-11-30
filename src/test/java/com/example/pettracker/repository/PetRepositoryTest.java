package com.example.pettracker.repository;

import com.example.pettracker.configuration.TestPetConfig;
import com.example.pettracker.dto.response.OutOfZoneCountDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
@Import(TestPetConfig.class)
class PetRepositoryTest {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private TestPetConfig.TestPetFactory factory;

    @Test
    void testCountOutOfZoneGrouped() {
        petRepository.save(factory.createCat(false, false));
        petRepository.save(factory.createCat(false, true));
        petRepository.save(factory.createDog(false));
        petRepository.save(factory.createDog(true));

        List<OutOfZoneCountDto> results = petRepository.countOutOfZoneGrouped();

        assertEquals(3, results.stream().mapToLong(OutOfZoneCountDto::count).sum());
    }
}
