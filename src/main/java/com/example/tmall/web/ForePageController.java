package com.example.tmall.web;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

/**
 * Created by LXX on 2019/2/13.
 */
@Controller
public class ForePageController {

    @GetMapping("/")
    public String index(){
        return "redirect:home";
    }

    @GetMapping("/home")
    public String home(){
        return "fore/home";
    }

    @GetMapping("/register")
    public String register(){
        return "fore/register";
    }

    @GetMapping("/registerSuccess")
    public String registerSuccess(){
        return "fore/registerSuccess";
    }

    @GetMapping("/login")
    public String login(){
        return "fore/login";
    }

    @GetMapping("/forelogout")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()){
            subject.logout();
        }
        return "redirect:home";
    }

    @GetMapping("/product")
    public String product(){
        return "fore/product";
    }

    @GetMapping("/category")
    public String category(){
        return "fore/category";
    }

    @GetMapping("/search")
    public String search(){
        return "fore/search";
    }

    @GetMapping("/buy")
    public String buy(){
        return "fore/buy";
    }

    @GetMapping("/cart")
    public String cart(){
        return "fore/cart";
    }

    @GetMapping("/alipay")
    public String alipay(){
        return "fore/alipay";
    }

    @GetMapping("/payed")
    public String payed(){
        return "fore/payed";
    }

    @GetMapping("/bought")
    public String bought(){
        return "fore/bought";
    }

    @GetMapping("/confirmPay")
    public String confirmPay(){
        return "fore/confirmPay";
    }

    @GetMapping("/orderConfirmed")
    public String orderConfirmed(){
        return "fore/orderConfirmed";
    }

    @GetMapping("/review")
    public String review(){
        return "fore/review";
    }
}
