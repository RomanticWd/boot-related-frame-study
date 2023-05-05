package site.liu.simple.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TestService {

    /**
     * 没有加@recover注解打印日志：
     * retryTest method
     * retryMethod name :liu
     * invoke name :liu
     * retryMethod name :liu
     * invoke name :liu
     * retryMethod name :liu
     * invoke name :liu
     * retryMethod name :liu
     * invoke name :liu
     * retry捕获异常, 记录并告警:远程调用错误
     * 全局异常捕获：远程调用错误
     */
    // 重试3次，第一次重试间隔1s，第二次间隔2s，第三次间隔4s
    @Retryable(value = {Exception.class}, maxAttempts = 4, backoff = @Backoff(delay = 1000, multiplier = 2))
    public String retryMethod(String name) {
        log.info("retryMethod name :{}", name);
        invoke(name);
        return name + "result";
    }

    /**
     * 模拟异常
     *
     * @return String
     */
    private String invoke(String name) {
        log.info("invoke name :{}", name);
        throw new RuntimeException("远程调用错误");
    }

    /**
     * 加@recover注解打印日志：
     * retryTest method
     * retryMethod name :liu
     * invoke name :liu
     * retryMethod name :liu
     * invoke name :liu
     * retryMethod name :liu
     * invoke name :liu
     * retryMethod name :liu
     * invoke name :liu
     * exception:远程调用错误, retryMethodRecover : liu
     * 最终没有抛出异常
     */
//    @Recover
    public String retryMethodRecover(Exception e, String name) {
        log.info("exception:{}, retryMethodRecover : {}", e.getMessage(), name);
        return name + "retryMethodRecover";
    }

}
