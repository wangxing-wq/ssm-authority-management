package com.wx.service;

import com.wx.dao.SysRoleMapper;
import com.wx.dao.SysRoleUserMapper;
import com.wx.domain.entity.SysRole;
import com.wx.exception.ParamException;
import com.wx.helper.RequestHolder;
import com.wx.param.RoleParam;
import com.wx.util.IpUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author 22343
 * @version 1.0
 */
@Service
public class SysRoleService {
	
	@Resource
	private SysRoleMapper sysRoleMapper;
	@Resource
	private SysRoleUserMapper sysRoleUserMapper;
	@Resource
	private SysLogService sysLogService;
	
	public SysRole selectByPrimaryKey(Integer id) {
		return sysRoleMapper.selectByPrimaryKey(id);
	}
	
	
	public int updateByPrimaryKeySelective(SysRole record) {
		return sysRoleMapper.updateByPrimaryKeySelective(record);
	}
	
	
	public int updateByPrimaryKey(SysRole record) {
		return sysRoleMapper.updateByPrimaryKey(record);
	}
	
	public List<SysRole> getRoleListByUserId(int userId) {
		return sysRoleMapper.findByUserId(userId);
	}
	
	public List<SysRole> getRoleListByAclId(int aclId) {
		return null;
	}
	
	public Object getUserListByRoleList(List<SysRole> roleList) {
		return null;
	}
	
	public void save(RoleParam param) {
		// TODO: 2022/11/5 确保没有被添加同名过的
		checkName(param.getName());
		SysRole build = buildSysRole(param);
		sysRoleMapper.insert(build);
		sysLogService.saveRoleLog(null, build);
	}
	
	private static SysRole buildSysRole(RoleParam param) {
		SysRole build = SysRole.builder().id(param.getId()).name(param.getName()).remark(param.getRemark()).type(param.getType()).status(param.getStatus()).build();
		build.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
		build.setOperator(RequestHolder.getCurrentUser().getUsername());
		build.setOperateTime(new Date());
		return build;
	}
	
	private void checkName(String name) {
		SysRole sysRole = sysRoleMapper.findByName(name);
		if (sysRole != null) {
			throw new ParamException("存在同名角色");
		}
	}
	
	public void update(RoleParam param) {
		checkName(param.getName());
		SysRole sysRole = buildSysRole(param);
		sysRoleMapper.updateByPrimaryKeySelective(sysRole);
		// sysLogService.saveRoleLog(before, after);
	}
	
	public List<SysRole> getAll() {
		return sysRoleMapper.findAll();
	}
}
