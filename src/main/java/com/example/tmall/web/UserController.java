package com.example.tmall.web;

import com.example.tmall.pojo.User;
import com.example.tmall.service.UserService;
import com.example.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by LXX on 2019/2/12.
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public Page4Navigator<User> list(
            @RequestParam(name = "start",value = "start",defaultValue = "0")int start,
            @RequestParam(name = "size",value = "size",defaultValue = "5")int size){
        start = start < 0 ? 0 : start;
        Page4Navigator<User> page4Navigator = userService.list(start,size,5);
        return page4Navigator;
    }
}
