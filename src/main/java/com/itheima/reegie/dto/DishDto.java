package com.itheima.reegie.dto;

import com.itheima.reegie.entity.Dish;
import com.itheima.reegie.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {
    // 口味集合
    private List<DishFlavor> flavors = new ArrayList<>();
    //分类的名称
    private String categoryName;
    // 菜品份数
    private Integer copies;
}
