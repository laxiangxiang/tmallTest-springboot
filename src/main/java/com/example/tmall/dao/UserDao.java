package com.example.tmall.dao;

import com.example.tmall.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by LXX on 2019/2/12.
 */
public interface UserDao extends JpaRepository<User,Integer>{

    User findByName(String name);

    User findByNameAndPassword(String name,String password);

}
