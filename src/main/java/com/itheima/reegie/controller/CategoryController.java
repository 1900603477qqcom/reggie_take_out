package com.itheima.reegie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reegie.common.R;
import com.itheima.reegie.entity.Category;
import com.itheima.reegie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

/**
 * 新增分类
 * @param category
 * * @return
 * */
  public R<String> save(@RequestBody Category category){
      log.info("category:{}",category);
      categoryService.save(category);
      return R.success("新增分类成功");
  }

/**
 * 分页查询
 * @param page
 * @param pageSize
 * @return
 */
@GetMapping("/page")
    public R<Page> pageR(int page, int pageSize){
//      分页构造器
//       Page<Category> pageInfo = new Page<>(page,pageSize);
////       条件构造器
//    LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
////    添加排序条件，根据sort进行排序
//    queryWrapper.orderByAsc(Category::getSort);
//
////    分页查询
//    categoryService.page(pageInfo,queryWrapper);
//    return R.success(pageInfo);
    //1. 创建分页对象
    Page<Category> categoryPage = new Page<>(page,pageSize);
    //2. 创建查询条件对象
    LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
    lambdaQueryWrapper.orderByAsc(Category::getSort); // 根据排序字段做升序
    lambdaQueryWrapper.orderByDesc(Category::getUpdateTime); // 根据修改时间做降序
    //3. 进行查询
    categoryService.page(categoryPage,lambdaQueryWrapper);
    //4. 返回查询结果
    return R.success(categoryPage);
}

/**
 * 根据id删除分类
 * @param id
 * @return
 */
@DeleteMapping
    public R<String> delete(Long id){
    // 因为MybatisPlus提供的业务逻辑方法无法满足我们的需求，所以我们需要自制逻辑删除方法
    //1. 判断当前分类的id是否关联了菜品，如果有关联，不能被删除，抛出异常，由全局异常处理器处理这个异常信息
    //2. 判断当前分类信息有没有关联套餐信息，如果有关联，不能被删除，抛出自定义异常，由全局异常处理器来处理异常信息
    //3. 如果当前分类既没有关联菜品，又没有关联套餐，可以删除
    log.info("删除分类成功，id为：{}",id);
//    categoryService.removeById(id);
    categoryService.remove(id);
    return R.success("分类信息删除成功");
}
    /**
* 根据id修改分类信息
* @param category
* @return
        */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改分类信息：{}",category);
        categoryService.updateById(category);
        return R.success("修改分类信息成功");
    }
    /**
     * 获取菜品分类列表
     *
     * */
    @GetMapping
    public R<List<Category>> getCategoryByType(Category category){
        //1、创建查询对象
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //2、添加查询条件
        categoryLambdaQueryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        categoryLambdaQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        //3、执行查询操作
        List<Category> categoryList = categoryService.list(categoryLambdaQueryWrapper);
        //4、返回查询结果
        return R.success(categoryList);
    }
    /**
     *根据条件查询分类数据
     *@paramcategory
     *@return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
}
