package com.itheima.reegie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reegie.common.CustomException;
import com.itheima.reegie.entity.Category;
import com.itheima.reegie.entity.Dish;
import com.itheima.reegie.entity.Setmeal;
import com.itheima.reegie.mapper.CategoryMapper;
import com.itheima.reegie.service.CategoryService;
import com.itheima.reegie.service.DishService;
import com.itheima.reegie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除分类，删除之前需要进行判断
     * @param id
     */
    // 因为MybatisPlus提供的业务逻辑方法无法满足我们的需求，所以我们需要自制逻辑删除方法
    //1. 判断当前分类的id是否关联了菜品，如果有关联，不能被删除，抛出异常，由全局异常处理器处理这个异常信息
    //2. 判断当前分类信息有没有关联套餐信息，如果有关联，不能被删除，抛出自定义异常，由全局异常处理器来处理异常信息
    //3. 如果当前分类既没有关联菜品，又没有关联套餐，可以删除
    @Override
    public void remove(Long id) {
        //1. 创建菜品查询条件对象
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //2. 添加，根据分类id查询对应的菜品条件
        // select count(*) from dish where categoryId = ?
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        //3. 执行查询菜品操作
        int count = (int) dishService.count(dishLambdaQueryWrapper);
        //4. 判断当前分类的id有没有关联菜品，如果有，抛出自定义异常
        if(count > 0) throw  new CustomException("当前分类下关联了对应的菜品信息，不能被删除");
        //5. 创建套餐查询条件对象
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //6. 添加，根据分类id查询对应的套餐信息
        //select count(*) from setmeal where categoryId = ?
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        //7. 执行查询套餐操作，查询对应的套餐个数
        int count1 = (int) setmealService.count(setmealLambdaQueryWrapper);
        //8. 判断当前分类的id是否关联了套餐信息
        if(count1 > 0) throw new CustomException("当前分类先关联了对应的套餐信息，不能被删除");
        //9. 如果分类的id既没有关联菜品也没有关联套餐，可以被删除
        this.removeById(id);
    }
}
