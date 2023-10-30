package com.learning.springboot.checklistapi.controller;

import com.learning.springboot.checklistapi.dto.ChecklistItemDTO;
import com.learning.springboot.checklistapi.entity.ChecklistItemEntity;
import com.learning.springboot.checklistapi.exception.ValidationException;
import com.learning.springboot.checklistapi.service.ChecklistItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/checklist-items")
public class ChecklistItemController {

    private final ChecklistItemService checklistItemService;

    public ChecklistItemController(ChecklistItemService checklistItemService){
        this.checklistItemService = checklistItemService;
    }

    @Operation(description = "Retrieves all chesklist items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all checklist items")
    })
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ChecklistItemDTO>> getAllChecklistItems(){

        List<ChecklistItemDTO> response =  this.checklistItemService.findAllChecklistItems()
                .stream().map(ChecklistItemDTO::toDTO)
                .toList();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(description = "Creates chesklist item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Checklist item created")
    })
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createChecklistItems(@RequestBody ChecklistItemDTO checklistItemDTO){
        if(checklistItemDTO.categoryDTO() == null){
            throw new ValidationException("Category cannot be null");
        }
        ChecklistItemEntity newChecklistItem = this.checklistItemService.addNewChecklistItem(
                 checklistItemDTO.description(), checklistItemDTO.isCompleted(),
                 checklistItemDTO.deadline(), checklistItemDTO.categoryDTO().guid());

        return new ResponseEntity<>(newChecklistItem.getGuid(), HttpStatus.CREATED);
    }

    @Operation(description = "Modify a chesklist item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Checklist item modified")
    })
    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateChecklistItems(@RequestBody ChecklistItemDTO checklistItemDTO){
        if(StringUtils.hasLength(checklistItemDTO.guid())){
            throw new ValidationException("Checklist Item guid is mandatory");
        }
        this.checklistItemService.updateChecklistItem(checklistItemDTO.guid(), checklistItemDTO.description(),
                checklistItemDTO.isCompleted(), checklistItemDTO.deadline(),
                checklistItemDTO.categoryDTO() != null ? checklistItemDTO.categoryDTO().guid() : null);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(description = "Retrieves all chesklist Items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Checklist item deleted")
    })
    @DeleteMapping(value = "{guid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteChecklistItem(@PathVariable String guid){
        this.checklistItemService.deleteChecklistItem(guid);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
