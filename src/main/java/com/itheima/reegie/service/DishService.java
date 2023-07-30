package com.itheima.reegie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reegie.dto.DishDto;
import com.itheima.reegie.entity.Dish;

public interface DishService extends IService<Dish> {
    void saveWithFlavors(DishDto dishDto);
}
