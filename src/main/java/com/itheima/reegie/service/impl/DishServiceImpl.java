package com.itheima.reegie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reegie.entity.Dish;
import com.itheima.reegie.mapper.DishMapper;
import com.itheima.reegie.service.DishService;
import org.springframework.stereotype.Service;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
