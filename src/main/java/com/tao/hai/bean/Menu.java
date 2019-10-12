package com.tao.hai.bean;

import com.tao.hai.base.BaseDataEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name="sys_menu")
public class Menu  extends BaseDataEntity<Menu> {
    /**
     * 菜单唯一标识
     */
    @Id
    @Column(name="menu_id")
    private String menuId;
    /**
     * 父级编号
     */
    @Column(name = "parent_id")
    private String parentId;
    /**
     * 菜单名称
     */
    @Column(name="menu_name")
    private String menuName;
    /**
     * 菜单Url
     */
    @Column(name="menu_url")
    private String menuUrl;
    /**
     * 菜单类型
     */
    @Column(name="menu_Type")
    private String menuType;
    /**
     * 是否显示 0：隐藏 1：显示
     */
    @Column(name="is_Show")
    private int isShow;
    /**
     * 菜单排序
     */
    @Column(name="menu_sort")
    private String menuSort;
    //菜单子列表
    @Transient
    private List<Menu> subMenuList;

}
