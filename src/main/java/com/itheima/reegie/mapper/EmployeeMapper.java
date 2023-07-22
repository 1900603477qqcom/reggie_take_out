package com.itheima.reegie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reegie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用来继承BaseMapper，然后用mybatisplus里面实现类的sql语句方法
 * */
@Mapper  //用于标记该接口是继承BaseMapper接口的
public interface EmployeeMapper extends BaseMapper<Employee> {

}
