package com.example.tmall.web;

import com.example.tmall.pojo.Product;
import com.example.tmall.pojo.ProductImage;
import com.example.tmall.service.ProductImageService;
import com.example.tmall.service.ProductService;
import com.example.tmall.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LXX on 2019/2/12.
 */
@RestController
public class ProductImageController {

    @Autowired
    private ProductImageService productImageService;

    @Autowired
    private ProductService productService;

    @GetMapping("/products/{productId}/productImages")
    public List<ProductImage> list(@RequestParam(name = "type")String type, @PathVariable(name = "productId")int productId){
        Product product = productService.get(productId);
        if (ProductImageService.type_single.equals(type)){
            List<ProductImage> singles = productImageService.listSingleProductImages(product);
            return singles;
        }else if (ProductImageService.type_detail.equals(type)){
            List<ProductImage> details = productImageService.listDetailProductImages(product);
            return details;
        }else {
            return new ArrayList<>();
        }
    }

    @PostMapping("/productImages")
    public Object add(@RequestParam(name = "productId")int productId,
                      @RequestParam(name = "type")String type,
                      MultipartFile image,
                      HttpServletRequest request){
        ProductImage productImage = new ProductImage();
        Product product = productService.get(productId);
        productImage.setProduct(product);
        productImage.setType(type);
        productImageService.add(productImage);
        String folder = "img/";
        if (ProductImageService.type_single.equals(productImage.getType())){
            folder += "productSingle";
        }else {
            folder +="productDetail";
        }
        File imageFolder = new File(request.getServletContext().getRealPath(folder));
        File file = new File(imageFolder,productImage.getId()+".jpg");
        String fileName = file.getName();
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        try {
            image.transferTo(file);
            BufferedImage img = ImageUtil.change2jpg(file);
            ImageIO.write(img,"jpg",file);
        }catch (Exception e){
            e.printStackTrace();
        }

        if (ProductImageService.type_single.equals(productImage.getType())){
            String imageFolder_small = request.getServletContext().getRealPath("img/productSingle_small");
            String imageFolder_middle = request.getServletContext().getRealPath("img/productSingle_middle");
            File f_small = new File(imageFolder_small,fileName);
            File f_middle = new File(imageFolder_middle,fileName);
            f_small.getParentFile().mkdirs();
            f_middle.getParentFile().mkdirs();
            ImageUtil.resizeImage(file,56,56,f_small);
            ImageUtil.resizeImage(file,217,190,f_middle);
        }
        return productImage;
    }

    @DeleteMapping("/productImages/{id}")
    public void delete(@PathVariable(name = "id")int productImageId,HttpServletRequest request){
        ProductImage productImage = productImageService.get(productImageId);
        productImageService.delete(productImageId);
        String folder = "img/";
        if (ProductImageService.type_single.equals(productImage.getType()))
            folder += "productSingle";
        else
            folder += "productDetail";
        File imageFolder = new File(request.getServletContext().getRealPath(folder));
        File file = new File(imageFolder,productImage.getId()+".jpg");
        String fileName = file.getName();
        file.delete();
        if (ProductImageService.type_single.equals(productImage.getType())){
            String imageFolder_small = request.getServletContext().getRealPath("img/productSingle_small");
            String imageFolder_middle = request.getServletContext().getRealPath("img/productSingle_middle");
            File f_small = new File(imageFolder_small,fileName);
            File f_middle = new File(imageFolder_middle,fileName);
            f_small.delete();
            f_middle.delete();
        }
    }
}
