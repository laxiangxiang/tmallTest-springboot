package com.example.tmall.util.comparator;

import com.example.tmall.pojo.Product;

import java.util.Comparator;

/**
 * Created by LXX on 2019/2/15.
 * 把 销量高的放前面
 */
public class ProductSaleCountComparator implements Comparator<Product>{

    @Override
    public int compare(Product o1, Product o2) {
        return o2.getSaleCount() - o1.getSaleCount();
    }
}
