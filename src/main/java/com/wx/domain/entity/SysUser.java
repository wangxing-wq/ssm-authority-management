package com.wx.domain.entity;

import java.io.Serializable;
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
public class SysUser implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
    * 用户id
    */
    private Integer id;

    /**
    * 用户名称
    */
    private String username;

    /**
    * 手机号
    */
    private String telephone;

    /**
    * 邮箱
    */
    private String mail;

    /**
    * 加密后的密码
    */
    private String password;

    /**
    * 用户所在部门的id
    */
    private Integer deptId;

    /**
    * 状态，1：正常，0：冻结状态，2：删除
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
    * 最后一次更新时间
    */
    private Date operateTime;

    /**
    * 最后一次更新者的ip地址
    */
    private String operateIp;
}
