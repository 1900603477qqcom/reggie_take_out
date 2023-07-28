package com.itheima.reegie.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reegie.common.BaseContext;
import com.itheima.reegie.common.R;
import com.itheima.reegie.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 拦截器类，
 * */
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //1. 获取本次请求的url
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();// /backend/index.html
        log.info("拦截到请求：{}", requestURI);
        //     获取当前线程id
        long id = Thread.currentThread().getId();
        log.info("线程id为：{}",id);
        //2. 设置需要方法路径定义不需要处理的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**"
        };
        //3. 判断本次请求是否需要放行
        boolean check = check(urls, requestURI);

        //4. 检验是否需要放行
        if (check) {
            log.info("本次请求{}不需要处理", requestURI);
            filterChain.doFilter(request, response);
            Employee employee = (Employee) request.getSession().getAttribute("employee");
            BaseContext. setCurrentId(employee.getId());
        }
         //5. 不符合放行规则，判断当前员工是否登录如果已登录，则直接放行
        else if (request.getSession().getAttribute("employee") != null) {
            log.info("用户已登录，用户ID为：{}", request.getSession().getAttribute("employee"));
//            获取HttpSession中的登 录用户信息, 调用BaseContext的setCurrentId方法将当前登录用户ID存入ThreadLocal。(用于改造成动态获取当前登录用户的id。)
            Employee employee = (Employee) request.getSession().getAttribute("employee");
            BaseContext. setCurrentId(employee.getId());
            filterChain.doFilter(request,response);

        }else
            response.getWriter().write(JSON.toJSONString(R.error("NOT LOGIN")));
//        //6. 不符合放行规则，判断当前用户是否登录
//        else if(request.getSession().getAttribute("user") != null){
//            log.info("当前用户已经登录可以放行"+request.getSession().getAttribute("user").toString());
//            User user = (User) request.getSession().getAttribute("user");
//            BaseContext.setThreadLocalId(user.getId());
//            filterChain.doFilter(request,response);
//        }
//        log.info("用户未登录");
        //5、如果用户未登录则返回未登录结果，通过输出流方式向客户端页面响应数据

    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     *
     * @param urls
     * @param requestURI
     * @return
     */
    private boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match){
                return true;
            }
        }
        return false;
    }
}
