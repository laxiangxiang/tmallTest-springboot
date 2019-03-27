package com.example.tmall.web;

import com.example.tmall.pojo.Order;
import com.example.tmall.pojo.OrderItem;
import com.example.tmall.service.OrderItemService;
import com.example.tmall.service.OrderService;
import com.example.tmall.util.Page4Navigator;
import com.example.tmall.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Created by LXX on 2019/2/13.
 */
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @GetMapping("/orders")
    public Page4Navigator<Order> list(
            @RequestParam(name = "start",value = "start",defaultValue = "0")int start,
            @RequestParam(name = "size",value = "size",defaultValue = "5")int size){
        start = start < 0? 0:start;
        Page4Navigator page4Navigator = orderService.list(start,size,5);
        orderItemService.fill(page4Navigator.getContent());
        orderService.removeOrderFromOrderItem(page4Navigator.getContent());
        return page4Navigator;
    }

    @PutMapping("deliveryOrder/{id}")
    public Object deliveryOrder(@PathVariable int id){
        Order o = orderService.get(id);
        o.setDeliveryDate(new Date());
        o.setStatus(OrderService.waitConfirm);
        orderService.update(o);
        return Result.success();
    }
}
