package com.itheima.reegie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reegie.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper  //用于标记该接口是继承BaseMapper接口的
public interface CategoryMapper extends BaseMapper<Category> {

}
