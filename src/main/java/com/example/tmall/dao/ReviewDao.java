package com.example.tmall.dao;

import com.example.tmall.pojo.Product;
import com.example.tmall.pojo.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by LXX on 2019/2/15.
 */
public interface ReviewDao extends JpaRepository<Review,Integer>{
    List<Review> findByProductOrderByIdDesc(Product product);
    int countByProduct(Product product);
}
