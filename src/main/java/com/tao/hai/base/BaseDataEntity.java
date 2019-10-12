package com.tao.hai.base;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.util.Date;
@Data
public class BaseDataEntity<T> extends DataEntity<T> {
    /**
     * 创建日期
     */
    //格式化前台页面收到的json时间格式，不指定的话会变成缺省的"yyyy-MM-dd'T'HH:mm:ss"
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name="create_date")
    private Date createDate;
    /**
     * 更新日期
     */
    //格式化前台页面收到的json时间格式，不指定的话会变成缺省的"yyyy-MM-dd'T'HH:mm:ss"
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name="update_date")
    private Date updateDate;
    /**
     * 是否有效，0 有效 1 无效
     */
    @Column(name="del_flag")
    private String delFlag;
}
