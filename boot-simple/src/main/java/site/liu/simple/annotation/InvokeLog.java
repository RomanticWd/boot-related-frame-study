package site.liu.simple.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD) // 作用于方法上
@Retention(RetentionPolicy.RUNTIME) // 声明周期是在运行时候
@Documented
public @interface InvokeLog {
}
