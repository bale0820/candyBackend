package com.tjg_project.candy.domain.category.controller;

import com.tjg_project.candy.domain.category.entity.CategoryMain;
import com.tjg_project.candy.domain.category.service.CategoryMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryMainService categoryMainService;

    @GetMapping("/list")
    public List<CategoryMain> getList() {
        return categoryMainService.getList();
    }

}
