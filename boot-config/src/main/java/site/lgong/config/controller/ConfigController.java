package site.lgong.config.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.lgong.config.components.EnvInitializer;
import site.lgong.config.components.PropertyTrigger;
import site.lgong.config.converter.YamlConverter;

import java.util.Map;

/**
 * 控制器
 *
 * @author: liudayue
 * @date: 2022-01-29 11:36
 **/
@Slf4j
@RestController
@RequestMapping("config")
public class ConfigController {

    @GetMapping("get")
    public String get() {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        String yamlContent = null;

        try {
            Map<String, Object> envMap = EnvInitializer.getEnvMap();
            Map<String, Object> map = YamlConverter.monoToMultiLayer(envMap, null);
            yamlContent = objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            log.error("获取配置失败", e);
        }
        return yamlContent;
    }

    @PostMapping("save")
    public String change(@RequestBody Map<String, Object> map) {
        String ymlContent = (String) map.get("yml");
        PropertyTrigger.change(ymlContent);
        return "success";
    }

}
