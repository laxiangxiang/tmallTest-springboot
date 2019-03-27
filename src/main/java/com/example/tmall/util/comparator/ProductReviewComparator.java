package com.example.tmall.util.comparator;

import com.example.tmall.pojo.Product;

import java.util.Comparator;

/**
 * Created by LXX on 2019/2/15.
 * 把 评价数量多的放前面
 */
public class ProductReviewComparator implements Comparator<Product>{
    @Override
    public int compare(Product o1, Product o2) {
        return o2.getReviewCount() - o1.getReviewCount();
    }
}
