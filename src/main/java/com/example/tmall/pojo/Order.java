package com.example.tmall.pojo;

import com.example.tmall.service.OrderService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by LXX on 2019/2/12.
 */
@Data
@Table(name = "order_")
@Entity
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    private String orderCode;

    private String address;

    private String receiver;

    private String mobile;

    private String userMessage;

    private Date createDate;

    private Date payDate;

    private Date deliveryDate;

    private Date confirmDate;

    private String status;

    @Transient
    private List<OrderItem> orderItems;

    @Transient
    private float total;

    @Transient
    private int totalNumber;

    @Transient
    private String statusDesc;

    public String getStatusDesc(){
        if (null != statusDesc)
            return statusDesc;
        String desc = "未知";
        switch (desc){
            case OrderService.waitPay:
                desc = "待付";
                break;
            case OrderService.waiteDelivery:
                desc = "待发";
                break;
            case OrderService.waitConfirm:
                desc = "待收";
                break;
            case OrderService.waitReview:
                desc = "待评价";
                break;
            case OrderService.finish:
                desc = "完成";
                break;
            case OrderService.delete:
                desc = "删除";
                break;
            default:
                desc = "未知";
        }
        statusDesc = desc;
        return statusDesc;
    }
}
