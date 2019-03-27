package com.example.tmall.service;

import com.example.tmall.dao.OrderDao;
import com.example.tmall.dao.OrderItemDao;
import com.example.tmall.pojo.Order;
import com.example.tmall.pojo.OrderItem;
import com.example.tmall.pojo.User;
import com.example.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by LXX on 2019/2/12.
 */
@Service
@CacheConfig(cacheNames = "orders")
public class OrderService {

    public static final String waitPay = "waitPay";

    public static final String waiteDelivery = "waitDelivery";

    public static final String waitConfirm = "waitConfirm";

    public static final String waitReview = "waitReview";

    public static final String finish = "finish";

    public static final String delete = "delete";

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemService orderItemService;

    @Cacheable(key = "'orders-page-'+#p0+'-'+#p1")
    public Page4Navigator<Order> list(int start,int size,int navigatePage){
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = PageRequest.of(start,size,sort);
        Page page = orderDao.findAll(pageable);
        return new Page4Navigator<>(page,navigatePage);
    }

    /**
     * 在把一个Order转换为json的同时，会把其对应的 orderItems 转换为 json数组，
     * 而 orderItem对象上有 order属性， 这个order 属性又会被转换为 json对象，
     * 然后这个 order 下又有 orderItems 。。。
     * 就这样就会产生无穷递归，系统就会报错了。
     * 所以这里采用 removeOrderFromOrderItem 把 OrderItem的order设置为空就可以了。
     * 那么为什么不用 @JsonIgnoreProperties 来标记这个字段呢？
     * 因为后续我们要整合Redis，如果标记成了 @JsonIgnoreProperties 会在和 Redis 整合的时候有 Bug,
     * 所以还是采用这种方式比较好。
     * @param orders
     */
    public void removeOrderFromOrderItem(List<Order> orders){
        orders.forEach(order -> removeOrderFromOrderItem(order));
    }

    public void removeOrderFromOrderItem(Order order){
        List<OrderItem> orderItems = order.getOrderItems();
        orderItems.forEach(orderItem -> orderItem.setOrder(null));
    }

    @Cacheable(key = "'order-one-'+#p0")
    public Order get(int id){
        return orderDao.getOne(id);
    }

    @CacheEvict(allEntries = true)
    public void update(Order order){
        orderDao.save(order);
    }

    @CacheEvict(allEntries = true)
    @Transactional(propagation = Propagation.REQUIRED,rollbackForClassName ="Exception" )
    public float add(Order order,List<OrderItem> orderItems){
        float total = 0;
        add(order);
        if (false){
            throw  new RuntimeException();
        }
        for (OrderItem orderItem : orderItems){
            orderItem.setOrder(order);
            orderItemService.update(orderItem);
            total += orderItem.getProduct().getPromotePrice()*orderItem.getNumber();
        }
        return total;
    }
    @CacheEvict(allEntries = true)
    public void add(Order order){
        orderDao.save(order);
    }

    public List<Order> listByUserWithoutDelete(User user){
        List<Order> orders = listByUserAndNotDeleted(user);
        orderItemService.fill(orders);
        return orders;
    }

    @Cacheable(key = "'orders-uid-'+#p0.id")
    private List<Order> listByUserAndNotDeleted(User user) {
        return orderDao.findByUserAndStatusNotOrderByIdDesc(user,OrderService.delete);
    }

    public void cacl(Order order){
        List<OrderItem> orderItems = order.getOrderItems();
        float total = 0;
        for (OrderItem orderItem : orderItems){
            total += orderItem.getProduct().getPromotePrice()*orderItem.getNumber();
        }
        order.setTotal(total);
    }
}