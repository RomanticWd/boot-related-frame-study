package site.lgong.config.components;

import org.springframework.beans.factory.annotation.Value;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * 变量池，容器封装所有带@Value注解的属性
 *
 * @author: liudayue
 * @date: 2022-01-28 17:37
 **/
public class VariablePool {

    public static Map<String, Map<Class, String>> pool = new HashMap<>();

    // @value注解要使用${}格式
    private static final String regex = "^(\\$\\{)(.)+(\\})$";

    private static Pattern pattern;

    static {
        pattern = Pattern.compile(regex);
    }

    public static void add(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Value.class)) {
                Value annotation = field.getAnnotation(Value.class);
                String annotationValue = annotation.value();
                // 不满足格式直接跳过循环
                if (!pattern.matcher(annotationValue).matches()) {
                    continue;
                }
                // 处理掉${}
                annotationValue = annotationValue.replace("${", "");
                annotationValue = annotationValue.substring(0, annotationValue.length() - 1);
                // 类似于pool.getOrDefault(annotationValue, new HashMap<>());
                Map<Class, String> classMap = Optional.ofNullable(pool.get(annotationValue)).orElse(new HashMap<>());
                // 以类为key， 因为一个@value注解修饰的变量可以在多个类中使用
                classMap.put(clazz, field.getName());
                pool.put(annotationValue, classMap);
            }
        }
    }

    public static Map<String, Map<Class, String>> getPool() {
        return pool;
    }
}
