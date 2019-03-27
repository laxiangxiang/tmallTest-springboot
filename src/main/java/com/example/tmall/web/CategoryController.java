package com.example.tmall.web;

import com.example.tmall.pojo.Category;
import com.example.tmall.service.CategoryService;
import com.example.tmall.util.ImageUtil;
import com.example.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by LXX on 2019/1/25.
 */
@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 无分页
     * @return
     */
    @GetMapping("/categories/nopage")
    public List<Category> list(){
        return categoryService.list();
    }

    /**
     * 有分页
     * @param start
     * @param size
     * @return
     */
    @GetMapping("/categories")
    public Page4Navigator<Category> listWithPage(
            @RequestParam(name = "start",value = "start",defaultValue = "0") int start,//从第0页开始
            @RequestParam(name = "size",value = "size",defaultValue = "5") int size){
        Page4Navigator page4Navigator = categoryService.list(start,size,5);//5表示导航分页最多有5个，像 [1,2,3,4,5] 这样
        return page4Navigator;
    }

    @PostMapping("/categories")
    public Object add(Category category, MultipartFile image, HttpServletRequest request) throws IOException{
        categoryService.add(category);
        saveOrUpdateImageFile(category,image,request);
        return category;
    }

    private void saveOrUpdateImageFile(Category category,MultipartFile image,HttpServletRequest request) throws IOException{
        File imageFolder = new File(request.getServletContext().getRealPath("img/category"));
        File file = new File(imageFolder,category.getId()+".jpg");
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        image.transferTo(file);
        BufferedImage img = ImageUtil.change2jpg(file);
        ImageIO.write(img,"jpg",file);
    }

    @DeleteMapping("/categories/{id}")
    public String delete(@PathVariable(name = "id")int id,HttpServletRequest request){
        categoryService.delete(id);
        File imageFolder = new File(request.getServletContext().getRealPath("img/category"));
        System.out.println(request.getServletPath());
        System.out.println(request.getServletContext().getRealPath("img/category"));
        File file = new File(imageFolder,id+".jpg");
        file.delete();
//        返回 null, 会被RESTController 转换为空字符串
        return null;
    }

    @GetMapping("/categories/{id}")
    public Category get(@PathVariable(name = "id") int id){
        return categoryService.get(id);
    }

    @PutMapping("/categories")
    public Category update(Category category,MultipartFile image,HttpServletRequest request) throws IOException{
        categoryService.save(category);
        if (image!=null){
            saveOrUpdateImageFile(category,image,request);
        }
        return category;
    }
}
