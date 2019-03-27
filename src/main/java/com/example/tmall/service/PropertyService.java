package com.example.tmall.service;

import com.example.tmall.dao.CategoryDAO;
import com.example.tmall.dao.PropertyDao;
import com.example.tmall.pojo.Category;
import com.example.tmall.pojo.Property;
import com.example.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by LXX on 2019/1/28.
 */
@Service
@CacheConfig(cacheNames = "properties")
public class PropertyService {

    @Autowired
    private PropertyDao propertyDao;

    @Autowired
    private CategoryService categoryService;

    @CacheEvict(allEntries = true)
    public void add(Property property){
        propertyDao.save(property);
    }

    @CacheEvict(allEntries = true)
    public void delete(int id){
        propertyDao.deleteById(id);
    }

    @Cacheable(key = "'property-one-'+#p0")
    public Property get(int id){
        return propertyDao.getOne(id);
    }

    @CacheEvict(allEntries = true)
    public Property update(Property property){
        return propertyDao.save(property);
    }

    @Cacheable(key = "'propertied-cid'+#p0+'-page-'+#p1+'-'+#p2")
    public Page4Navigator<Property> listWithPage(int categoryId,int start,int size,int navigatePage){
        Category category = categoryService.get(categoryId);
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = PageRequest.of(start,size,sort);
        Page<Property> page = propertyDao.findByCategory(category,pageable);
        return new Page4Navigator<>(page,navigatePage);
    }

    @Cacheable(key = "'properties-cid-'+#p0.id")
    public List<Property> listByCategory(Category category){
        return propertyDao.findByCategory(category);
    }
}
