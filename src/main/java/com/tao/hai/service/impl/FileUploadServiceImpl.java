package com.tao.hai.service.impl;

import com.tao.hai.base.BaseServiceImpl;
import com.tao.hai.bean.common.FileBean;
import com.tao.hai.dao.common.FileDao;
import com.tao.hai.json.AjaxError;
import com.tao.hai.json.AjaxJson;
import com.tao.hai.json.AjaxSuccess;
import com.tao.hai.service.FileUploadService;
import com.tao.hai.util.FileUtil;
import com.tao.hai.util.ShiroUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.UUID;

@Service
public class FileUploadServiceImpl extends BaseServiceImpl<FileDao, FileBean> implements FileUploadService {
    @Resource
    FileDao fileDao;
    @Override
    public boolean isExist(String pathUrl) {
        return fileDao.isExist(pathUrl);
    }
    /***保存*/
    public int save(FileBean fileBean){
        if(StringUtils.isEmpty(fileBean.getFileId())){
            fileBean.setFileId(UUID.randomUUID().toString());
            fileBean.setCreateDate(new Date());
            fileBean.setCreateId(ShiroUtils.getUserId());
        }else{
            fileBean.setNewRecord(false);
        }
        fileBean.setUpdateId(ShiroUtils.getUserId());
        fileBean.setUpdateDate(new Date());
        int num=super.save(fileBean);
        return num;
    }

    /**批量删除功能*/
    public AjaxJson deleteFile(String... ids){
        AjaxJson ajaxJson;
        StringBuffer sb=new StringBuffer();
        if(ids!=null&&ids.length>0){
            for(String id:ids){
                FileBean fileBean = new FileBean();
                fileBean.setFileId(id);
                FileBean file = super.getByKey(fileBean);
                if (file != null) {
                    String filePath = file.getFilePath() + File.separator + file.getFileName();
                    if (super.delete(id) > 0) {
                        boolean b = FileUtil.deleteFile(filePath);
                        if (!b) {
                            sb.append(file.getFileName()).append("数据库记录删除成功，文件删除失败");
                        }
                        sb.append(file.getFileName()).append("删除成功");
                    }
                }else{
                    sb.append(id).append("删除失败");
                }
            }
            ajaxJson = new AjaxSuccess();
            ajaxJson.setMessage(sb.toString());
        }else{
            ajaxJson = new AjaxError();
            ajaxJson.setMessage("未获取到文件ID，删除失败！");
        }
        return ajaxJson;
    }
}
