package site.liu.simple.proxy;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 切面代理类
 */
public class JoinPointProxy {

    //方法是否已执行
    private boolean executed;

    //方法执行结果
    private Object result;

    //方法执行异常
    private Exception exception;

    //连接点
    private ProceedingJoinPoint joinPoint;

    public JoinPointProxy(ProceedingJoinPoint joinPoint) {
        this.joinPoint = joinPoint;
    }

    public Object proceed() throws Throwable {
        // 执行前判断主方法是否已经执行
        if (!isExecuted()) {
            try {
                result = joinPoint.proceed();
            } catch (Exception e) {
                // 主方法catch异常， exception不为null说明主方法执行遇到异常了
                exception = e;
                throw e;
            } finally {
                executed = true;
            }
        }
        return result;
    }

    public boolean isExecuted() {
        return executed;
    }

    public Object getResult() {
        return result;
    }

    public Exception getException() {
        return exception;
    }
}


