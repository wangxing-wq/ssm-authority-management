package com.wx.dao;

import com.wx.domain.entity.SysRoleAcl;

import java.util.List;

/**
 * @author 22343
 * @date 2022/10/22 11:04
 * @version 1.0
 */
public interface SysRoleAclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleAcl record);

    int insertSelective(SysRoleAcl record);

    SysRoleAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRoleAcl record);

    int updateByPrimaryKey(SysRoleAcl record);
	
	List<Integer> findAclListByRoleId(int roleId);
    
    List<Integer> findAclListByUserId(Integer id);
	
	int batchInsert(List<SysRoleAcl> sysRoleAclList);
	
	int deleteByRoleId(int roleId);
}
