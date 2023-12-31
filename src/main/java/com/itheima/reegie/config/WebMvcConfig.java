package com.itheima.reegie.config;

import com.itheima.reegie.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * 设置静态资源映射
 * */

@Slf4j
@Configuration  //标记这个类是被spring管理的bean（配置类）
public class WebMvcConfig extends WebMvcConfigurationSupport {

    //配置OrderedHiddenHttpMethodFilter
//    @Bean
//    public OrderedHiddenHttpMethodFilter hiddenHttpMethodFilter() {
//        return new OrderedHiddenHttpMethodFilter();
//    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始进行静态资源映射");
        // 后端管理静态资源放行
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        // 前台静态资源放行
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }

    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换器···");
//        创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        // 设置对象转换器 底层使用我们自己定义的JacksonObjectMapper对象，将java对象转换成json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        // 将上面的消息转换器对象追加到MVC框架的消息转换器集合中
        converters.add(0,messageConverter);
    }
}
