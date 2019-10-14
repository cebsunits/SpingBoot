package com.tao.hai.bean;

import com.tao.hai.base.BaseDataEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name="sys_dept")
public class Dept extends BaseDataEntity<Dept> {

    /**
     * 部门唯一标识
     */
    @Id
    @Column(name="dept_id")
    private String deptId;
    /**
     * 父级编号
     */
    @Column(name = "parent_id")
    private String parentId;
    /**
     * 部门层级
     */
    @Column(name = "dept_level")
    private String deptLevel;

    /**
     * 部门编码
     */
    @Column(name="dept_code")
    private String deptCode;
    /**
     * 部门名称
     */
    @Column(name="dept_name")
    private String deptName;
    /**
     * 部门排序
     */
    @Column(name="dept_sort")
    private String deptSort;
    //菜单子列表
    @Transient
    private List<Dept> subDeptList;
}
