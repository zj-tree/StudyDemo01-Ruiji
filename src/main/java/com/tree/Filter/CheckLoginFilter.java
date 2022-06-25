package com.tree.Filter;

import com.alibaba.fastjson2.JSON;
import com.tree.Common.BaseContext;
import com.tree.Common.R;
import com.tree.Entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Auther: 来两碗米饭
 * @ClassName：CheckLoginFiter
 * @Date: 2022/6/19 18:30
 * @Description TODO:登录检查过滤器
 */
@WebFilter(filterName = "checkLoginFilter",urlPatterns = "/*")
@Slf4j
public class CheckLoginFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();
        log.info(requestURI);
        //放行的url
        String[] urls = {
                "/backend/**",
                "/front/**",
                "/employee/login",
                "/employee/logout",
                "/user/sendMsg",
                "/user/login",

        };

        //判断是否是可以放行的
        boolean check = check(urls, requestURI);
        if (check){
            filterChain.doFilter(request,response);
            return;
        }


        //判断用户是否登录
        if (request.getSession().getAttribute("employee") != null){
            Long employee = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(employee);

            filterChain.doFilter(request,response);
            return;
        }

        //判断用户是否登录
        if (request.getSession().getAttribute("user") != null){
            long user = (long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(user);
            log.info(String.valueOf(user));
            filterChain.doFilter(request,response);
            return;
        }

        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    public boolean check(String [] urls,String url){
        boolean flag = false;
        for (String s : urls) {
            boolean match = PATH_MATCHER.match(s,url);
            if (match){
                flag = true;
            }
        }
        return flag;
    }
}
