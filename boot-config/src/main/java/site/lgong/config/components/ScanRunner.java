package site.lgong.config.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;

/**
 * spring启动最后执行-扫描类相关功能
 *
 * @author: liudayue
 * @date: 2022-01-28 16:57
 **/
@Slf4j
@Component
public class ScanRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {

        String rootPath = this.getClass().getResource("/").getPath();
        List<String> fileList = FileScanner.findFileByType(rootPath, null, FileScanner.TYPE_CLASS);
        doFilter(rootPath, fileList);
        EnvInitializer.init();
    }

    private void doFilter(String rootPath, List<String> fileList) {
        rootPath = FileScanner.getRealRootPath(rootPath);
        for (String fullPath : fileList) {
            String shortName = fullPath.replace(rootPath, "").replace(FileScanner.TYPE_CLASS, "");
            String packageFileName = shortName.replace(Matcher.quoteReplacement(File.separator), "\\.");

            try {
                Class<?> clazz = Class.forName(packageFileName);
                // 过滤保留spring管理的类
                if (clazz.isAnnotationPresent(Component.class) || clazz.isAnnotationPresent(Controller.class) || clazz.isAnnotationPresent(Service.class)) {
                    // 将类中的变量放入变量池
                    VariablePool.add(clazz);
                }
            } catch (Exception e) {
                log.error("类加载失败", e);
            }
        }
    }
}
