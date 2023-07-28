package com.itheima.reegie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reegie.entity.Employee;
import com.itheima.reegie.mapper.EmployeeMapper;
import com.itheima.reegie.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * 实现了EmployeeService接口
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
