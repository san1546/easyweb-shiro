package com.hc.hyh.system.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 权限表
 */
@Data
@TableName("sys_authorities")
public class Authorities {
    @TableId
    private Integer authorityId;  // 权限id

    private String authorityName;  // 权限名称

    private String authority;  // 权限标识（如果为空不会添加在shiro的权限列表中）

    private String menuUrl;  // 菜单url

    private Integer parentId;  // 上级菜单

    private Integer isMenu;  // 菜单还是按钮（菜单会显示在侧导航，按钮不会显示在侧导航，只要url不是空，都会作为权限标识）

    private Integer orderNumber;  // 排序号

    private String menuIcon;  // 菜单图标

    private Date createTime;  // 创建时间

    private Date updateTime;  // 修改时间

}
