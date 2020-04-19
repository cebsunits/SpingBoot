package com.tao.hai.dao.common;

import com.tao.hai.base.BaseDao;
import com.tao.hai.bean.common.FileBean;

public interface FileDao extends BaseDao<FileBean> {
    /**
     * 判断一个文件是否存在
     * @param pathUrl FileBean中存的路径
     * @return
     */
    boolean isExist(String pathUrl);
}
