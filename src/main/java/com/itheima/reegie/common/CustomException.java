package com.itheima.reegie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 自定义业务异常类
 * 在业务逻辑操作过程中,如果遇到一些业务参数、操作异常的情况下，我们直接抛出此异常
 */
@Slf4j
public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }

    /**
     * 异常处理方法
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex){
        log.error(ex.getMessage());
        return R.error(ex.getMessage());
    }
}
