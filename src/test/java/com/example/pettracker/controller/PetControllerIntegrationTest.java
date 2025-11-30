package com.example.pettracker.controller;

import com.example.pettracker.configuration.TestPetConfig;
import com.example.pettracker.dto.request.PetCreateRequest;
import com.example.pettracker.dto.request.PetUpdateRequest;
import com.example.pettracker.dto.response.OutOfZoneResponse;
import com.example.pettracker.dto.response.PetResponse;
import com.example.pettracker.enums.PetType;
import com.example.pettracker.enums.TrackerType;
import com.example.pettracker.model.Pet;
import com.example.pettracker.repository.PetRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Import(TestPetConfig.class)
class PetControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestPetConfig.TestPetFactory factory;

    @Test
    void testCreatePet() throws Exception {
        PetCreateRequest createRequest = factory.catCreateRequest(true, false);

        String createJson = objectMapper.writeValueAsString(createRequest);

        MvcResult result = mockMvc.perform(post("/api/v1/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isOk())
                .andReturn();

        PetResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), PetResponse.class);

        assertNotNull(response.id());
        assertEquals(PetType.CAT, response.petType());
        assertFalse(response.lostTracker());
    }

    @Test
    void testUpdatePetStatus() throws Exception {
        PetCreateRequest createRequest = factory.catCreateRequest(true, false);
        MvcResult createResult = mockMvc.perform(post("/api/v1/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andReturn();

        PetResponse created = objectMapper.readValue(createResult.getResponse().getContentAsString(), PetResponse.class);

        PetUpdateRequest updateRequest = new PetUpdateRequest(false, true);
        MvcResult updateResult = mockMvc.perform(put("/api/v1/pets/{id}", created.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andReturn();

        PetResponse updated = objectMapper.readValue(updateResult.getResponse().getContentAsString(), PetResponse.class);

        assertEquals(created.id(), updated.id());
        assertFalse(updated.inZone());
        assertTrue(updated.lostTracker());
    }

    @Test
    void testGetOutOfZone() throws Exception {
        mockMvc.perform(post("/api/v1/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(factory.catCreateRequest(false, false))))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(factory.dogCreateRequest(false))))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get("/api/v1/pets/out-of-zone"))
                .andExpect(status().isOk())
                .andReturn();

        OutOfZoneResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), OutOfZoneResponse.class);

        assertEquals(2, response.stats().size());
        assertTrue(response.stats().stream().anyMatch(s -> s.petType() == PetType.CAT));
        assertTrue(response.stats().stream().anyMatch(s -> s.petType() == PetType.DOG));
    }
}

