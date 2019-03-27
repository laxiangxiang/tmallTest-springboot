package com.example.tmall.dao;

import com.example.tmall.pojo.Product;
import com.example.tmall.pojo.Property;
import com.example.tmall.pojo.PropertyValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by LXX on 2019/1/29.
 */
public interface PropertyValueDao extends JpaRepository<PropertyValue,Integer>{

    List<PropertyValue> findByProductOrderByIdDesc(Product product);

    PropertyValue getByPropertyAndProduct(Property property,Product product);
}
