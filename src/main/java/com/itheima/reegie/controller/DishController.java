package com.itheima.reegie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reegie.common.R;
import com.itheima.reegie.dto.DishDto;
import com.itheima.reegie.entity.Category;
import com.itheima.reegie.service.CategoryService;
import com.itheima.reegie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/dish")
@Slf4j
@RestController
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

   /**
           * 分页查询菜品接口
     * 需要在这个地方调用categoryService来补全返回的DTO信息
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        Page<DishDto> dishDtoPage = dishService.pageForDto(page,pageSize,name);
        // 填充必要字段
        dishDtoPage.getRecords().forEach(item -> {
            Long categoryId = item.getCategoryId();
            //4. 根据分类的id获取分类对象
            Category category = categoryService.getById(categoryId);
            if(category != null){
                String categoryName = category.getName();
                item.setCategoryName(categoryName);
            }
        });
        return R.success(dishDtoPage);
    }

    /**
     * 根据id查询菜品信息
     */
    @GetMapping("/{id}")
    public R<DishDto> getDishById(@PathVariable("id") Long id){
        DishDto dishDto = dishService.getDtoById(id);
        if(dishDto != null) return R.success(dishDto);
        else return R.error("获取失败，没有查到该菜品");
    }

    /**
     * 新增接口
     */
    @PostMapping
    public R<String> add(@RequestBody DishDto dishDto){
        // 新增菜品方法， 保存菜品基本信息又要保存菜品口味信息
        dishService.saveWithFlavors(dishDto);
        return R.success("添加菜品成功");
    }

    /**
     * 修改接口
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavors(dishDto);
        return R.success("修改菜品成功");
    }

    /**
     * 删除接口
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        dishService.removeWithFlavors(ids);
        return R.success("测试成功");
    }

    /**
     * 起售停售接口 -- 批量起售停售接口
     */
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable("status") Integer status,Long[] ids){
        dishService.updateStatus(status,ids);
        return R.success("修改成功");
    }

    /**
     * 根据分类的id查询对应分类的菜品信息 - 优化（获取DishDto对象的list）
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Long categoryId){
        List<DishDto> dishDtoList = dishService.getDtoListByCategoryId(categoryId);
        dishDtoList.forEach(item -> {
            //补全分类名称
            Category category = categoryService.getById(categoryId);
            if(category != null){
                String categoryName = category.getName();
                item.setCategoryName(categoryName);
            }
        });
        return R.success(dishDtoList);
    }

}
