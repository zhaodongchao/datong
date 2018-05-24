package com.dongchao.datong.common.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@Scope("singleton")
@RestController
public class UserController {
    @Autowired
    private UserDao userDao ;
    @GetMapping(value = "/user/{id}")
    public String findUser(@PathVariable(name = "id",required = false) String id){
        User user1 = new User("13","赵东朝","东东",25,new BigDecimal(10000));
        userDao.save(user1);
        return userDao.getStr(id);
    }
}
