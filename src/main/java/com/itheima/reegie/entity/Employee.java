package com.itheima.reegie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 对应数据库里面的employee表的字段以及字段的类型
 */
@Data  //所有的属性自动加上get、set方法，也会自动重写toString方法
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String name;
    private String password;
    private String phone;
    private String sex;
    private String idNumber;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    /**
     *  @TableField的作用
     * 在进行插入操作时对
     * 添加了注解@TableField(fill = FieldFill.INSERT)
     * 的字段进行自动填充。
     * 对添加了注解@TableField(fill = FieldFill.INSERT_UPDATE)
     * 的字段在进行插入和更新时进行自动填充。
     */

    //@TableField用于自动填入时数据库字段和属性的映射
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;  //创建者id
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;  //修改者的id




}
