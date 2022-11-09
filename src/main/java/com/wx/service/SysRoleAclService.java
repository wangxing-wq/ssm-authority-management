package com.wx.service;

import com.wx.dao.SysRoleAclMapper;
import com.wx.domain.entity.SysRoleAcl;
import com.wx.helper.RequestHolder;
import com.wx.util.IpUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 22343
 * @version 1.0
 */
@Service
public class SysRoleAclService {
	
	@Resource
	private SysRoleAclMapper sysRoleAclMapper;
	
	
	public int deleteByPrimaryKey(Integer id) {
		return sysRoleAclMapper.deleteByPrimaryKey(id);
	}
	
	
	public int insert(SysRoleAcl record) {
		return sysRoleAclMapper.insert(record);
	}
	
	
	public int insertSelective(SysRoleAcl record) {
		return sysRoleAclMapper.insertSelective(record);
	}
	
	
	public SysRoleAcl selectByPrimaryKey(Integer id) {
		return sysRoleAclMapper.selectByPrimaryKey(id);
	}
	
	
	public int updateByPrimaryKeySelective(SysRoleAcl record) {
		return sysRoleAclMapper.updateByPrimaryKeySelective(record);
	}
	
	
	public int updateByPrimaryKey(SysRoleAcl record) {
		return sysRoleAclMapper.updateByPrimaryKey(record);
	}
	
	@Transactional(rollbackFor=Exception.class)
	public void changeRoleAclList(int roleId,List<Integer> aclIdList) {
		//    校验登录用户是否有这个权限
		//    查询本身权限
		List<Integer> aclListByRoleId = sysRoleAclMapper.findAclListByRoleId(roleId);
		// int originCount = aclListByRoleId.size();
		// aclListByRoleId.removeAll(aclIdList);
		// if (originCount != 0 && CollectionUtils.isEmpty(aclListByRoleId)){
		// 	return;
		// }
		sysRoleAclMapper.deleteByPrimaryKey(roleId);
		List<SysRoleAcl> sysRoleAclList = aclIdList.stream().map(item -> SysRoleAcl.builder().roleId(roleId).aclId(item).operator(RequestHolder.getCurrentUser().getUsername()).operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest())).operateTime(new Date()).build()).collect(Collectors.toList());
		// 删除原来的
		int deleteCount = sysRoleAclMapper.deleteByRoleId(roleId);
		// 获取修改的
		int count = sysRoleAclMapper.batchInsert(sysRoleAclList);
	}
}
