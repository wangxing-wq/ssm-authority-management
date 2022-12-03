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
	;
	
	private final String code;
	private final String message;
	
	AclEnum(String code,String message){
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
