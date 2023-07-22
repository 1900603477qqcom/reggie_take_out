package com.itheima.reegie.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 此类是一个通用结果类，服务端响应的所有结果最终都会包装成此种类型返回给前端页面
 * 通用返回结果，服务端响应的数据最终都会封装成此对象
 * @param <T>
 */
@Data
public class R<T>{
    private Integer code; //编码：1成功，0和其他数字为失败
    private String msg;  //错误信息
    private T data; //数据
    private Map map = new HashMap(); //动态数据

    /**
     * 成功的话返回
     * 如果业务执行结果为成功, 构建R对象时, 只需要调用success方法; 如果需要返回数据传递
     * object 参数, 如果无需返回, 可以直接传递null。
     * */
    public static <T> R<T> success(T object){
        R<T> r = new R<T>();
        r.data = object;
        r.code = 1;
        return r;
    }
    /**
     * 失败的话返回
     *  如果业务执行结果为失败, 构建R对象时, 只需要调用error方法, 传递错误提示信息即可。
     * */
    public static <T> R<T> error(String msg) {
        R r = new R();
        r.msg = msg;
        r.code = 0;
        return r;
    }
    public R<T> add(String key, Objects value){
        this.map.put(key,value);  //put() 方法将指定的键/值对插入到 HashMap 中。
        return this;
    }
}
