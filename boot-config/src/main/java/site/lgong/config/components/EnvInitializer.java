package site.lgong.config.components;

import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.core.io.ClassPathResource;
import site.lgong.config.converter.YamlConverter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 解析yml文件，完成配置中心环境的初始化
 *
 * @author: liudayue
 * @date: 2022-01-28 17:52
 **/
public class EnvInitializer {

    private static Map<String, Object> envMap = new HashMap<>();

    public static void init() {
        String rootPath = EnvInitializer.class.getResource("/").getPath();
        List<String> fileList = FileScanner.findFileByType(rootPath, null, FileScanner.TYPE_YML);
        for (String ymlFilePath : fileList) {
            rootPath = FileScanner.getRealRootPath(rootPath);
            ymlFilePath = ymlFilePath.replace(rootPath, "");
            // 用spring自带的YamlMapFactoryBean进行yml文件的解析
            YamlMapFactoryBean yamlMapFb = new YamlMapFactoryBean();
            yamlMapFb.setResources(new ClassPathResource(ymlFilePath));
            // yml文件解析后都会生成一个独立的Map
            Map<String, Object> map = yamlMapFb.getObject();
            // 将解析的map合并
            YamlConverter.doConvert(map, null, envMap);
        }
    }

    public static void setEnvMap(Map<String, Object> envMap) {
        EnvInitializer.envMap = envMap;
    }

    public static Map<String, Object> getEnvMap() {
        return envMap;
    }
}
