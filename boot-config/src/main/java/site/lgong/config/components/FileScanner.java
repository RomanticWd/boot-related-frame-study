package site.lgong.config.components;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * 文件扫描-根据文件后缀扫描文件
 *
 * @author: liudayue
 * @date: 2022-01-28 17:01
 **/
public class FileScanner {

    public static final String TYPE_CLASS = ".class";

    public static final String TYPE_YML = ".yml";

    public static List<String> findFileByType(String rootPath, List<String> fileList, String fileType) {

        if (fileList == null) {
            fileList = new ArrayList<>();
        }

        File rootFile = new File(rootPath);
        // 不是文件目录，将当前文件加入文件列表
        if (!rootFile.isDirectory()) {
            addFile(rootFile.getPath(), fileList, fileType);
        } else {
            // 文件目录，遍历下级目录
            String[] subFileList = rootFile.list();
            for (String file : subFileList) {
                String subFilePath = rootPath + "/" + file;
                File subFile = new File(subFilePath);
                if (!subFile.isDirectory()) {
                    addFile(subFile.getPath(), fileList, fileType);
                } else {
                    // 递归遍历
                    findFileByType(subFilePath, fileList, fileType);
                }
            }
        }
        return fileList;
    }

    private static void addFile(String fileName, List<String> fileList, String fileType) {
        // 将后缀类型为fileType的加入集合
        if (fileName.endsWith(fileType)) {
            fileList.add(fileName);
        }
    }

    public static String getRealRootPath(String rootPath) {
        if (System.getProperty("os.name").startsWith("Windows") && rootPath.startsWith("/")) {
            // "unhappy".substring(2) returns "happy"
            rootPath = rootPath.substring(1);
            rootPath = rootPath.replaceAll("/", Matcher.quoteReplacement(File.separator));
        }
        return rootPath;
    }

}
