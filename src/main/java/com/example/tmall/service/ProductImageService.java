package com.example.tmall.service;

import com.example.tmall.dao.ProductImageDao;
import com.example.tmall.pojo.OrderItem;
import com.example.tmall.pojo.Product;
import com.example.tmall.pojo.ProductImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by LXX on 2019/2/12.
 */
@Service
@CacheConfig(cacheNames = "productImages")
public class ProductImageService {

    public static final String type_single = "single";

    public static final String type_detail="detail";

    @Autowired
    private ProductImageDao productImageDao;

    @Autowired
    private ProductService productService;

    @CacheEvict(allEntries = true)
    public void add(ProductImage productImage){
        productImageDao.save(productImage);
    }

    @CacheEvict(allEntries = true)
    public void delete(int id){
        productImageDao.deleteById(id);
    }

    @Cacheable(key = "'productImage-one-'+#p0")
    public ProductImage get(int id){
        return productImageDao.getOne(id);
    }

    @Cacheable(key = "'productImages-single-pid-'+#p0.id")
    public List<ProductImage> listSingleProductImages(Product product){
        return productImageDao.findByProductAndTypeOrderByIdDesc(product,type_single);
    }

    @Cacheable(key = "'productImages-detail-pid'+#p0.id")
    public List<ProductImage> listDetailProductImages(Product product){
        return productImageDao.findByProductAndTypeOrderByIdDesc(product,type_detail);
    }

    public void setFirstProductImage(Product product){
        List<ProductImage> singleImages = listSingleProductImages(product);
        if (!singleImages.isEmpty()){
            product.setFirstProductImage(singleImages.get(0));
        }else {
            product.setFirstProductImage(new ProductImage());
        }
    }

    public void setFirstProductImages(List<Product> products){
        products.forEach(product -> setFirstProductImage(product));
    }

    public void setFirstProductImagesOnOrderItems(List<OrderItem> onOrderItems){
        onOrderItems.forEach(orderItem -> setFirstProductImage(orderItem.getProduct()));
    }
}
