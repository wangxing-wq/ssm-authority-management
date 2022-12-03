package com.wx.dao;

import com.wx.domain.beans.PageQuery;
import com.wx.domain.entity.SysAcl;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 22343
 * @version 1.0
 */
public interface SysAclMapper {
	int deleteByPrimaryKey(Integer id);
	
	int insert(SysAcl record);
	
	int insertSelective(SysAcl record);
	
	SysAcl selectByPrimaryKey(Integer id);
	
	int updateByPrimaryKeySelective(SysAcl record);
	
	int updateByPrimaryKey(SysAcl record);
	
	List<SysAcl> findListByAclModuleId(@Param("aclModuleId") Integer aclModuleId,@Param("pageQuery") PageQuery pageQuery);
	
	List<SysAcl> findAll();
	
	List<SysAcl> findListByUserId(int userId);
	
	List<SysAcl> findListByUserIdList(List<Integer> userAclIdList);
	
	List<SysAcl> getByUrl(String url);
	
	List<SysAcl> findListByAclId(List<Integer> aclIdList);
}
