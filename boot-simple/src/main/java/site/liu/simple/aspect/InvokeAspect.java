package site.liu.simple.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class InvokeAspect {

    /**
     * 最终日志打印顺序：
     * around前置处理 -@around
     * before 处理  -@before
     * aopTest method  -业务代码
     * AfterReturn 处理，返回值：success -@AfterReturning
     * after 处理  -@after
     * around后置处理  -@around
     */


    /**
     * 定义切点是此注解
     */
    @Pointcut("@annotation(site.liu.simple.annotation.InvokeLog)")
    public void pointCut() {

    }

    @Around(value = "pointCut()")
    public Object aroundMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        log.info("around前置处理");
        Object proceed = proceedingJoinPoint.proceed();
        log.info("around后置处理");

        return proceed;
    }

    @Before("pointCut()")
    public void beforeMethod(JoinPoint joinPoint) {
        log.info("before 处理");
    }

    @After("pointCut()")
    public void afterMethod(JoinPoint joinPoint) {
        log.info("after 处理");
    }

    /**
     * 后置通知，可以获取返回值
     * @param joinPoint
     * @param returnValue 这个参数名需要与注解中returning定义名相同，告诉注解用这个参数来接受返回值
     * @return
     */
    @AfterReturning(value = "pointCut()", returning = "returnValue")
    public Object afterMethodReturning(JoinPoint joinPoint, Object returnValue) {
        log.info("AfterReturn 处理，返回值：{}", returnValue);
        return returnValue;
    }

}
