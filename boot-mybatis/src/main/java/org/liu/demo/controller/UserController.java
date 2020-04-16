package org.liu.demo.controller;

import org.liu.demo.bean.User;
import org.liu.demo.dao.UserDao;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class UserController {

    @Resource
    private UserDao userDao;

    @GetMapping("/api/user")
    public User findUserByName() {
        String name = "zhang";
        return userDao.findUserByName(name);
    }
}
