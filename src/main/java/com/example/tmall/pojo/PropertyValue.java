package com.example.tmall.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by LXX on 2019/1/29.
 */
@Entity
@Table(name = "propertyvalue")
@Data
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class PropertyValue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String value;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
