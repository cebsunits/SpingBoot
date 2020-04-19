package com.tao.hai.util;

import com.tao.hai.bean.common.FileBean;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtil {
    /**
     * 上传文件
     */
    public static void uploadFile(String filePath, String fileName, MultipartFile multipartFile) throws IOException {
        File file = new File(filePath + File.separator + fileName);
        /**不存在创建目录*/
        if(!file.getParentFile().exists()){
            file.mkdirs();
        }
        multipartFile.transferTo(file);
    }

    /**
     * 上传文件
     */
    public static void uploadFile(String filePath, String fileName, byte[] file) throws IOException {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(filePath + fileName);
        fileOutputStream.write(file);
        fileOutputStream.flush();
        fileOutputStream.close();
    }


    /**
     * 删除文件
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            return file.delete();
        } else {
            return false;
        }
    }

    /**
     * 转换中文输入
     */
    public static String GBToISO(String gb) {
        try {
            return gb == null ? gb : new String(gb.getBytes("GB2312"),
                    StandardCharsets.ISO_8859_1);
        } catch (Exception e) {
            return gb;
        }
    }
    /**
     * 下载文件
     * outputStream 输出流
     */
    public static void download(String filePath,  OutputStream outputStream) {
        File file=new File(filePath);
        //创建输入流读取文件
        BufferedInputStream bufferedInputStream=null;
        try {
            if(file.exists()){
                //创建输入流读取文件
                bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                //输出流
                byte[] buffer = new byte[1024];


                //写入文件的方法
                int size = 0;
                while ((size = bufferedInputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, size);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭输入流
            if(bufferedInputStream!=null){
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 下载并压缩
     */
    public static void zipFile(List<String> filePaths, ZipOutputStream zipOutputStream) {
        byte[] buffer = new byte[1024];
        try {
            for (String filePath : filePaths) {
                File inputFile = new File(filePath);
                if (inputFile.exists()) {
                    if (inputFile.isFile()) {
                        //创建输入流读取文件
                        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(inputFile));
                        //将文件写入zip内，即将文件进行打包
                        zipOutputStream.putNextEntry(new ZipEntry(inputFile.getName()));
                        //写入文件的方法
                        int size = 0;
                        while ((size = bufferedInputStream.read(buffer)) > 0) {
                            zipOutputStream.write(buffer, 0, size);
                        }
                        //关闭输入输出流
                        zipOutputStream.closeEntry();
                        bufferedInputStream.close();
                    } else {//如果是文件夹，则使用穷举的方法获取文件，写入zip
                        File[] listFiles = inputFile.listFiles();
                        List<String> paths = new ArrayList<>();
                        for (File fileItem : listFiles) {
                            paths.add(fileItem.toString());
                        }
                        zipFile(paths, zipOutputStream);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipOutputStream != null) {
                try {
                    zipOutputStream.flush();
                    zipOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
