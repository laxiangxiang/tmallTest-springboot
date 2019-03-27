package com.example.tmall.web;

import com.example.tmall.pojo.Product;
import com.example.tmall.pojo.PropertyValue;
import com.example.tmall.service.ProductService;
import com.example.tmall.service.PropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by LXX on 2019/1/29.
 */
@RestController
public class PropertyValueController {

    @Autowired
    private PropertyValueService propertyValueService;

    @Autowired
    private ProductService productService;

    @GetMapping("/products/{id}/propertyValues")
    public List<PropertyValue> list(@PathVariable("id")int id){
        Product product = productService.get(id);
        propertyValueService.init(product);
        List<PropertyValue> propertyValues = propertyValueService.list(product);
        return propertyValues;
    }

    @PutMapping("/propertyValues")
    public PropertyValue update(@RequestBody PropertyValue propertyValue){
        propertyValueService.update(propertyValue);
        return propertyValue;
    }
}
