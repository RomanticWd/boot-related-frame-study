package site.liu.simple.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.liu.simple.annotation.InvokeLog;

@Slf4j
@RestController
@RequestMapping("/boot-simple")
public class TestController {

    @InvokeLog
    @GetMapping("/test/aop")
    public String aopTest() {
        log.info("aopTest method");
        return "success";
    }

}
