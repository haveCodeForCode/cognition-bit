package com.cognition.bit.common.until;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 文件工具
 *
 * @author Worry
 * @version 2019/9/14
 */
public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * java文件写出
     *
     * @param file
     * @param filePath
     * @param fileName
     * @throws IOException
     */
    public static void uploadFile(byte[] file, String filePath, String fileName) throws IOException {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath + fileName);
        out.write(file);
        out.flush();
        out.close();
    }


    /**
     * 按字节读取文件
     *
     * @param filename
     */
    public static void ReadFileByBytes(String filename) {
        File file = new File(filename);
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            int index = 0;
            System.out.println("-----------------读取文件------------------");
            while (-1 != (index = is.read())) {
                System.out.write(index);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }// TODO Auto-generated catch block
        finally {
            try {
                if (null != is) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除文件
     *
     * @param fileName
     * @return
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    public static String renameToUUID(String fileName) {
        return UUID.randomUUID() + "." + fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 文件类型
     *
     * @param fileName
     * @return
     */
    public static Map<String,String> fileType(String fileName) {
        Map<String,String> mapGroup = new HashMap<>();
        if (fileName == null) {
            mapGroup.put("state","500");
            mapGroup.put("Suffix","文件名为空！");
            return mapGroup;

        } else {
            // 获取文件后缀名并转化为写，用于后续比较
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();
            // 创建图片类型数组0
            String[] img = {"bmp", "jpg", "jpeg", "png", "tiff", "gif", "pcx", "tga", "exif", "fpx", "svg", "psd", "cdr", "pcd", "dxf", "ufo", "eps", "ai", "raw", "wmf"};
            // 创建文档类型数组1
            String[] document = {"txt", "doc", "docx", "xls", "htm", "html", "jsp", "rtf", "wpd", "pdf", "ppt"};
            // 创建视频类型数组2
            String[] video = {"mp4", "avi", "mov", "wmv", "asf", "navi", "3gp", "mkv", "f4v", "rmvb", "webm"};
            // 创建音乐类型数组3
            String[] music = {"mp3", "wma", "wav", "mod", "ra", "cd", "md", "asf", "aac", "vqf", "ape", "mid", "ogg", "m4a", "vqf"};

            for (String s : img) {
                if (s.equals(fileType)) {
                    mapGroup.put("state","0");
                    mapGroup.put("Suffix","img");
                    return mapGroup;
                }
            }

            for (String s : document) {
                if (s.equals(fileType)) {
                    mapGroup.put("state","1");
                    mapGroup.put("Suffix","document");
                    return mapGroup;
                }
            }

            for (String s : video) {
                if (s.equals(fileType)) {
                    mapGroup.put("state","2");
                    mapGroup.put("Suffix","video");
                    return mapGroup;
                }
            }

            for (String s : music) {
                if (s.equals(fileType)) {
                    mapGroup.put("state","3");
                    mapGroup.put("Suffix","music");
                    return mapGroup;
                }
            }

        }
        mapGroup.put("state","500");
        mapGroup.put("Suffix","无对应类型！！");
        return mapGroup;
    }
}
