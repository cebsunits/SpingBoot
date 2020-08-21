package com.tao.hai.base;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class BaseDataEntity<T> extends DataEntity<T> {
    /**
     * 删除标记（0：正常；1：删除；2：审核；）
     */
    @JSONField(serialize = false)
    public static final String DEL_FLAG_NORMAL = "0";
    @JSONField(serialize = false)
    public static final String DEL_FLAG_DELETE = "1";

    public BaseDataEntity(){
        this.delFlag=DEL_FLAG_NORMAL;
    }
    /**
     * 创建人
     */
    private String createId;
    /**
     * 更新人
     */
    private String updateId;
    /**
     * 创建日期
     */
    //格式化前台页面收到的json时间格式，不指定的话会变成缺省的"yyyy-MM-dd'T'HH:mm:ss"
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    /**
     * 更新日期
     */
    //格式化前台页面收到的json时间格式，不指定的话会变成缺省的"yyyy-MM-dd'T'HH:mm:ss"
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;
    /**
     * 是否有效，0 有效 1 无效
     */
    private String delFlag;
}