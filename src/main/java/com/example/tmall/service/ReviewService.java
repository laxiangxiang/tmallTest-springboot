package com.example.tmall.service;

import com.example.tmall.dao.ReviewDao;
import com.example.tmall.pojo.Product;
import com.example.tmall.pojo.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by LXX on 2019/2/15.
 */
@Service
@CacheConfig(cacheNames = "reviews")
public class ReviewService {

    @Autowired
    private ReviewDao reviewDao;

    @Autowired
    private ProductService productService;

    @CacheEvict(allEntries = true)
    public void add(Review review){
        reviewDao.save(review);
    }

    @Cacheable(key = "'reviews-pid-'+#p0.id")
    public List<Review> list(Product product){
        return reviewDao.findByProductOrderByIdDesc(product);
    }

    @Cacheable(key="'reviews-count-pid-'+ #p0.id")
    public int getCount(Product product){
        return reviewDao.countByProduct(product);
    }
}
