package com.example.tmall.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by LXX on 2019/1/29.
 */
@Data
@Entity
@Table(name = "product")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
//用于告诉 es 如何进行匹配，indexName相当于数据库，type相当于表
@Document(indexName = "my_tmall_springboot",type = "product")
public class Product implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private String name;

    private String subTitle;

    private float originalPrice;

    private float promotePrice;

    private int stock;

    private Date createDate;

    @Transient
    private ProductImage firstProductImage;

    //单个产品图片集合
    @Transient
    private List<ProductImage> productSingleImages;

    //详情产品图片集合
    @Transient
    private List<ProductImage> productDetailImages;

    //销量
    @Transient
    private int saleCount;

    //累计评价
    @Transient
    private int reviewCount;
}
