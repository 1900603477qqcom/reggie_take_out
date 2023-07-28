package com.itheima.reegie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reegie.common.R;
import com.itheima.reegie.entity.Employee;
import com.itheima.reegie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@RestController //标记这个类能映射响应
@RequestMapping("/employee") //映射的响应路径(无请求方法要求)
public class EmployeeController {
    @Autowired //自动注入
    private EmployeeService employeeService;

    /**
     * 员工登录
     *
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")   //映射的响应路径（必须映射Post方法）
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        //1、将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2、根据页面提交的用户名username查询数据库
        /**
         * LambdaQueryWrapper是MyBatis-Plus框架中的一个查询条件构造器，用于简化和优化数据库查询操作。
         * 它提供了一种基于Lambda表达式的方式来构建查询条件，使得查询语句更加直观、易于理解和维护。
         * 使用LambdaQueryWrapper，你可以通过链式调用方法来构建查询条件，而无需手动编写SQL语句。
         * 它支持的方法包括等值条件、模糊查询、范围查询、排序、分页等，可以满足大多数常见的查询需求。
         * */
        //2.1 创建查询对象
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //2.2 添加过滤查询条件，根据用户名查询用户对象
        //select * from employee where username = ?
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //3、如果没有查询到用户名则返回登录失败的结果
        if (emp == null) {
            return R.error("用户名不存在，登录失败");
        }
        //4、如果查询到用户名，但是密码比对不一致则返回登录失败结果
        if (!emp.getPassword().equals(password)) {
            return R.error("密码错误，登录失败");
        }
        //5、查看员工状态，如果为禁用状态则返回员工账号已被禁用结果
        if (emp.getStatus() == 0) {
            return R.error("账号已被禁用");
        }
        //6. 启用状态，保存当前登录的用户信息到session中
        HttpSession session = request.getSession();
        session.setAttribute("employee",emp);
        //7. 返回登录成功
        return R.success(emp);
    }

    /**
     * 员工退出
     *
     * @param session
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpSession session) {
        //清理Session中保存的当前登录员工的id
        session.removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 新增员工
     *
     * @param employee
     * @return
     */
    @PostMapping("/add")
    public R<String> save(HttpSession session, @RequestBody Employee employee) {
        log.info("新增员工，员工信息：{}", employee.toString());

        //1. 补全用户信息
        //1.1 补全密码 默认初始密码123456
        String password = DigestUtils.md5DigestAsHex("123456".getBytes());
        employee.setPassword(password);

//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//
//        //获取当前登录用户id
//        Long empId = (Long) request.getSession().getAttribute("employee");
//
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);
//2. 保存当前用户信息到数据库
        employeeService.save(employee);
        //3. 返回保存成功信息到前端页面
        return R.success("新增员工成功");
    }

    /**
     * 员工信息分页查询
     *
     * @param page     当前查询页码
     * @param pageSize 每页展示记录数
     * @param name     员工姓名 - 可选参数
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page = {},pageSize = {},name = {}", page, pageSize, name);
        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name != null, Employee::getName, name);   // 模糊查询
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);// 根据修改时间进行升序排序
        //执行查询
        employeeService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 根据id修改员工信息
     *
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        //     获取当前线程id
        long id = Thread.currentThread().getId();
        log.info("线程id为：{}",id);

        log.info(employee.toString());
//        Long empId = (Long) request.getSession().getAttribute("employee");

//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(empId);
        //2. 执行修改操作
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    /**
     * 根据id查询员工信息
     *
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public R<Employee> getById(@PathVariable Long id) {
        //1. 根据id查询员工信息
        Employee employee = employeeService.getById(id);
        //2. 对返回对象进行校验，判断对象是否为空
        if(employee != null) return R.success(employee);
        else return R.error("没有查询到对应的员工信息");

    }


}