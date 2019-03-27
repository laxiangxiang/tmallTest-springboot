package com.example.tmall.web;

import com.example.tmall.pojo.*;
import com.example.tmall.service.*;
import com.example.tmall.util.Result;
import com.example.tmall.util.comparator.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by LXX on 2019/2/13.
 */
@RestController
public class ForeRESTController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductImageService productImageService;

    @Autowired
    private PropertyValueService propertyValueService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/forehome")
    public Object home(){
        List<Category> cs = categoryService.list();
        productService.fill(cs);
        productService.fillByRow(cs);
        categoryService.removeCategoryFromProduct(cs);
        return cs;
    }

    @PostMapping("foreregister")
    public Result register(@RequestBody User user){
        String name = user.getName();
        String password = user.getPassword();
//        什么要用 HtmlUtils.htmlEscape？
// 因为有些同学在恶意注册的时候，会使用诸如 <script>alert('papapa')</script> 这样的名称，会导致网页打开就弹出一个对话框。 那么在转义之后，就没有这个问题了。
        name = HtmlUtils.htmlEscape(name);
        user.setName(name);
        boolean exist = userService.isExist(name);
        if (exist){
            String message = "用户名已经被使用";
            return Result.fail(message);
        }
//        user.setPassword(password);
//        userService.add(user);
//        return Result.success();
        ////////////////使用Shiro///////////////
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        String algorithmName = "md5";
        //会通过随机方式创建盐， 并且加密算法采用 "md5", 除此之外还会进行 2次加密
        String encodedPassword = new SimpleHash(algorithmName,password,salt,times).toString();
        user.setSalt(salt);
        user.setPassword(encodedPassword);
        userService.add(user);
        return Result.success();
    }

    @PostMapping("forelogin")
    public Result login(@RequestBody User user, HttpSession session){
        String name = user.getName();
        name = HtmlUtils.htmlEscape(name);
//        User user1 = userService.get(name,user.getPassword());
//        if (null == user1){
//            String message = "账号密码错误";
//            return Result.fail(message);
//        }else {
//            session.setAttribute("user",user1);
//            return Result.success();
//        }
        ///////////////使用Shiro验证登录//////////////
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(name,user.getPassword());
        try {
            subject.login(token);
            User user1 = userService.getByName(name);
//            subject.getSession().setAttribute("user",user1);
            session.setAttribute("user",user1);
            return Result.success();
        }catch (AuthenticationException e){
            String message = "账号密码错误";
            return Result.fail(message);
        }
    }

    @GetMapping("/foreproduct/{pid}")
    public Result product(@PathVariable(name = "pid")int pid){
        Product product = productService.get(pid);
        List<ProductImage> productSingleImages = productImageService.listSingleProductImages(product);
        List<ProductImage> productDetailImages = productImageService.listDetailProductImages(product);
        product.setProductSingleImages(productSingleImages);
        product.setProductDetailImages(productDetailImages);
        List<PropertyValue> pvs = propertyValueService.list(product);
        List<Review> reviews = reviewService.list(product);
        productService.setSaleAndReviewNumber(product);
        productImageService.setFirstProductImage(product);
        Map<String,Object> map = new HashMap<>();
        map.put("product",product);
        map.put("pvs",pvs);
        map.put("reviews",reviews);
        return Result.success(map);
    }

    @GetMapping("forecheckLogin")
    public Result checkLogin(HttpSession session){
//        User user =(User) session.getAttribute("user");
//        if (null != user)
//            return Result.success();
//        else
//            return Result.fail("未登录");
        ////使用Shiro方式验证是否登录//////////
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()){
            return Result.success();
        }else return Result.fail("未登录");
    }

    @GetMapping("forecategory/{cid}")
    public Category category(@PathVariable(name = "cid")int cid,String sort){
        Category category = categoryService.get(cid);
        productService.fill(category);
        productService.setSaleAndReviewNumber(category.getProducts());
        categoryService.removeCategoryFromProduct(category);
        if (null != sort){
            switch (sort){
                case "review":
                    Collections.sort(category.getProducts(),new ProductReviewComparator());
                    break;
                case "date":
                    Collections.sort(category.getProducts(),new ProductDateComparator());
                    break;
                case "saleCount":
                    Collections.sort(category.getProducts(),new ProductSaleCountComparator());
                    break;
                case "price":
                    Collections.sort(category.getProducts(),new ProductPriceComparator());
                    break;
                case "all":
                    Collections.sort(category.getProducts(),new ProductAllComparator());
                    break;
            }
        }
        return category;
    }

    @PostMapping("/foresearch")
    public List<Product> searchProducts(String keyword){
        if (null == keyword)
            keyword = "";
        List<Product> products = productService.search(keyword,0,20);
        productImageService.setFirstProductImages(products);
        productService.setSaleAndReviewNumber(products);
        return products;
    }

    @GetMapping("forebuyone")
    public int buyOne(@RequestParam(name = "pid")int pid,
                         @RequestParam(name = "num")int num,
                         HttpSession session){
        return buyOneAndAddCart(pid,num,session);
    }

    private int buyOneAndAddCart(int pid, int num, HttpSession session){
        User user  = (User) session.getAttribute("user");
        Product product = productService.get(pid);
        int oiid = 0;
        List<OrderItem> orderItems = orderItemService.listByUser(user);
        boolean found = false;
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getProduct().getId() == pid){
                orderItem.setNumber(orderItem.getNumber()+num);
                orderItemService.update(orderItem);
                found = true;
                oiid = orderItem.getId();
                break;
            }
        }
        if (!found){
            OrderItem oi = new OrderItem();
            oi.setUser(user);
            oi.setProduct(product);
            oi.setNumber(num);
            orderItemService.add(oi);
            oiid = oi.getId();
        }
        return oiid;
    }
    @GetMapping("/forebuy")
    public Result buy(String[] oiid,HttpSession session){
        List<OrderItem> orderItems = new ArrayList<>();
        float total = 0;
        for (String s : oiid) {
            int id = Integer.parseInt(s);
            OrderItem orderItem = orderItemService.get(id);
            total += orderItem.getProduct().getPromotePrice()*orderItem.getNumber();
            orderItems.add(orderItem);
        }
        productImageService.setFirstProductImagesOnOrderItems(orderItems);
        session.setAttribute("ois",orderItems);
        Map<String,Object> map = new HashMap<>();
        map.put("orderItems",orderItems);
        map.put("total",total);
        return Result.success(map);
    }

    @GetMapping("/foreaddCart")
    public Result addCart(int pid,int num,HttpSession session){
        buyOneAndAddCart(pid, num, session);
        return Result.success();
    }

    @GetMapping("/forecart")
    public Object cart(HttpSession session){
        User user = (User)session.getAttribute("user");
        List<OrderItem> orderItems = orderItemService.listByUser(user);
        productImageService.setFirstProductImagesOnOrderItems(orderItems);
        return orderItems;
    }

    @GetMapping("foredeleteOrderItem")
    public Result deleteOrderItem(@RequestParam(name = "oiid")int oiid,HttpSession session){
        User user = (User) session.getAttribute("user");
        if (null == user){
            return Result.fail("未登录");
        }
        OrderItem orderItem = orderItemService.get(oiid);
        orderItemService.delete(oiid);
        return Result.success();
    }

    @GetMapping("/forechangeOrderItem")
    public Result changeOrderItem(@RequestParam(name = "pid")int pid,@RequestParam(name = "num")int num,HttpSession session){
        User user = (User) session.getAttribute("user");
        if (null == user){
            return Result.fail("未登录");
        }
        List<OrderItem> orderItems = orderItemService.listByUser(user);
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getProduct().getId() == pid){
                orderItem.setNumber(num);
                orderItemService.update(orderItem);
                break;
            }
        }
        return Result.success();
    }

    @PostMapping("forecreateOrder")
    public Object createOrder(@RequestBody Order order,HttpSession session){
        User user = (User)session.getAttribute("user");
        if (null == user){
            return Result.fail("未登录");
        }
        String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+new Random().nextInt(1000);
        order.setOrderCode(orderCode);
        order.setCreateDate(new Date());
        order.setUser(user);
        order.setStatus(OrderService.waitPay);
        List<OrderItem> orderItems = (List<OrderItem>)session.getAttribute("ois");
        float total = orderService.add(order,orderItems);
        Map<String,Object> map = new HashMap<>();
        map.put("oid",order.getId());
        map.put("total",total);
        return Result.success(map);
    }

    @GetMapping("forepayed")
    public Object payed(int oid){
        Order order = orderService.get(oid);
        order.setStatus(OrderService.waiteDelivery);
        order.setPayDate(new Date());
        orderService.update(order);
        return order;
    }

    @GetMapping("forebought")
    public Object bought(HttpSession session){
        User user = (User)session.getAttribute("user");
        if (null == user){
            return Result.fail("未登录");
        }
        List<Order> orders = orderService.listByUserWithoutDelete(user);
        orderService.removeOrderFromOrderItem(orders);
        return orders;
    }

    @GetMapping("foreconfirmPay")
    public Object confirmPay(int oid){
        Order order = orderService.get(oid);
        orderItemService.fill(order);
        orderService.cacl(order);
        orderService.removeOrderFromOrderItem(order);
        return order;
    }

    @GetMapping("foreorderConfirmed")
    public Object orderConfirmed(int oid){
        Order order = orderService.get(oid);
        order.setStatus(OrderService.waitReview);
        order.setConfirmDate(new Date());
        orderService.update(order);
        return Result.success();
    }

    @GetMapping("foredeleteOrder")
    public Object deleteOrder(int oid){
        Order o = orderService.get(oid);
        o.setStatus(OrderService.delete);
        orderService.update(o);
        return Result.success();
    }

    @GetMapping("forereview")
    public Object review(int oid){
        Order order = orderService.get(oid);
        orderItemService.fill(order);
        orderService.removeOrderFromOrderItem(order);
        Product product = order.getOrderItems().get(0).getProduct();
        List<Review> reviews = reviewService.list(product);
        productService.setSaleAndReviewNumber(product);
        Map<String,Object> map = new HashMap<>();
        map.put("p",product);
        map.put("o",order);
        map.put("reviews",reviews);
        return Result.success(map);
    }

    @PostMapping("/foredoreview")
    public Object doreview(HttpSession session,int oid,int pid,String content){
        Order order = orderService.get(oid);
        order.setStatus(OrderService.finish);
        orderService.update(order);
        Product p = productService.get(pid);
        content = HtmlUtils.htmlEscape(content);
        User user = (User)session.getAttribute("user");
        Review review = new Review();
        review.setContent(content);
        review.setProduct(p);
        review.setCreateDate(new Date());
        review.setUser(user);
        reviewService.add(review);
        return Result.success();
    }
}
