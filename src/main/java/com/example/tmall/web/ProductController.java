package com.example.tmall.web;

import com.example.tmall.pojo.Product;
import com.example.tmall.service.ProductImageService;
import com.example.tmall.service.ProductService;
import com.example.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by LXX on 2019/1/29.
 */
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductImageService productImageService;

    @GetMapping("categories/{cid}/products")
    public Page4Navigator<Product> listWithPage(
            @PathVariable(name = "cid") int categoryId,
            @RequestParam(name = "start",value = "start",defaultValue = "0") int start,
            @RequestParam(name = "size",value = "size",defaultValue = "5") int size){
        Page4Navigator<Product> page4Navigator = productService.listWithPage(categoryId,start,size,5);
        productImageService.setFirstProductImages(page4Navigator.getContent());
        return page4Navigator;
    }

    @PostMapping("/products")
    public void add(@RequestBody Product product){
        productService.add(product);
    }

    @GetMapping("/products/{id}")
    public Product get(@PathVariable(name = "id") int id){
        return productService.get(id);
    }

    @PutMapping("/products")
    public void update(@RequestBody Product product){
        productService.update(product);
    }

    @DeleteMapping("/products/{id}")
    public String delete(@PathVariable(name = "id")int id){
        productService.delete(id);
        return null;
    }
}
