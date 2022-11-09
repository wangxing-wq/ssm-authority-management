package com.wx.domain.entity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 22343
 * @date 2022/10/22 11:04
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysLog {
    private Integer id;

    /**
    * 权限更新的类型，1：部门，2：用户，3：权限模块，4：权限，5：角色，6：角色用户关系，7：角色权限关系
    */
    private Integer type;

    /**
    * 基于type后指定的对象id，比如用户、权限、角色等表的主键
    */
    private Integer targetId;

    /**
    * 旧值
    */
    private String oldValue;

    /**
    * 新值
    */
    private String newValue;

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

    /**
    * 当前是否复原过，0：没有，1：复原过
    */
    private Integer status;
}
