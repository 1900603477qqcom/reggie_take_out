package com.itheima.reegie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 异常处理方法
 * @return
 *
 * @ControllerAdvice : 指定拦截那些类型的控制器;
 * @ResponseBody: 将方法的返回值 R 对象转换为json格式的数据, 响应给页面;
 * 上述使用的两个注解, 也可以合并成为一个注解 @RestControllerAdvice
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.info(ex.getMessage());
        if (ex.getMessage().contains("Duplicate entry")){
            String[] sqlit = ex.getMessage().split(" ");
            String msg = sqlit[2]+"已存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }
}
