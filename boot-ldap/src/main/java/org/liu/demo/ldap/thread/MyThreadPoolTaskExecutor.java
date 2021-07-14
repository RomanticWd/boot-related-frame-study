package org.liu.demo.ldap.thread;

import org.slf4j.MDC;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;

/**
 * 自定义线程池执行器：修改其中的run()方法，解决异步过程中，子线程并没有办法获取到父线程ThreadLocal存储的数据问题
 *
 * @author yue.liu
 * @since [2021/7/14 12:32]
 */
public class MyThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

    @Override
    public void execute(Runnable runnable) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        super.execute(() -> run(runnable, contextMap));
    }

    private void run(Runnable runnable, Map<String, String> context) {
//        父线程ThreadLocal存储的数据, 传递到子线程中。
        if (context != null) {
            MDC.setContextMap(context);
        }
        try {
            runnable.run();
        } finally {
            MDC.clear();
        }
    }
}
