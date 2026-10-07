package com.csci318.lost.controller;

import com.csci318.lost.dto.LostItemRequest;
import com.csci318.lost.model.LostItemStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LostItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateAndGetLostItem() throws Exception {
        LostItemRequest request = new LostItemRequest(
                1L,
                "AirPods Pro",
                "Electronics",
                "White AirPods Pro with a pink protective case",
                "UOW Library",
                LocalDate.of(2026, 9, 15),
                LostItemStatus.OPEN
        );

        String jsonContent = objectMapper.writeValueAsString(request);

        // POST /api/lost-items -> 201 Created
        String response = mockMvc.perform(post("/api/lost-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.itemName").value("AirPods Pro"))
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andReturn().getResponse().getContentAsString();

        // Parse created ID
        Long createdId = objectMapper.readTree(response).get("id").asLong();

        // GET /api/lost-items/{id} -> 200 OK
        mockMvc.perform(get("/api/lost-items/" + createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdId))
                .andExpect(jsonPath("$.category").value("Electronics"));

        // GET /api/lost-items -> 200 OK
        mockMvc.perform(get("/api/lost-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testGetNonExistentLostItemReturns404() throws Exception {
        mockMvc.perform(get("/api/lost-items/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    public void testCreateLostItemInvalidPayloadReturns400() throws Exception {
        LostItemRequest invalidReq = new LostItemRequest();
        invalidReq.setItemName(""); // Empty item name triggers IllegalArgumentException

        mockMvc.perform(post("/api/lost-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidReq)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    public void testUpdateLostItemStatus() throws Exception {
        LostItemRequest createReq = new LostItemRequest(
                2L,
                "Blue Backpack",
                "Bags",
                "Navy blue Jansport backpack",
                "Building 17",
                LocalDate.now(),
                LostItemStatus.OPEN
        );

        String createJson = objectMapper.writeValueAsString(createReq);

        String response = mockMvc.perform(post("/api/lost-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        LostItemRequest updateReq = new LostItemRequest();
        updateReq.setStatus(LostItemStatus.RECOVERED);

        mockMvc.perform(put("/api/lost-items/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("RECOVERED"));
    }

    @Test
    public void testDeleteLostItem() throws Exception {
        LostItemRequest createReq = new LostItemRequest(
                3L,
                "Keys",
                "Keys",
                "Keychain with 3 keys",
                "Cafeteria",
                LocalDate.now(),
                LostItemStatus.OPEN
        );

        String response = mockMvc.perform(post("/api/lost-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/api/lost-items/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/lost-items/" + id))
                .andExpect(status().isNotFound());
    }
}
