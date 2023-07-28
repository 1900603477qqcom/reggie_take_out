package com.itheima.reegie.controller;

import com.itheima.reegie.service.CategoryService;
import com.itheima.reegie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/dish")
@Slf4j
@RestController
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;
}
