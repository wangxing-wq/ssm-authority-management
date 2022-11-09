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
public class SysAcl {
    /**
    * 权限id
    */
    private Integer id;

    /**
    * 权限码
    */
    private String code;

    /**
    * 权限名称
    */
    private String name;

    /**
    * 权限所在的权限模块id
    */
    private Integer aclModuleId;

    /**
    * 请求的url, 可以填正则表达式
    */
    private String url;

    /**
    * 类型，1：菜单，2：按钮，3：其他
    */
    private Integer type;

    /**
    * 状态，1：正常，0：冻结
    */
    private Integer status;

    /**
    * 权限在当前模块下的顺序，由小到大
    */
    private Integer seq;

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
    * 最后一个更新者的ip地址
    */
    private String operateIp;
}
