package com.csci318.found.controller;

import com.csci318.found.dto.FoundItemRequest;
import com.csci318.found.model.FoundItemStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FoundItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateAndGetFoundItem() throws Exception {
        FoundItemRequest request = new FoundItemRequest(
                101L,
                "Sony Headphones",
                "Electronics",
                "Black noise cancelling headphones found on table",
                "Building 6",
                LocalDate.of(2026, 9, 16),
                FoundItemStatus.UNCLAIMED
        );

        String jsonContent = objectMapper.writeValueAsString(request);

        // POST /api/found-items -> 201 Created
        String response = mockMvc.perform(post("/api/found-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.itemName").value("Sony Headphones"))
                .andExpect(jsonPath("$.status").value("UNCLAIMED"))
                .andReturn().getResponse().getContentAsString();

        Long createdId = objectMapper.readTree(response).get("id").asLong();

        // GET /api/found-items/{id} -> 200 OK
        mockMvc.perform(get("/api/found-items/" + createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdId))
                .andExpect(jsonPath("$.category").value("Electronics"));

        // GET /api/found-items -> 200 OK
        mockMvc.perform(get("/api/found-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testGetNonExistentFoundItemReturns404() throws Exception {
        mockMvc.perform(get("/api/found-items/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    public void testUpdateFoundItemStatus() throws Exception {
        FoundItemRequest createReq = new FoundItemRequest(
                102L,
                "Water Bottle",
                "Personal Belongings",
                "Stainless steel Hydroflask",
                "Gym",
                LocalDate.now(),
                FoundItemStatus.UNCLAIMED
        );

        String createJson = objectMapper.writeValueAsString(createReq);

        String response = mockMvc.perform(post("/api/found-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        FoundItemRequest updateReq = new FoundItemRequest();
        updateReq.setStatus(FoundItemStatus.CLAIMED);

        mockMvc.perform(put("/api/found-items/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CLAIMED"));
    }

    @Test
    public void testDeleteFoundItem() throws Exception {
        FoundItemRequest createReq = new FoundItemRequest(
                103L,
                "Calculator",
                "Electronics",
                "CASIO FX-82AU PLUS II",
                "Lecture Hall 1",
                LocalDate.now(),
                FoundItemStatus.UNCLAIMED
        );

        String response = mockMvc.perform(post("/api/found-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/api/found-items/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/found-items/" + id))
                .andExpect(status().isNotFound());
    }
}
