package com.wx.dao;

import com.wx.domain.beans.PageQuery;
import com.wx.domain.entity.SysAcl;
import com.wx.inter.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 22343
 * @version 1.0
 */
public interface SysAclMapper extends BaseDao<Integer,SysAcl> {
	List<SysAcl> findListByAclModuleId(@Param("aclModuleId") Integer aclModuleId,@Param("pageQuery") PageQuery pageQuery);
	
	List<SysAcl> findAll();
	
	List<SysAcl> findListByUserId(int userId);
	
	List<SysAcl> findListByUserIdList(List<Integer> userAclIdList);
	
	List<SysAcl> getByUrl(String url);
	
	List<SysAcl> findListByAclId(List<Integer> aclIdList);
}
