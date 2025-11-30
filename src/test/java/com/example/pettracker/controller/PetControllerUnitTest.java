package com.example.pettracker.controller;

import com.example.pettracker.configuration.TestPetConfig;
import com.example.pettracker.dto.request.PetCreateRequest;
import com.example.pettracker.dto.response.OutOfZoneResponse;
import com.example.pettracker.model.Cat;
import com.example.pettracker.model.Pet;
import com.example.pettracker.service.PetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class PetControllerUnitTest {

    private MockMvc mockMvc;
    private PetService petService;
    private ObjectMapper objectMapper;
    private TestPetConfig.TestPetFactory factory;

    @BeforeEach
    void setup() {
        petService = Mockito.mock(PetService.class);
        PetController controller = new PetController(petService);
        objectMapper = new ObjectMapper();
        factory = new TestPetConfig.TestPetFactory();

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
    }

    @Test
    void testGetOutOfZoneCount() throws Exception {
        OutOfZoneResponse response = new OutOfZoneResponse(List.of());
        Mockito.when(petService.getOutOfZoneCount()).thenReturn(response);

        mockMvc.perform(get("/api/v1/pets/out-of-zone"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stats").isArray());
    }

    @Test
    void testCreatePet() throws Exception {
        PetCreateRequest request = factory.catCreateRequest(true, false);

        Pet createdPet = Cat.builder()
                .trackerType(request.trackerType())
                .ownerId(request.ownerId())
                .inZone(request.inZone())
                .lostTracker(request.lostTracker())
                .build();

        Mockito.when(petService.createPet(Mockito.any())).thenReturn(createdPet);

        mockMvc.perform(post("/api/v1/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.petType").value("CAT"));
    }
}

