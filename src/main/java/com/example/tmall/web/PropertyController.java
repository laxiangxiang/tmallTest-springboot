package com.example.tmall.web;

import com.example.tmall.pojo.Property;
import com.example.tmall.service.PropertyService;
import com.example.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by LXX on 2019/1/28.
 */
@RestController
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @GetMapping("/categories/{cid}/properties")
    public Page4Navigator<Property> listWithPage(
            @PathVariable(name = "cid")int cid,
            @RequestParam(name = "start",value = "start",defaultValue = "0")int start,
            @RequestParam(name = "size",value = "size",defaultValue = "5")int size
    ){
        Page4Navigator<Property> page4Navigator = propertyService.listWithPage(cid,start,size,5);
        return page4Navigator;
    }

    @PostMapping("/properties")
    public Object add(@RequestBody Property property){
        propertyService.add(property);
        return property;
    }

    @GetMapping("/properties/{id}")
    public Property get(@PathVariable(name = "id")int id){
        Property property = propertyService.get(id);
        return property;
    }

    @PutMapping("/properties/{id}")
    public Property update(Property property){
        Property property1 = propertyService.update(property);
        return property1;
    }

    @DeleteMapping("/properties/{id}")
    public String delete(@PathVariable(name = "id")int id){
        propertyService.delete(id);
        return null;
    }
}
