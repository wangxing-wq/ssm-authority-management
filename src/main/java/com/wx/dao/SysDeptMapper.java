package com.wx.dao;

import com.wx.domain.entity.SysDept;

import java.util.List;

/**
 * @author 22343
 * @date 2022/10/22 11:04
 * @version 1.0
 */
public interface SysDeptMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysDept record);

    int insertSelective(SysDept record);

    SysDept findById(Integer id);

    int updateByPrimaryKeySelective(SysDept record);

    int updateByPrimaryKey(SysDept record);
    
    List<SysDept> findAll();
    
    List<Integer> selectBySelective(SysDept continuation);
    
    List<SysDept> selectChildDeptListByLevel(String level);
    
    int deleteByIdList(List<Integer> sysUserIdList);
    
    List<Integer> findChildrenByLevel(String level);
}
