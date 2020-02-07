package com.spring.recipes.controllers;

import com.spring.recipes.domain.Category;
import com.spring.recipes.repositories.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class IndexController {

    private CategoryRepository categoryRepository;

    public IndexController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping({"", "/", "/index", "/index.html"})
    public String getIndexPage() {
        StringBuilder text = new StringBuilder();
        Iterable<Category> categories = categoryRepository.findAll();

        categories.forEach(category -> {
            text.append(category.getDescription()).append("; ");
        });

        log.info("Categories in db: " + text.toString());
        return "index";
    }
}