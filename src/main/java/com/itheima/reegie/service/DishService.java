package com.itheima.reegie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reegie.dto.DishDto;
import com.itheima.reegie.entity.Dish;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DishService extends IService<Dish> {
    void saveWithFlavors(DishDto dishDto);

    @Transactional
    void updateWithFlavors(DishDto dishDto);

    void removeWithFlavors(List<Long> ids);

    Page<DishDto> pageForDto(int page, int pageSize, String name);

    DishDto getDtoById(Long id);

    List<DishDto> getDtoListByCategoryId(Long categoryId);

    void updateStatus(Integer status, Long[] ids);
}
