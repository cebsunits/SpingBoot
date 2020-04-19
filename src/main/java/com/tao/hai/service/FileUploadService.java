package com.tao.hai.service;

import com.tao.hai.base.BaseService;
import com.tao.hai.bean.common.FileBean;
import com.tao.hai.json.AjaxJson;

public interface FileUploadService extends BaseService<FileBean> {
    /**
     * 判断一个文件是否存在
     * @param pathUrl FileBean中存的路径
     * @return
     */
    boolean isExist(String pathUrl);
    /**删除*/
    AjaxJson deleteFile(String... ids);
}
