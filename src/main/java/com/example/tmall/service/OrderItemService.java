package com.example.tmall.service;

import com.example.tmall.dao.OrderItemDao;
import com.example.tmall.pojo.Order;
import com.example.tmall.pojo.OrderItem;
import com.example.tmall.pojo.Product;
import com.example.tmall.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by LXX on 2019/2/12.
 */
@Service
@CacheConfig(cacheNames = "orderItems")
public class OrderItemService {

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private ProductImageService productImageService;

    public void fill(List<Order> orders){
        orders.forEach(order -> fill(order));
    }

    public void fill(Order order){
        List<OrderItem> orderItems = listByOrder(order);
        float total = 0;
        int totalNumber = 0;
        for (OrderItem oi :orderItems) {
            total+=oi.getNumber()*oi.getProduct().getPromotePrice();
            totalNumber+=oi.getNumber();
            productImageService.setFirstProductImage(oi.getProduct());
        }
        order.setTotal(total);
        order.setOrderItems(orderItems);
        order.setTotalNumber(totalNumber);
    }

    @Cacheable(key = "'orderItems-oid-'+#p0.id")
    public List<OrderItem> listByOrder(Order order){
        return orderItemDao.findByOrderOrderByIdDesc(order);
    }

    public int getSaleCount(Product product){
        List<OrderItem> orderItems = listByProduct(product);
        int result = 0;
        for (OrderItem orderItem : orderItems) {
            if (null != orderItem.getOrder()){
                if (null != orderItem.getOrder() && null != orderItem.getOrder().getPayDate())
                    result += orderItem.getNumber();
            }
        }
        return result;
    }

    @Cacheable(key = "'orderItems-pid-'+#p0.id")
    public List<OrderItem> listByProduct(Product product){
        return orderItemDao.findByProduct(product);
    }

    @Cacheable(key = "'orderItems-uid-'+#p0.id")
    public List<OrderItem> listByUser(User user){
        return orderItemDao.findByUserAndOrderIsNull(user);
    }

    @CacheEvict(allEntries = true)
    public void update(OrderItem orderItem){
        orderItemDao.save(orderItem);
    }

    @CacheEvict(allEntries = true)
    public void add(OrderItem orderItem){
        orderItemDao.save(orderItem);
    }

    @Cacheable(key = "'orderItem-one-'+#p0")
    public OrderItem get(int id){
        return orderItemDao.getOne(id);
    }

    @CacheEvict(allEntries = true)
    public void delete(int id){
        orderItemDao.deleteById(id);
    }
}
