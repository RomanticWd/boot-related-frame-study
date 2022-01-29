package site.lgong.config.converter;

import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 解析yml 合并map
 *
 * @author: liudayue
 * @date: 2022-01-28 17:57
 **/
public class YamlConverter {

    /**
     * 将多层map转成单层map
     * @param map 要转换的目标map
     * @param parentKey properties中的key，也是@value注解中${}的key
     * @param propertiesMap 最终的单层map
     */
    public static void doConvert(Map<String, Object> map, String parentKey, Map<String, Object> propertiesMap) {
        String prefix = Objects.isNull(parentKey) ? "" : parentKey + ".";
        map.forEach((key, value) -> {
            if (value instanceof Map) {
                // 多层遍历，将多层map转成单层map
                doConvert((Map) value, prefix + key, propertiesMap);
            } else {
                propertiesMap.put(prefix + key, value);
            }
        });
    }

    /**
     * 单层map转成多层
     * @param envMap 单层map
     * @param multiMap 循环嵌套的多层map
     * @return
     */
    public static Map<String, Object> monoToMultiLayer(Map<String, Object> envMap, Map<String, Object> multiMap) {
        if (multiMap == null) {
            multiMap = new HashMap<>();
        }
        for (Map.Entry<String, Object> entry : envMap.entrySet()) {
            // envMap中的key就是配置文件中的key，以通常会有多个.
            String key = entry.getKey();
            Object value = entry.getValue();
            // 没有. ，直接塞入map
            if (!key.contains(".")) {
                multiMap.put(key, value);
            } else {
                // 有.截取第一个.之前和之后的内容
                int index = key.indexOf(".");
                String outKey = key.substring(0, index);
                String innerKey = key.substring(index + 1);

                // 可能存在多个key是以同一串内容开头，如spring.port和spring.url
                Map<String, Object> innerMap = (Map) multiMap.getOrDefault(outKey, new HashMap<>());
                if (!innerMap.containsKey(".")) {
                    innerMap.put(innerKey, envMap.get(key));
                } else {
                    int index2 = innerKey.indexOf(".");
                    String tempKey = innerKey.substring(0, index2);
                    String tempInnerKey = innerKey.substring(index2 + 1);

                    Map<String, Object> tempMap = new HashMap<>();
                    tempMap.put(tempInnerKey, envMap.get(key));
                    // 递归遍历
                    innerMap.put(tempKey, monoToMultiLayer(tempMap, (Map) innerMap.getOrDefault(tempKey, new HashMap<>())));
                }
                multiMap.put(outKey, innerMap);
            }
        }
        return multiMap;
    }

    /**
     * yml格式的内容转成map
     * @param ymlContent yml格式的字符内容
     * @return
     */
    public static Map<String, Object> convert(String ymlContent) {
        Map<String, Object> propertiesMap = new HashMap<>();
        Yaml yaml = new Yaml();
        Iterable<Object> objects = yaml.loadAll(ymlContent);
        for (Object object : objects) {
            doConvert((Map)object, null, propertiesMap);
        }
        return propertiesMap;
    }
}
