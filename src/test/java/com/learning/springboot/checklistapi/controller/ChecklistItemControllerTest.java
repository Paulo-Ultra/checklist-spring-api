package com.learning.springboot.checklistapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.springboot.checklistapi.dto.CategoryDTO;
import com.learning.springboot.checklistapi.dto.ChecklistItemDTO;
import com.learning.springboot.checklistapi.entity.CategoryEntity;
import com.learning.springboot.checklistapi.entity.ChecklistItemEntity;
import com.learning.springboot.checklistapi.service.ChecklistItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChecklistItemController.class)
class ChecklistItemControllerTest {

    @MockBean
    private ChecklistItemService checklistItemService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void shouldCallGetAllChecklistItemsAndReturn200() throws Exception {

        List<ChecklistItemEntity> findAllData = (Arrays.asList(
                getChecklistItem(1L, "Item 1", false, LocalDate.now(), 1L, "Cat 1"),
                getChecklistItem(2L, "Item 2", true, LocalDate.now(), 2L, "Cat 2")
        ));
        when(checklistItemService.findAllChecklistItems()).thenReturn(findAllData);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/checklist-items"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(2)))
                .andExpect(jsonPath("$[0].guid").isNotEmpty())
                .andExpect(jsonPath("$[0].isCompleted").value(false))
                .andExpect(jsonPath("$[0].description").value("Item 1"))
                .andExpect(jsonPath("$[0].deadline").value("2023-11-17"))
                .andExpect(jsonPath("$[0].postedDate").isNotEmpty())
                .andExpect(jsonPath("$[0].category.guid").isNotEmpty())
                .andExpect(jsonPath("$[0].category.name").value("Cat 1"))
                .andExpect(jsonPath("$[1].guid").isNotEmpty())
                .andExpect(jsonPath("$[1].isCompleted").value(true))
                .andExpect(jsonPath("$[1].description").value("Item 2"))
                .andExpect(jsonPath("$[1].deadline").value("2023-11-17"))
                .andExpect(jsonPath("$[1].postedDate").isNotEmpty())
                .andExpect(jsonPath("$[1].category.guid").isNotEmpty())
                .andExpect(jsonPath("$[1].category.name").value("Cat 2"));
    }

    @Test
    void shouldCallEndpointAndAddNewChecklistItemAndReturn201() throws Exception {

        when(this.checklistItemService.addNewChecklistItem(anyString(), anyBoolean(), any(LocalDate.class),
                anyString())).thenReturn(getChecklistItem(1L, "Item 1", false, LocalDate.now(), 1L, "Cat 1"));

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/checklist-items")
                .content(objectMapper.writeValueAsString(
                        getChecklistItemDTO("Teste", true, LocalDate.now(), "Teste Category")
                ))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.guid").isNotEmpty());
    }

    @Test
    void shouldCallEndpointAndUpdateChecklistItemAndReturn204() throws Exception {

        when(this.checklistItemService.addNewChecklistItem(anyString(), anyBoolean(), any(LocalDate.class),
                anyString())).thenReturn(getChecklistItem(1L, "Item 1", false, LocalDate.now(), 1L, "Cat 1"));

        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/checklist-items")
                        .content(objectMapper.writeValueAsString(
                                getChecklistItemDTO("Teste", true, LocalDate.now(), "Teste Category")
                        ))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldCallEndpointAndDeleteChecklistItemAndReturn204() throws Exception {

        String checklistItemGuid = UUID.randomUUID().toString();

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/checklist-items/{guid}", checklistItemGuid)
                    .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNoContent());

         verify(checklistItemService, times(1)).deleteChecklistItem(checklistItemGuid);
    }

    @Test
    void shouldCallEndpointAndPatchChecklistItemAndReturn202() throws Exception {

        //when(this.checklistItemService.updateIsCompletedStatus(anyString(), anyBoolean())).thenReturn());

        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/checklist-items")
                        .content(objectMapper.writeValueAsString(
                                getChecklistItemDTO("Teste", true, LocalDate.now(), "Teste Category")
                        ))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    private ChecklistItemDTO getChecklistItemDTO(String description, Boolean isCompleted, LocalDate deadline, String categoryName){
        return ChecklistItemDTO.builder()
                .guid(UUID.randomUUID().toString())
                .description(description)
                .isCompleted(isCompleted)
                .deadline(deadline)
                .categoryDTO(CategoryDTO.builder()
                        .guid(UUID.randomUUID().toString())
                        .name(categoryName)
                        .build())
                .postedDate(LocalDate.now())
                .build();
    }

    private ChecklistItemEntity getChecklistItem(Long id, String description, Boolean isCompleted, LocalDate deadline,
                                                 Long categoryId, String categoryName){

        ChecklistItemEntity checklistItem = new ChecklistItemEntity();
        checklistItem.setId(id);
        checklistItem.setGuid(UUID.randomUUID().toString());
        checklistItem.setIsCompleted(isCompleted);
        CategoryEntity category = new CategoryEntity();
        category.setId(categoryId);
        category.setGuid(UUID.randomUUID().toString());
        category.setName(categoryName);
        checklistItem.setCategory(category);
        checklistItem.setDescription(description);
        checklistItem.setDeadline(deadline);
        checklistItem.setPostedDate(LocalDate.now());

        return checklistItem;
    }
}
