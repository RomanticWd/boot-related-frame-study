package site.liu.simple.aspect;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import site.liu.simple.proxy.JoinPointProxy;

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
     * 全局异常捕获 -GlobalExceptionHandler
     */


    /**
     * 定义切点是此注解
     */
    @Pointcut("@annotation(site.liu.simple.annotation.InvokeLog)")
    public void pointCut() {

    }

    /**
     * 最初的切面加全局异常捕获的执行顺序
     *
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = "pointCut()")
    public Object aroundMethodOne(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // 切面前置处理
        aroundBefore();
        // 业务执行
        Object proceed = proceedingJoinPoint.proceed();
        // 切面后置处理
        aroundAfter(proceed);

        //打印顺序：
        /**
         * around前置处理
         * before 处理
         * aopTest method
         * after 处理
         * 全局异常捕获
         */
        return proceed;
    }

    /**
     * 通过代理类处理aop异常和全局异常捕获
     *
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
//    @Around(value = "pointCut()")
    public Object aroundMethodTwo(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        JoinPointProxy pointProxy = new JoinPointProxy(proceedingJoinPoint);

        Object result = null;
        try {
            result = handler(pointProxy);

        } catch (Exception e) {
            //主业务逻辑异常，直接抛出，不捕获不处理
            if (pointProxy.isExecuted()) {
                Exception exception = pointProxy.getException();
                if (exception != null) {
                    throw exception;
                }
            }

            //切面代码异常，捕获并进行处理
            log.error("切面异常", e);

            //确保主业务方法已执行，并放行
            if (pointProxy.isExecuted()) {
                return pointProxy.getResult();
            } else {
                return pointProxy.proceed();
            }
        }
        return result;
    }

    private Object handler(JoinPointProxy pointProxy) throws Throwable {
        // 切面前置处理
        aroundBefore();
        // 业务执行
        Object proceed = pointProxy.proceed();
        // 切面后置处理
        aroundAfter(proceed);
        return proceed;
    }

    private void aroundBefore() {
        log.info("around前置处理");
    }

    private void aroundAfter(Object proceed) {
        log.info("around后置处理，response：{}", JSON.toJSONString(proceed));
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
     *
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
