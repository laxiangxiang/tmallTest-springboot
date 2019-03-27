package com.example.tmall.util.comparator;

import com.example.tmall.pojo.Product;

import java.util.Comparator;

/**
 * Created by LXX on 2019/2/15.
 */
public class ProductPriceComparator implements Comparator<Product>{

    @Override
    public int compare(Product o1, Product o2) {
        return (int) o1.getPromotePrice()- (int) o2.getPromotePrice();
    }
}
