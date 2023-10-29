package com.learning.springboot.checklistapi.dataloader;

import com.learning.springboot.checklistapi.entity.CategoryEntity;
import com.learning.springboot.checklistapi.repository.CategoryRepository;
import com.learning.springboot.checklistapi.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Profile("data-load")
@Slf4j
@Component
public class DbLoader implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;

    public DbLoader(CategoryRepository categoryRepository, CategoryService categoryService){
        this.categoryRepository = categoryRepository;
        this.categoryService = categoryService;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Populating db with categories");
        List<String> categoryNames = Arrays.asList(
                "Trabalho", "Saúde", "Educação", "Pessoal", "Outros"
        );

        for(String categoryName : categoryNames){
            Optional<CategoryEntity> catOpt = this.categoryRepository.findByName(categoryName);
           if(!catOpt.isPresent()){
               categoryService.addNewCategory(categoryName);
           }
        }
    }
}
