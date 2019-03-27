package com.example.tmall.interceptor;

import com.example.tmall.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by LXX on 2019/2/19.
 */
public class LoginInterceptor implements HandlerInterceptor{

    /**
     * 进入servlet执行Servlet方法之前执行
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        String contextPath = session.getServletContext().getContextPath();
        String[] requireAuthPages = new String[]{
                "buy",
                "alipay",
                "payed",
                "cart",
                "bought",
                "confirmPay",
                "orderConfirmed",
                "forebuyone",
                "forebuy",
                "foreaddCart",
                "forecart",
                "forechangeOrderItem",
                "foredeleteOrderItem",
                "forecreateOrder",
                "forepayed",
                "forebought",
                "foreconfirmPay",
                "foreorderConfirmed",
                "foredeleteOrder",
                "forereview",
                "foredoreview"
        };
        String uri = request.getRequestURI();
        uri = uri.replaceAll(contextPath+"/","");
        String page = uri;
        if (beginWith(page,requireAuthPages)){
//            User user = (User)session.getAttribute("user");
//            if (user == null){
//                response.sendRedirect("login");
//                return false;
//            }
            ////使用Shiro验证是否登录//////////
            Subject subject = SecurityUtils.getSubject();
            if (!subject.isAuthenticated()){
                response.sendRedirect("login");
                return false;
            }
        }
        return true;
    }

    private boolean beginWith(String page,String[] requiredAuthPage){
        boolean result = false;
        for (String s : requiredAuthPage) {
            if (page.startsWith(s)){
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * 进入Servlet，在Servlet方法中的代码执行之后执行
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

    }

    /**
     * servlet返回视图或者数据之后执行
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {

    }
}
