package site.lgong.config.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import site.lgong.config.service.UserService;

/**
 * 测试类
 *
 * @author: liudayue
 * @date: 2022-01-29 14:57
 **/
@RestController
public class TestController {

    @Autowired
    private UserService userService;

    @GetMapping("name")
    public String getUserName() {
        return " name: " + userService.getName() + "\r\n age:  " + userService.getAge();
    }

}
