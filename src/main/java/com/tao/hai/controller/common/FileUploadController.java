package com.tao.hai.controller.common;

import com.github.pagehelper.PageInfo;
import com.tao.hai.base.ParameterModelBean;
import com.tao.hai.bean.common.FileBean;
import com.tao.hai.config.FileConfig;
import com.tao.hai.json.AjaxJson;
import com.tao.hai.json.AjaxSuccess;
import com.tao.hai.service.FileUploadService;
import com.tao.hai.util.DateUtils;
import com.tao.hai.util.FileUtil;
import com.tao.hai.util.ParameterModelBeanUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipOutputStream;

/**
 * 文件上传功能
 */
@Controller
@RequestMapping(value = "/common/fileUpload")
@Slf4j
public class FileUploadController {

    @Resource
    FileConfig fileConfig;
    @Resource
    FileUploadService fileUploadService;

    @GetMapping()
    @RequiresPermissions("common:file:page")
    public String index() {
        return "common/file/file";
    }

    @RequiresPermissions("common:file:page")
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(FileBean fileBean, HttpServletRequest request) {
        /**Sort对象初始化错误，需要更换方法解决*/
        ParameterModelBean bean = ParameterModelBeanUtil.getParameterModelBean(fileBean);
        String sortName = request.getParameter("sortName");
        String sortOrder = request.getParameter("sortOrder");
        if (StringUtils.isEmpty(sortName)) {
            sortName = "createDate";
        }
        if (StringUtils.isEmpty(sortOrder)) {
            sortOrder = "asc";
        }
        /**排序方式*/
        bean.setOrder(sortName);
        bean.setSort(sortOrder);
        PageInfo<FileBean> pageInfo = fileUploadService.getPageList(bean);
        return pageInfo;
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("common:file:batchRemove")
    public AjaxJson batchRemove(String[] ids) {
        AjaxJson ajaxJson = fileUploadService.deleteFile(ids);
        return ajaxJson;
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("common:file:remove")
    public AjaxJson remove(String fileId, HttpServletRequest request) {
        AjaxJson ajaxJson = fileUploadService.deleteFile(fileId);
        return ajaxJson;
    }

    /**
     * 查看
     */
    @GetMapping("/view")
    @RequiresPermissions("common:file:view")
    public String view(String id, HttpServletRequest request) {
        return "common/file/fileRead";
    }

    /**
     * 查看
     */
    @PostMapping("/read")
    @RequiresPermissions("common:file:view")
    public Object read(String id, HttpServletRequest request) {


        return "common/file/fileRead";
    }

    @ResponseBody
    @PostMapping("/upload")
    public AjaxJson upload(@RequestParam("files") MultipartFile[] multipartFiles, HttpServletRequest request) {
        AjaxJson ajaxJson = new AjaxSuccess();
        StringBuffer sb = new StringBuffer();
        String businessType = request.getParameter("businessType");
        String filePath = fileConfig.getFileUploadUrl();
        if (StringUtils.isNotEmpty(businessType)) {
            filePath = filePath + File.separator + businessType;
        }
        for (MultipartFile multipartFile : multipartFiles) {
            String fileName = multipartFile.getOriginalFilename();
            String fileType = multipartFile.getContentType();
            long fileSize = multipartFile.getSize();
            FileBean fileBean = new FileBean();
            fileBean.setFilePath(filePath);
            fileBean.setFileName(fileName);
            fileBean.setFileSize(fileSize);
            fileBean.setFileType(fileType);
            try {
                FileUtil.uploadFile(fileBean.getFilePath(), fileBean.getFileName(), multipartFile);
                sb.append("<br/>").append(fileName).append("上传成功");
            } catch (IOException e) {
                e.printStackTrace();
                sb.append("<br/>").append(fileName).append("上传失败").append(e.getMessage());
            }
            int saveNum = fileUploadService.save(fileBean);
            if (saveNum <= 0) {
                log.error(fileName + "保存数据库失败");
            }
        }
        ajaxJson.setMessage(sb.toString());
        return ajaxJson;
    }

    /**
     * 下载
     */
    @PostMapping("/download")
    @RequiresPermissions("common:file:download")
    public void download(String fileId, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isNotEmpty(fileId)) {
            FileBean file = new FileBean();
            file.setFileId(fileId);
            FileBean fileBean = fileUploadService.getByKey(file);
            if (fileBean != null) {
                String fileName = fileBean.getFileName();
                // 清空输出流
                response.reset();
                //UTF-8
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                // 设定输出文件头
                response.setHeader("Content-disposition", "attachment; filename="
                        + FileUtil.GBToISO(fileName)
                );
                // 定义输出类型
                response.setContentType(fileBean.getFileType());
                //文件路径
                String filePath = fileBean.getFilePath() + File.separator + fileBean.getFileName();
                try {
                    OutputStream outputStream = response.getOutputStream();
                    FileUtil.download(filePath, outputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 批量下载
     */
    @PostMapping("/batchDownload")
    @RequiresPermissions("common:file:downloads")
    public void batchDownload(String[] ids, String zipFileName,HttpServletRequest request, HttpServletResponse response) {
        if (ids != null && ids.length > 0) {
            List<String> filePaths = new ArrayList<String>();
            for (String fileId : ids) {
                FileBean file = new FileBean();
                file.setFileId(fileId);
                FileBean fileBean = fileUploadService.getByKey(file);
                if (ObjectUtils.isNotEmpty(fileBean)) {
                    String filePath = fileBean.getFilePath() + File.separator + fileBean.getFileName();
                    filePaths.add(filePath);
                }
            }
            if (StringUtils.isEmpty(zipFileName)) {
                zipFileName = DateUtils.getDate();
            }
            response.reset(); // 重点突出
            response.setCharacterEncoding("UTF-8"); // 重点突出
            response.setContentType("application/zip"); // 不同类型的文件对应不同的MIME类型 // 重点突出
            // 对文件名进行编码处理中文问题
            zipFileName = new String(zipFileName.getBytes(), StandardCharsets.UTF_8);
            // inline在浏览器中直接显示，不提示用户下载
            // attachment弹出对话框，提示用户进行下载保存本地
            // 默认为inline方式
            response.setHeader("Content-Disposition", "attachment;filename=" + zipFileName+".zip");
            /**压缩包*/
            try {
                ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());
                FileUtil.zipFile(filePaths, zipOutputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /***解决跨域问题*/
    @ApiOperation(notes="文件下载",value="文件下载")
    @GetMapping("/downloadFile")
    public ResponseEntity<FileSystemResource> downloadFile(String fileId, HttpServletResponse response) {
        try {
            if (StringUtils.isNotEmpty(fileId)) {
                FileBean bean = new FileBean();
                bean.setFileId(fileId);
                FileBean fileBean = fileUploadService.getByKey(bean);
                if (fileBean != null) {
                    String fileName = DateUtils.getDate() + fileBean.getFileName();
                    fileName = new String(fileName.getBytes(), StandardCharsets.UTF_8);
                    //文件路径
                    String filePath = fileBean.getFilePath() + File.separator + fileBean.getFileName();
                    File file = new File(filePath);
                    //
                    HttpHeaders headers = new HttpHeaders();
                    headers.add("Cache-Control", "no-cache,no-store,must-revalidate");
                    headers.add("Content-Disposition", "attachment;filename=" + fileName);
                    headers.add("Pragma", "no-cache");
                    headers.add("Expires", "0");
                    headers.add("Last-Modified", new Date().toString());
                    headers.add("ETag", String.valueOf(System.currentTimeMillis()));
                    return ResponseEntity.ok().headers(headers).contentLength(file.length())
                            .contentType(MediaType.parseMediaType(fileBean.getFileType()))
                            .body(new FileSystemResource(file));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("fileId:{}下载文件失败，cause:{}",fileId,e.getMessage());
        } finally {
        }
        return ResponseEntity.status(0).build();
    }
}
