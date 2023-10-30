package com.learning.springboot.checklistapi.controller;

import com.learning.springboot.checklistapi.dto.CategoryDTO;
import com.learning.springboot.checklistapi.entity.CategoryEntity;
import com.learning.springboot.checklistapi.exception.ValidationException;
import com.learning.springboot.checklistapi.service.CategoryService;
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
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @Operation(description = "Retrieves all categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all categories")
    })
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CategoryDTO>> getAllCategories(){

        List<CategoryDTO> response = this.categoryService.findAllCategories().stream()
                .map(CategoryDTO::toDTO)
                .toList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(description = "Create a category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created")
    })
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addNewCategory(@RequestBody CategoryDTO categoryDTO){

        CategoryEntity newCategory = this.categoryService.addNewCategory(categoryDTO.name());
        return new ResponseEntity<>(newCategory.getGuid(), HttpStatus.CREATED);
    }

    @Operation(description = "Modify a category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category modified")
    })
    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateCategory(@RequestBody CategoryDTO categoryDTO){
        if(!StringUtils.hasText(categoryDTO.guid())){
            throw new ValidationException("Category guid cannot be null or empty");
        }
        this.categoryService.updateCategory(categoryDTO.guid(), categoryDTO.name());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(description = "Delete a category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category deleted")
    })
    @DeleteMapping(value = "{guid}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String guid){
        this.categoryService.deleteCategory(guid);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
