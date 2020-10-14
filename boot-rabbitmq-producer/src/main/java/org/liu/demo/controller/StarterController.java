package org.liu.demo.controller;

import org.liu.demo.service.HelloWorldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StarterController {

    @Autowired
    private HelloWorldService helloWorldService;

    @RequestMapping("/testStarter")
    public String sayHello() {
        return helloWorldService.sayHello();
    }

}
