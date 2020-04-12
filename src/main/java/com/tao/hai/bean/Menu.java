package com.tao.hai.bean;

import com.tao.hai.base.BaseDataEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "sys_menu")
public class Menu extends BaseDataEntity<Menu> {
    /**
     * 菜单唯一标识
     */
    @Id
    @Column(name = "menu_id")
    private String menuId;
    /**
     * 父级编号
     */
    @Column(name = "parent_id")
    private String parentId;
    /**
     * 菜单名称
     */
    @Column(name = "menu_name")
    private String menuName;
    /**
     * 菜单Url
     */
    @Column(name = "menu_url")
    private String menuUrl;
    /**
     * 菜单类型
     */
    @Column(name = "menu_Type")
    private String menuType;
    /**
     * 是否显示 0：隐藏 1：显示
     */
    @Column(name = "is_Show")
    private Integer isShow;
    /**
     * 菜单排序
     */
    @Column(name = "menu_sort")
    private Integer menuSort;
    /**
     * 菜单图标
     */
    @Column(name = "menu_icon")
    private String menuIcon;
    /**
     * 权限字符串,menu例子：role:*，button例子：role:create,role:update,role:delete,role:view
     */
    @Column(name = "permission")
    private String permission;
    @Transient
    private Menu parent;

    /***获取根目录ID***/
    public static String getRootId() {
        return "1";
    }
}
