package com.learning.springboot.checklistapi.controller;

import com.learning.springboot.checklistapi.dto.ChecklistItemDTO;
import com.learning.springboot.checklistapi.entity.ChecklistItemEntity;
import com.learning.springboot.checklistapi.exception.ValidationException;
import com.learning.springboot.checklistapi.service.ChecklistItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/checklist-items")
public class ChecklistItemController {

    private final ChecklistItemService checklistItemService;

    public ChecklistItemController(ChecklistItemService checklistItemService){
        this.checklistItemService = checklistItemService;
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ChecklistItemDTO>> getAllChecklistItems(){

        List<ChecklistItemDTO> response =  this.checklistItemService.findAllChecklistItems()
                .stream().map(ChecklistItemDTO::toDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

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
    @DeleteMapping(value = "{guid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteChecklistItem(@PathVariable String guid){
        this.checklistItemService.deleteChecklistItem(guid);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
