package com.tao.hai.bean.common;

import com.tao.hai.annotation.FieldQuery;
import com.tao.hai.base.BaseDataEntity;
import com.tao.hai.constants.SqlParamConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "sys_file")
public class FileBean extends BaseDataEntity<FileBean> {
    /**文件ID*/
    @Id
    @Column(name="file_id")
    private String fileId;
    /**文件路径*/
    @Column(name="file_path")
    private String filePath;
    /**文件类型*/
    @Column(name="file_type")
    private String fileType;
    /**文件名称*/
    @FieldQuery(value= SqlParamConstant.LIKE)
    @Column(name="file_name")
    private String fileName;
    /**文件大小*/
    @FieldQuery(value= SqlParamConstant.GE)
    @Column(name="file_size")
    private long fileSize;
}
