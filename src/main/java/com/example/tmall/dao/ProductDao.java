package com.example.tmall.dao;

import com.example.tmall.pojo.Category;
import com.example.tmall.pojo.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by LXX on 2019/1/29.
 */
public interface ProductDao extends JpaRepository<Product,Integer>{

    Page<Product> findByCategory(Category category, Pageable pageable);

    List<Product> findByCategory(Category category);
    List<Product> findByNameLike(String keyword,Pageable pageable);
    List<Product> findByNameLike(String keyword);
}
