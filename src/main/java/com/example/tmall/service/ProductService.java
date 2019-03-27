package com.example.tmall.service;

import com.example.tmall.dao.ProductDao;
import com.example.tmall.elasticsearch.ProductESDAO;
import com.example.tmall.pojo.Category;
import com.example.tmall.pojo.Product;
import com.example.tmall.util.Page4Navigator;
import com.example.tmall.util.SpringContextUtil;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LXX on 2019/1/29.
 */
@Service
@CacheConfig(cacheNames = "products")
public class ProductService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductImageService productImageService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ProductESDAO productESDAO;

    @CacheEvict(allEntries = true)
    public void add(Product product){
        productDao.save(product);
        productESDAO.save(product);
    }

    @CacheEvict(allEntries = true)
    public void delete(int id){
        productDao.deleteById(id);
        productESDAO.deleteById(id);
    }

    @Cacheable(key = "'product-one-'+#p0")
    public Product get(int id){
        return productDao.getOne(id);
    }

    @CacheEvict(allEntries = true)
    public void update(Product product){
        productDao.save(product);
        productESDAO.save(product);
    }

    @Cacheable(key = "'products-cid-'+#p0+'-page-'+#p1+'-'+#p2")
    public Page4Navigator<Product> listWithPage(int categoryId,int start,int size,int navigatePages){
        Category category = categoryService.get(categoryId);
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = PageRequest.of(start,size,sort);
        Page page = productDao.findByCategory(category,pageable);
        return new Page4Navigator<>(page,navigatePages);
    }

    /**
     * 为分类填充产品集合
     * @param category
     */
    public void fill(Category category){
//        List<Product> products = listByCategory(category);
        // springboot 的缓存机制是通过切面编程 aop来实现的。 从fill方法里直接调用 listByCategory 方法， aop 是拦截不到的，也就不会走缓存了。
        ProductService productService = SpringContextUtil.getBean(ProductService.class);
        List<Product> products = productService.listByCategory(category);
        productImageService.setFirstProductImages(products);
        category.setProducts(products);
    }

    /**
     * 为多个分类填充产品集合
     * @param categories
     */
    public void fill(List<Category> categories){
        categories.forEach(category -> fill(category));
    }

    /**
     * 为多个分类填充推荐产品集合，
     * 即吧分类下的产品集合按照8个为一行，拆成多行，以利后续页面上进行显示
     * @param categories
     */
    public void fillByRow(List<Category> categories){
        int productNumberEachRow = 8;
        categories.forEach(category -> {
            List<Product> products = category.getProducts();
            List<List<Product>> productsByRow = new ArrayList<List<Product>>();
            for (int i = 0; i < products.size(); i += productNumberEachRow){
                int size = i+productNumberEachRow;
                if (size > products.size()){
                    List<Product> productsOfEachRow = products.subList(i,products.size());
                    productsByRow.add(productsOfEachRow);
                    break;
                }else {
                    List<Product> productsOfEachRow = products.subList(i,size);
                    productsByRow.add(productsOfEachRow);
                }
            }
            category.setProductsByRow(productsByRow);
        });
    }

    /**
     * 查询某个分类下的所有产品
     * @param category
     * @return
     */
    @Cacheable(key = "'products-cid-'+#p0.id")
    public List<Product> listByCategory(Category category){
        return productDao.findByCategory(category);
    }

    public void setSaleAndReviewNumber(Product product){
        int saleCount = orderItemService.getSaleCount(product);
        product.setSaleCount(saleCount);
        int reviewCount = reviewService.getCount(product);
        product.setReviewCount(reviewCount);
    }

    public void setSaleAndReviewNumber(List<Product> products){
        products.forEach(product -> setSaleAndReviewNumber(product));
    }

    public List<Product> search(String keyword,int start,int size){
//        Sort sort = new Sort((Sort.Direction.DESC),"id");
//        Pageable pageable = PageRequest.of(start,size,sort);
//        List<Product> products = productDao.findByNameLike("%"+keyword+"%");
//        return products;
        //查询的修改。 以前查询是模糊查询，现在通过 ProductESDAO 到 elasticsearch 中进行查询了。
        initDatabase2ES();
        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(
                QueryBuilders.matchPhraseQuery("name",keyword),
                ScoreFunctionBuilders.weightFactorFunction(100))
                .scoreMode(FunctionScoreQuery.ScoreMode.SUM)
                .setMinScore(10);
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = PageRequest.of(start,size,sort);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .withQuery(functionScoreQueryBuilder).build();
        Page<Product> page = productESDAO.search(searchQuery);
        return page.getContent();
    }

    /**
     * 初始化数据到es. 因为数据刚开始都在数据库中，不在es中，所以刚开始查询，先看看es有没有数据，如果没有，就把数据从数据库同步到es中。
     */
    private void initDatabase2ES(){
        Pageable pageable = PageRequest.of(0,5);
        Page<Product> page = productESDAO.findAll(pageable);
        if (page.getContent().isEmpty()){
            List<Product> products = productDao.findAll();
            for (Product product : products){
                productESDAO.save(product);
            }
        }

    }
}
