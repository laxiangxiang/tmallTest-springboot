package com.example.tmall.dao;

import com.example.tmall.pojo.Product;
import com.example.tmall.pojo.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by LXX on 2019/2/12.
 */
public interface ProductImageDao extends JpaRepository<ProductImage,Integer>{

    List<ProductImage> findByProductAndTypeOrderByIdDesc(Product product,String type);
}
