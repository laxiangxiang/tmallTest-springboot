package com.example.tmall.service;

import com.example.tmall.dao.CategoryDAO;
import com.example.tmall.pojo.Category;
import com.example.tmall.pojo.Product;
import com.example.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.NullableUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by LXX on 2019/1/25.
 */
@Service
@CacheConfig(cacheNames = "categories")
/**
 * @CacheEvict(allEntries=true)
 *其意义是删除 categories~keys 里的所有的keys.
 *可是呢，下面有各种有一行注释掉的注解。 比如增加，注释掉的注解是：
    //	@CachePut(key="'category-one-'+ #p0")它的作用是以 category-one-id 的方式增加到 Redis中去。
 *这样做本身其实是没有问题的，而且在 get 的时候，还可以使用，但是最后还是放弃这种做法了，为什么呢？
 *因为，虽然这种方式可以在 redis 中增加一条数据，但是： 它并不能更新分页缓存 categories-page-0-5 里的数据， 这样就会出现数据不一致的问题了。
 * 即在redis 中，有这一条单独数据的缓存，但是在分页数据里，却没有这一条，这样就矛盾了。为了解决这个矛盾，最精细的做法是，同时更新分页缓存里的数据。
 * 因为 redis 不是结构化的数据，它是 “nosql", 为了做到 同时更新缓存分页缓存里的数据，会超级的复杂，而且超级容易出错，其开发量也会非常大。
 *那么怎么办呢？ 最后，我们采纳了折中的办法，即，一旦增加了某个分类数据，那么就把缓存里所有分类相关的数据，都清除掉。
 * 下一次再访问的时候，一看，缓存里没数据，那么就会从数据库中读出来，读出来之后，再放在缓存里。如此这般，牺牲了一点小小的性能，数据的一致性就得到了保障了。
 *修改和删除，都是同一个道理。
 */
public class CategoryService {
    @Autowired
    private CategoryDAO categoryDAO;

    public List<Category> list(){
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        return categoryDAO.findAll(sort);
    }

    @Cacheable(key = "'categories-page-'+#p0+'-'+#p1")
    public Page4Navigator<Category> list(int start,int size,int navigatePages){
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(start,size,sort);
        Pageable pageable1 = PageRequest.of(start,size,sort);
        Page pageFromJPA = categoryDAO.findAll(pageable1);
        return new Page4Navigator<>(pageFromJPA,navigatePages);
    }

    @CacheEvict(allEntries = true)//删除categories下的所有key
//    @CachePut(key = "'category-one-'+#p0")
    public void add(Category category){
        categoryDAO.save(category);
    }

    @CacheEvict(allEntries = true)
//    @CacheEvict(key = "'category-one-'+#p0")
    public void delete(int id){
        categoryDAO.deleteById(id);
    }

    @Cacheable(key = "'category-one-'+#p0")//p0表示第一个参数
    public Category get(int id){
        return categoryDAO.getOne(id);
    }

    @CacheEvict(allEntries = true)
//    @CachePut(key = "'category-one-'+#p0")
    public void save(Category category){
        categoryDAO.save(category);
    }

    /**
     * 这个方法的用处是删除Product对象上的 分类。 为什么要删除呢？
     * 因为在对分类做序列还转换为 json 的时候，会遍历里面的 products,
     * 然后遍历出来的产品上，又会有分类，接着就开始子子孙孙无穷溃矣地遍历了，就搞死个人了
     * 而在这里去掉，就没事了。 只要在前端业务上，没有通过产品获取分类的业务，去掉也没有关系。
     * 和后台订单管理哪里去掉 orderItem 上的 订单信息道理是相同的。
     * @param categories
     */
    public void removeCategoryFromProduct(List<Category> categories){
        categories.forEach(category -> removeCategoryFromProduct(category));
    }

    public void removeCategoryFromProduct(Category category){
        List<Product> products = category.getProducts();
        if (null != products){
            products.forEach(product -> product.setCategory(null));
        }
        List<List<Product>> productsByRow = category.getProductsByRow();
        if (null != productsByRow){
            productsByRow.forEach(products1 -> {
                products1.forEach(product -> product.setCategory(null));
            });
        }
    }
}
