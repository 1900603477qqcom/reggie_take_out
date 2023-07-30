package com.itheima.reegie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reegie.dto.DishDto;
import com.itheima.reegie.entity.Dish;
import com.itheima.reegie.entity.DishFlavor;
import com.itheima.reegie.mapper.DishMapper;
import com.itheima.reegie.service.DishFlavorService;
import com.itheima.reegie.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    /**
     *新增菜品，同时保存对应的口味数据
     *@paramdishDto
     */
    @Transactional
    public void saveWithFlavors(DishDto dishDto) {
        //保存菜品的基本信息到菜品dish
        this.save(dishDto);

        Long dishID = dishDto.getId(); //菜品id

        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item)->{
            item.setDishId(dishID);
            return item;
        }).collect(Collectors.toList());

        //保存菜品口味数据到菜品口味表dish_flavor
        dishFlavorService.saveBatch(flavors);

    }
}
