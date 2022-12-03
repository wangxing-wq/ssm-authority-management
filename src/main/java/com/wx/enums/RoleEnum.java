package com.wx.enums;

import com.wx.inter.StateCode;

/**
 * @author 22343
 * @version 1.0
 */
public enum RoleEnum implements StateCode {
	;
	
	private final String code;
	private final String message;
	
	RoleEnum(String code,String message){
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
