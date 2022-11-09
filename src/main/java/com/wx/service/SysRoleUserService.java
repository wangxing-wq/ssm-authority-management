package com.wx.service;

import com.wx.dao.SysRoleUserMapper;
import com.wx.dao.SysUserMapper;
import com.wx.domain.entity.SysRoleUser;
import com.wx.domain.entity.SysUser;
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
public class SysRoleUserService {
	
	@Resource
	private SysRoleUserMapper sysRoleUserMapper;
	
	@Resource
	private SysUserMapper sysUserMapper;
	
	
	public int deleteByPrimaryKey(Integer id) {
		return sysRoleUserMapper.deleteByPrimaryKey(id);
	}
	
	
	public int insert(SysRoleUser record) {
		return sysRoleUserMapper.insert(record);
	}
	
	
	public int insertSelective(SysRoleUser record) {
		return sysRoleUserMapper.insertSelective(record);
	}
	
	
	public SysRoleUser selectByPrimaryKey(Integer id) {
		return sysRoleUserMapper.selectByPrimaryKey(id);
	}
	
	
	public int updateByPrimaryKeySelective(SysRoleUser record) {
		return sysRoleUserMapper.updateByPrimaryKeySelective(record);
	}
	
	
	public int updateByPrimaryKey(SysRoleUser record) {
		return sysRoleUserMapper.updateByPrimaryKey(record);
	}
	
	public List<SysUser> getListByRoleId(int roleId) {
		return sysUserMapper.findUserListByRoleId(roleId);
	}
	
    @Transactional(rollbackFor = Exception.class)
	public void changeRoleUsers(int roleId,List<Integer> userIdList) {
		// TODO: 2022/11/6 校验权限
		// 删除这个用户的本来有的用户
		// 添加用户
		int roleUserCount = sysRoleUserMapper.deleteByRoleId(roleId);
		List<SysRoleUser> sysRoleUserList = userIdList.stream().map(item -> SysRoleUser.builder().roleId(roleId).userId(item).operator(RequestHolder.getCurrentUser().getUsername()).operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest())).operateTime(new Date()).build()).collect(Collectors.toList());
		int insertCount = sysRoleUserMapper.batchInsert(sysRoleUserList);
	}
}
