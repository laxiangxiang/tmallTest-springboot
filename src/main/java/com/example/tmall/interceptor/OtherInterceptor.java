package com.example.tmall.interceptor;

import com.example.tmall.pojo.Category;
import com.example.tmall.pojo.OrderItem;
import com.example.tmall.pojo.User;
import com.example.tmall.service.CategoryService;
import com.example.tmall.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by LXX on 2019/2/19.
 */
public class OtherInterceptor implements HandlerInterceptor{

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private CategoryService categoryService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        int cartTotalItemNumber = 0;
        if (null != user){
            List<OrderItem> orderItems = orderItemService.listByUser(user);
            for (OrderItem orderItem : orderItems) {
                cartTotalItemNumber += orderItem.getNumber();
            }
        }
        List<Category> categories = categoryService.list();
        String contextPath = session.getServletContext().getContextPath();
        request.getServletContext().setAttribute("categories_below_search",categories);
        session.setAttribute("cartTotalItemNumber",cartTotalItemNumber);
        request.getServletContext().setAttribute("contextPath",contextPath);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {

    }
}
