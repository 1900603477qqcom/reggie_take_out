package com.itheima.reegie;


import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@Slf4j
@SpringBootApplication
//@ServletComponentScan	// 2.4版本以下需要此注解
//@MapperScan(basePackages = {"com.itheima.mapper"})	// mybatis配置mapper包
/***
 * /@ServletComponentScan 的作用:
 * 在SpringBoot项目中, 在引导类/配置类上加了该注解后, 会自动扫描项目中(当前包及
 * 其子包下)的@WebServlet , @WebFilter , @WebListener 注解, 自动注册Servlet的
 *   关组件 ;
 */

public class ReggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class,args);
        log.info("项目启动.....");
    }
}
