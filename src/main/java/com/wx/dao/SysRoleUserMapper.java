package com.wx.dao;

import com.wx.domain.entity.SysRoleUser;

import java.util.List;

/**
 * @author 22343
 * @date 2022/10/22 11:04
 * @version 1.0
 */
public interface SysRoleUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleUser record);

    int insertSelective(SysRoleUser record);

    SysRoleUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRoleUser record);

    int updateByPrimaryKey(SysRoleUser record);
	
	int batchInsert(List<SysRoleUser> sysRoleUserList);
    
    int deleteByRoleId(int roleId);
}
