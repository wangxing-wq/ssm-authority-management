package com.wx.dao;

import com.wx.domain.entity.SysAclModule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 22343
 * @date 2022/10/22 11:04
 * @version 1.0
 */
public interface SysAclModuleMapper {
    
    int deleteByPrimaryKey(Integer id);

    int insert(SysAclModule record);

    int insertSelective(SysAclModule record);

    SysAclModule selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAclModule record);

    int updateByPrimaryKey(SysAclModule record);
    
    List<SysAclModule> findAll();
    
    boolean selectByNameNotId(@Param("name") String name,@Param("id") Integer id);
    
    List<SysAclModule> selectChildDeptListByLevel(String level);
}
