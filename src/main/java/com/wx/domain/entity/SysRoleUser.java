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
public class SysRoleUser {
    private Integer id;

    /**
    * 角色id
    */
    private Integer roleId;

    /**
    * 用户id
    */
    private Integer userId;

    /**
    * 操作者
    */
    private String operator;

    /**
    * 最后一次更新的时间
    */
    private Date operateTime;

    /**
    * 最后一次更新者的ip地址
    */
    private String operateIp;
}
