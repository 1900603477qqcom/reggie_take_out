package com.itheima.reegie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reegie.common.CustomException;
import com.itheima.reegie.dto.DishDto;
import com.itheima.reegie.entity.Dish;
import com.itheima.reegie.entity.DishFlavor;
import com.itheima.reegie.entity.SetmealDish;
import com.itheima.reegie.mapper.DishMapper;
import com.itheima.reegie.service.DishFlavorService;
import com.itheima.reegie.service.DishService;
import com.itheima.reegie.service.SetmealDishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private SetmealDishService setmealDishService;

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

    /**
     * 修改菜品信息
     * 1. 接收的是一个dto对象
     * 2. 修改分两步，第一步修改dish基础信息，第二步将数据库中相关的口味信息全部删除后存放dto中的口味信息，暴力更新
     * 3. 判断该菜品是否被套餐引用，如果被引用，需要修改其在菜品套餐表里的数据
     * @param dishDto
     */
    @Transactional
    @Override
    public void updateWithFlavors(DishDto dishDto) {
        //1. 修改该dish的基础信息
        this.updateById(dishDto);
        //2. 修改该dish中的flavor列表的信息 - (暴力方法，删除所有与此dish相关的口味后再把新口味列表添加进表内)
        //2.1 创建条件对象
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //2.2 查询条件：id与dish相同的记录
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        //2.3 删除相关口味记录
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);
        //3. 获取需要新增的口味列表
        List<DishFlavor> dishFlavorList = dishDto.getFlavors();
        //4. 新增口味列表
        //4.1 补全口味dish_id
        dishFlavorList.forEach(item -> {
            item.setDishId(dishDto.getId());
        });
        dishFlavorService.saveBatch(dishFlavorList);
        //5. 查看套餐菜品表里面是否有该菜品信息，如果有，需要把该套餐中该菜品名称和price同步修改
        //5.1 创建查询对象
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getDishId,dishDto.getId());
        //5.2 查询执行
        List<SetmealDish> setmealDishList = setmealDishService.list(setmealDishLambdaQueryWrapper);
        //5.3 字段修改
        setmealDishList.forEach(item -> {
            item.setName(dishDto.getName());
            item.setPrice(dishDto.getPrice());
            setmealDishService.updateById(item);
        });
    }

    /**
     * 删除菜品信息
     * 1. 判断id列表中对应的dish是否均处于停售状态，如果有启售的，不允许删除
     * 2. 需要把对应的口味信息一并删除
     * @param ids
     */
    @Override
    public void removeWithFlavors(List<Long> ids) {
        //1. 创造查询对象
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //2. 判断ids内对应的dish对象是否启售
        dishLambdaQueryWrapper.eq(Dish::getStatus,"1").in(Dish::getId,ids);
        //3. 查询符合条件的记录数量
        int count = (int) this.count(dishLambdaQueryWrapper);
        if(count > 0) throw new CustomException("有菜品正在售卖中，不能被删除");
        //4. 如果上述无异常，则删除掉ids对应的dish
        this.removeByIds(ids);
        //5. 删除该dish对应的口味
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.in(DishFlavor::getDishId,ids);
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);
    }

    /**
     * 获取Dto对象
     * 1. 可选的查询条件：姓名
     */
    @Override
    public Page<DishDto> pageForDto(int page, int pageSize, String name) {
        //1. 创建分页对象
        Page<Dish> dishPage = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>(page,pageSize);
        //2. 创建查询条件对象
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //3. 添加查询条件
        lambdaQueryWrapper.like(name!=null,Dish::getName,name);
        lambdaQueryWrapper.orderByDesc(Dish::getUpdateTime); // 根据修改时间做降序
        //3. 进行查询
        this.page(dishPage,lambdaQueryWrapper);
        //4. 对象拷贝
        BeanUtils.copyProperties(dishPage,dishDtoPage);
        //5. 获取当前分页数据的集合
        List<Dish> records = dishPage.getRecords();
        List<DishDto> dtoList = records.stream().map(item -> {
            //1. 创建DishDto对象
            DishDto dishDto = new DishDto();
            //2. 把dish对象中的数据拷贝到dto对象中
            BeanUtils.copyProperties(item,dishDto);
            return dishDto;
        }).collect(Collectors.toList());
        //6. 把整理好的列表存放进dto分页里
        dishDtoPage.setRecords(dtoList);
        return dishDtoPage;
    }

    /**
     * 根据id查询菜品信息
     * 1. 查询dish表，获取dish的基本信息
     * 2. 查询dish_flavor表，获取dish的口味信息
     * 3. 创建一个DishDto对象，将查询到的dish和dishFlavors聚合到该对象中返回
     */
    @Override
    public DishDto getDtoById(Long id) {
        //1. 创建dish查询对象
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Dish::getId,id);
        Dish dish = this.getOne(lambdaQueryWrapper);
        //2. 如果该查询对象存在 则 创建查询对象，查询相关的dish_flavor基本信息
        if(dish != null){
            //2.1 创建dish_flavor查询对象
            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            //2.2 查询该id下的口味列表
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,id);
            List<DishFlavor> dishFlavorList = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
            //3. 创建DishDto对象
            DishDto dishDto = new DishDto();
            //4. 拷贝该对象
            BeanUtils.copyProperties(dish,dishDto);
            //5. 将列表赋值给DishDto对象
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }
        else return null;
    }

    /**
     * 根据分类id获取Dto列表
     * 1. 被停售的商品不允许被此方法查询出来
     * @param categoryId
     * @return
     */
    @Override
    public List<DishDto> getDtoListByCategoryId(Long categoryId) {
        //1. 创造查询条件
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //2. 添加查询条件
        lambdaQueryWrapper.eq(categoryId != null,Dish::getCategoryId,categoryId);
        //   被停售的商品不能被选进套餐中
        lambdaQueryWrapper.eq(Dish::getStatus,1);
        //3. 排序
        lambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
        List<Dish> dishList = this.list(lambdaQueryWrapper);
        //4. 使用for循环补全菜品口味信息
        List<DishDto> dtoList = new ArrayList<>();
        for (Dish item : dishList) {
            //4.1 创建携带口味信息的DishDto对象
            DishDto dishDto = new DishDto();
            //4.2 把菜品对象中的信息拷贝到dishDto对象中
            BeanUtils.copyProperties(item, dishDto);
            //4.3 补全口味信息
            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, item.getId());
            List<DishFlavor> dishFlavorList = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            dtoList.add(dishDto);
        }
        return dtoList;
    }

    /**
     * 批量起售停售的方法
     * 1. 需要查看ids中对应的dish是否被套餐引用，如果被引用，则不允许停售
     * @param status
     * @param ids
     */
    @Override
    public void updateStatus(Integer status, Long[] ids) {
        //1. 如果需要停用，则先判断菜品是否被套餐引用，被引用的菜品不能被停售
        if(status == 0){
            //1. 创建查询对象
            LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
            setmealDishLambdaQueryWrapper.in(SetmealDish::getDishId,ids);
            //2. 查询套餐菜品里的相关数据条数，如果条数大于0,则证明有菜品被套餐所引用，不能执行删除
            int count = (int) setmealDishService.count(setmealDishLambdaQueryWrapper);
            if(count > 0) throw new CustomException("有菜品正在被套餐使用中，不可停售");
        }
        // 如果status为未启用或启用的菜品没有被套餐引用，则允许进行停用操作
        //1. 生成修改对象
        LambdaUpdateWrapper<Dish> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        //2. 利用wrapper对象的set（修改状态字段）和in（在某一集合内）方法构建sql语句
        lambdaUpdateWrapper.set(Dish::getStatus,status).in(Dish::getId,ids);
        //3. 执行修改
        this.update(lambdaUpdateWrapper);
    }

}
