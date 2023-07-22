package com.itheima.reegie.controller;

import com.itheima.reegie.common.R;
import com.itheima.reegie.dto.SetmealDto;
import com.itheima.reegie.service.SetmealDishService;
import com.itheima.reegie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;

    /***
     *新增套餐
     */
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info("套餐信息：（）",setmealDto);

        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }
}
