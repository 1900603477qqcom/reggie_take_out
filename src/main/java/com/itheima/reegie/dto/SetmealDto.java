package com.itheima.reegie.dto;

import com.itheima.reegie.entity.Setmeal;
import com.itheima.reegie.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto  extends Setmeal {
    private List<SetmealDish> setmealDishes;//套餐关联的菜品集合
    private String categoryName;//分类名称


}
