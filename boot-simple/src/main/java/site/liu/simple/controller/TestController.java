package site.liu.simple.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.liu.simple.annotation.InvokeLog;
import site.liu.simple.service.TestService;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/boot-simple")
public class TestController {

    @Resource
    private TestService testService;

    @InvokeLog
    @GetMapping("/test/aop")
    public String aopTest() {
        log.info("aopTest method");
        // 异常代码，观察切面和全局异常的打印顺序
        int i = 10 / 0;
        return "success";
    }

    @GetMapping("/test/retry")
    public String retryTest(@RequestParam("name") String name) {
        log.info("retryTest method");
        String result = null;
        try {
            result = testService.retryMethod(name);
        } catch (Exception e) {
            log.error("retry捕获异常, 记录并告警:{}", e.getMessage());
            throw e;
        }
        return result;
    }

}
