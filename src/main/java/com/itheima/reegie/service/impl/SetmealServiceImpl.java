package com.itheima.reegie.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reegie.dto.DishDto;
import com.itheima.reegie.dto.SetmealDto;
import com.itheima.reegie.entity.Setmeal;
import com.itheima.reegie.entity.SetmealDish;
import com.itheima.reegie.mapper.SetmealMapper;
import com.itheima.reegie.service.DishService;
import com.itheima.reegie.service.SetmealDishService;
import com.itheima.reegie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealDishService setmealDishService;
    
    /**
     *  新增套餐信息
     *   1. 需要将dto中的菜品信息添加到套餐信息关联表中
     *    @param setmealDto
     */
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        //1、保存套餐菜品的基本信息
        this.save(setmealDto);
        //2、保存套餐菜品关联信息
        //获取前端套餐，菜品关联信息
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        //使用流
        setmealDto.setSetmealDishes(setmealDishes.stream().map(item -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList()));
        //保存套餐菜品关联对象信息
        setmealDishService.saveBatch(setmealDto.getSetmealDishes());
    }

    @Override
    public void removeWithDish(List<Long> ids) {

    }

    @Override
    public void updateWithDish(SetmealDto setmealDto) {

    }

    @Override
    public Page<SetmealDto> pageForDto(int page, int pageSize, String name) {
        return null;
    }

    @Override
    public SetmealDto getDtoById(Long id) {
        return null;
    }

    @Override
    public void updateStatus(Integer status, Long[] ids) {

    }

    @Override
    public List<SetmealDto> getListByCategoryId(Long categoryId) {
        return null;
    }

    @Override
    public List<DishDto> getDishById(Long id) {
        return null;
    }
}
