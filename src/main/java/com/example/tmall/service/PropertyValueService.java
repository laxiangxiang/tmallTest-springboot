package com.example.tmall.service;

import com.example.tmall.dao.PropertyValueDao;
import com.example.tmall.pojo.Product;
import com.example.tmall.pojo.Property;
import com.example.tmall.pojo.PropertyValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by LXX on 2019/1/29.
 */
@Service
@CacheConfig(cacheNames = "propertyValues")
public class PropertyValueService {

    @Autowired
    private PropertyValueDao propertyValueDao;

    @Autowired
    private PropertyService propertyService;

    public void init(Product product){
        List<Property> properties = propertyService.listByCategory(product.getCategory());
        properties.forEach(property -> {
            PropertyValue propertyValue = getByPropertyAndProduct(property,product);
            if (null == propertyValue){
                propertyValue = new PropertyValue();
                propertyValue.setProduct(product);
                propertyValue.setProperty(property);
                propertyValueDao.save(propertyValue);
            }
        });
    }

    @Cacheable(key="'propertyValues-pid-'+ #p0.id")
    public List<PropertyValue> list(Product product){
        return propertyValueDao.findByProductOrderByIdDesc(product);
    }

    @Cacheable(key = "'propertyValue-one-pid'+#p0.id+'-ptid'+#p1.id")
    public PropertyValue getByPropertyAndProduct(Property property,Product product){
        return propertyValueDao.getByPropertyAndProduct(property,product);
    }

    @CacheEvict(allEntries = true)
    public void update(PropertyValue propertyValue){
        propertyValueDao.save(propertyValue);
    }
}
