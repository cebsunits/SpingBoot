package com.tao.hai.base;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

public class BaseDataEntity<T> implements Serializable {
    /**
     * 创建日期
     */
    @Column(name="create_date")
    private Date createDate;
    /**
     * 更新日期
     */
    @Column(name="update_date")
    private Date updateDate;
    /**
     * 是否有效，0 有效 1 无效
     */
    @Column(name="del_flag")
    private String delFlag;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
}
