package com.wx.dao;

import com.wx.domain.entity.SysRole;

import java.util.List;

/**
 * @author 22343
 * @date 2022/10/22 11:04
 * @version 1.0
 */
public interface SysRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);
	List<SysRole> findAll();
	SysRole findByName(String name);
	List<SysRole> findByUserId(int userId);
}
