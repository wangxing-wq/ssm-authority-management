package com.wx.domain.entity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 22343
 * @date 2022/10/22 11:04
 * @version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysAclModule {
    /**
    * 权限模块id
    */
    private Integer id;

    /**
    * 权限模块名称
    */
    private String name;

    /**
    * 上级权限模块id
    */
    private Integer parentId;

    /**
    * 权限模块层级
    */
    private String level;

    /**
    * 权限模块在当前层级下的顺序，由小到大
    */
    private Integer seq;

    /**
    * 状态，1：正常，0：冻结
    */
    private Integer status;

    /**
    * 备注
    */
    private String remark;

    /**
    * 操作者
    */
    private String operator;

    /**
    * 最后一次操作时间
    */
    private Date operateTime;

    /**
    * 最后一次更新操作者的ip地址
    */
    private String operateIp;
}
