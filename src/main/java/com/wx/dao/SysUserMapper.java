package com.wx.dao;

import com.wx.domain.beans.PageQuery;
import com.wx.domain.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 22343
 * @date 2022/10/22 11:04
 * @version 1.0
 */
public interface SysUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);
    SysUser selectByUsername(String username);
    
    SysUser selectByCondition(SysUser condition);
    
    List<SysUser> selectByPage(@Param("deptId") int deptId,@Param("pageQuery") PageQuery pageQuery);
    
    List<SysUser> findUserListByRoleId(int roleId);
    
    List<SysUser> findAll();
    
}
