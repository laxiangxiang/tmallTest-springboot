package com.example.tmall.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by LXX on 2019/2/12.
 */
@Data
@Table(name = "user")
@Entity
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "password")
    private String password;

    private String name;

    private String salt;

    @Transient
    private String anonymousName;

    public String getAnonymousName(){
        if (null != anonymousName)
            return anonymousName;
        if (null == name)
            anonymousName = null;
        else if (name.length() <= 1)
            anonymousName = "*";
        else if (name.length() == 2)
            anonymousName = name.substring(0,1)+"*";
        else {
            char[] cs = name.toCharArray();
            for (int i = 1;i < cs.length - 1; i++){
                cs[i] = '*';
            }
            anonymousName = new String(cs);
        }
        return anonymousName;
    }
}
