package com.example.tmall.util.comparator;

import com.example.tmall.pojo.Product;

import java.util.Comparator;

/**
 * Created by LXX on 2019/2/15.
 * 把 创建日期晚的放前面
 */
public class ProductDateComparator implements Comparator<Product>{

    @Override
    public int compare(Product o1, Product o2) {
        return o1.getCreateDate().compareTo(o2.getCreateDate());
    }
}
