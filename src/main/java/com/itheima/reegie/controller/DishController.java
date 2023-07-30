package com.itheima.reegie.controller;

import com.itheima.reegie.common.R;
import com.itheima.reegie.dto.DishDto;
import com.itheima.reegie.service.CategoryService;
import com.itheima.reegie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    /**
     * 新增菜品
     * @paramdishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.saveWithFlavors(dishDto);
        return R.success("添加菜品成功");
    }

}
