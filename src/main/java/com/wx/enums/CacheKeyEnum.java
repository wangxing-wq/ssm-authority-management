package com.wx.enums;

import lombok.Getter;

/**
 * 缓存键枚举
 * @author 22343
 * 缓存Key
 */

@Getter
public enum CacheKeyEnum {
    
    
    /**
     * 系统acl
     */
    SYSTEM_ACL("系统权限点"),
    /**
     * 用户acl
     */
    USER_ACL("角色权限点");
    /**
     * 消息
     */
    private final String message;
    
    /**
     * 缓存键枚举
     * @param message 消息
     */
    private CacheKeyEnum(String message) {
		this.message = message;
	}
	
 
}
