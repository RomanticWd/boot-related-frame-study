package site.lgong.config.components;

import lombok.extern.slf4j.Slf4j;
import site.lgong.config.utils.SpringContextUtil;
import site.lgong.config.converter.YamlConverter;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 修改配置值
 *
 * @author: liudayue
 * @date: 2022-01-29 14:32
 **/
@Slf4j
public class PropertyTrigger {

    public static void change(String ymlContent) {
        Map<String, Object> newMap = YamlConverter.convert(ymlContent);
        Map<String, Object> oldMap = EnvInitializer.getEnvMap();

        oldMap.entrySet().stream()
                // 过滤，key在新的Map中对应的属性值是否发生了改变，如果没有改变则不做之后的任何操作
                .filter(entry -> newMap.containsKey(entry.getKey()))
                .filter(entry -> !entry.getValue().equals(newMap.get(entry.getKey())))
                .forEach(entry -> {
                    // 新的值替换envMap中的旧值
                    log.info("key:{}, value:{}", entry.getKey(), entry.getValue());
                    Object newVal = newMap.get(entry.getKey());
                    oldMap.put(entry.getKey(), newVal);
                    doChange(entry.getKey(), newVal);
                });
        // 修改后的Map写回EnvInitializer中的envMap
        EnvInitializer.setEnvMap(oldMap);
    }

    private static void doChange(String propertyName, Object newValue) {
        log.info("new value:{}", newValue);
        // 获取变量池
        Map<String, Map<Class, String>> pool = VariablePool.getPool();
        // 从变量池中取出用到这个变量的类
        Map<Class, String> classProMap = pool.get(propertyName);

        classProMap.forEach((className, realPropertyName) -> {
            try {
                Object bean = SpringContextUtil.getBean(className);
                Field field = className.getDeclaredField(realPropertyName);
                // 利用反射设置新的值
                field.setAccessible(true);
                field.set(bean, newValue);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                log.error("设置新value异常", e);
            }
        });
    }
}
