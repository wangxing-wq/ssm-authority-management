package com.wx.enums;

import com.wx.inter.StateCode;

/**
 * @author 22343
 * @version 1.0
 * 业务状态码定义<br/>
 * 1xxx 代表 ACl日志<br/>
 * 2xxx 代表 LOG日志<br/>
 * 3xxx 代表 ROLE日志<br/>
 * 4xxx 代表 Dept日志<br/>
 * 5xxx 代表 USER日志<br/>
 */

public enum AclEnum implements StateCode {
	/**
	 * Acl业务代码
	 */
	INSERT("11001","权限点插入失败"),
	DELETE("12001","权限点删除失败"),
	UPDATE("13001","权限点修改失败"),
	SELECT("14001","权限点查询失败"),
	BIND_NAME("15001","权限点绑定名称已存在"),
	BIND_URL_EXISTS("15002","权限点绑定URL已存在"),
	BIND_ACL_MODULE_NO_EXISTS("15003","权限点名称绑定权限模块不存在"),
	;
	
	private final String code;
	private final String message;
	
	AclEnum(String code,String message) {
		this.code = code;
		this.message = message;
	}
	
	@Override
	public String getCode() {
		return this.code;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
