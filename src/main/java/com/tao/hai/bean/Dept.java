package com.tao.hai.bean;

import com.tao.hai.annotation.FieldQuery;
import com.tao.hai.base.BaseDataEntity;
import com.tao.hai.constants.SqlParamConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "sys_dept")
public class Dept extends BaseDataEntity<Dept> {

    /**
     * 部门唯一标识
     */
    @Id
    @Column(name = "dept_id")
    private String deptId;
    /**
     * 父级编号
     */
    @Column(name = "parent_id")
    private String parentId;

    /**
     * 部门编码
     */
    @Column(name = "dept_code")
    private String deptCode;
    /**
     * 部门名称
     */
    @Column(name = "dept_name")
    @FieldQuery(value = SqlParamConstant.LIKE)
    private String deptName;
    /**
     * 部门排序
     */
    @Column(name = "dept_sort")
    private Integer deptSort;
    @Transient
    private Dept parent;
}
