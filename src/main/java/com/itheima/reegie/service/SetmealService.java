package com.itheima.reegie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reegie.dto.DishDto;
import com.itheima.reegie.dto.SetmealDto;
import com.itheima.reegie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    //1. 新增套餐，新增套餐基本信息和套餐关联菜品信息
    void saveWithDish(SetmealDto setmealDto);
    //2. 删除套餐，删除套餐基本信息和套餐关联菜品信息
    void removeWithDish(List<Long> ids);
    //3. 修改菜品信息，修改菜品基本信息和套餐关联菜品信息
    void updateWithDish(SetmealDto setmealDto);
    //4. 获取dto分页
    Page<SetmealDto> pageForDto(int page, int pageSize, String name);
    //5. 通过id获取dto对象
    SetmealDto getDtoById(Long id);
    // 批量起售停售方法
    void updateStatus(Integer status,Long[] ids);
    //根据分类id获取套餐列表
    List<SetmealDto> getListByCategoryId(Long categoryId);
    //根据套餐id获取套餐的全部菜品信息
    List<DishDto> getDishById(Long id);
}
