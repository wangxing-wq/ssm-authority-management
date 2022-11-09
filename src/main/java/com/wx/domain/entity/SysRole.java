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
public class SysRole {
    /**
    * 角色id
    */
    private Integer id;

    private String name;

    /**
    * 角色的类型，1：管理员角色，2：其他
    */
    private Integer type;

    /**
    * 状态，1：可用，0：冻结
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
    * 最后一次更新的时间
    */
    private Date operateTime;

    /**
    * 最后一次更新者的ip地址
    */
    private String operateIp;
}
