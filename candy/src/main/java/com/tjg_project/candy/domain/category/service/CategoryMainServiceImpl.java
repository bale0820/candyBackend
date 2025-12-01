package com.tjg_project.candy.domain.category.service;


import com.tjg_project.candy.domain.category.entity.CategoryMain;
import com.tjg_project.candy.domain.category.repository.CategoryMainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryMainServiceImpl implements CategoryMainService {

    @Autowired
    private CategoryMainRepository categoryMainRepository;

    @Override
    public List<CategoryMain> getList() {
        return categoryMainRepository.findAll();
    }
}
