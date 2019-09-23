package com.example.itokenwebadmin.interceptor;

import com.example.itokenwebadmin.entity.User;
import com.example.itokenwebadmin.service.consumer.RedisService;
import com.example.itokenwebadmin.utils.CookieUtil;
import com.example.itokenwebadmin.utils.JsonUtil;
import io.micrometer.core.instrument.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class WebAdminInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = CookieUtil.getCookie(request, "token");

        //token为空，一定没有登录
        if (token == null || token.isEmpty()) {
            response.sendRedirect("http://localhost:8503/login?url=http://localhost:8601/login");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        //已登陆状态
        if (user != null) {
            if (modelAndView != null) {
                modelAndView.addObject("user", user);

            }
        }
        //未登录状态
        else {
            String token = CookieUtil.getCookie(request, "token");
            if (token != null && !token.isEmpty()) {
                String loginCode = redisService.get(token);

                if (loginCode != null && !loginCode.isEmpty()) {
                    String json = redisService.get(loginCode);
                    if (json != null && !json.isEmpty()) {
                        //已登录状态，创建局部会话
                        user = JsonUtil.stringToObject(json, User.class);
                        if (modelAndView != null) {
                            modelAndView.addObject("user", user);
                        }
                        request.getSession().setAttribute("user", user);
                    }
                }
            }
        }

        //二次确认是否有用户信息
        if (user == null) {
            response.sendRedirect("http://localhost:8503/login?url=http://localhost:8601/login");

        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
