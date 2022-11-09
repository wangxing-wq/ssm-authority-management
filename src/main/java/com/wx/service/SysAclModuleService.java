package com.wx.service;

import com.wx.dao.SysAclModuleMapper;
import com.wx.domain.entity.SysAclModule;
import com.wx.exception.ParamException;
import com.wx.helper.LevelHelper;
import com.wx.helper.RequestHolder;
import com.wx.param.AclModuleParam;
import com.wx.util.IpUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author 22343
 * @version 1.0
 */
@Service
public class SysAclModuleService {
	
	@Resource
	private SysAclModuleMapper sysAclModuleMapper;
	
	public SysAclModule selectByPrimaryKey(Integer id) {
		return sysAclModuleMapper.selectByPrimaryKey(id);
	}
	
	
	public int updateByPrimaryKey(SysAclModule record) {
		return sysAclModuleMapper.updateByPrimaryKey(record);
	}
	
	public void save(AclModuleParam param) {
		checkExists(param);
		checkNameExists(param);
		SysAclModule build = getAclModule(param);
		build.setLevel(LevelHelper.calculateLevel(sysAclModuleMapper.selectByPrimaryKey(param.getParentId()).getLevel(),param.getParentId()));
		sysAclModuleMapper.insertSelective(build);
	}
	
	@Transactional(rollbackFor=Exception.class)
	public void update(AclModuleParam param) {
		SysAclModule oldAclModule = sysAclModuleMapper.selectByPrimaryKey(param.getId());
		if (Objects.isNull(oldAclModule)) {
			throw new ParamException("AclModuleParam id is exits");
		}
		checkExists(param);
		checkNameExists(param);
		SysAclModule aclModule = getAclModule(param);
		aclModule.setLevel(oldAclModule.getLevel().replaceFirst(".$",param.getParentId().toString()));
		aclModule.setId(param.getId());
		sysAclModuleMapper.updateByPrimaryKeySelective(aclModule);
		// 查询所有子树
		List<SysAclModule> sysAclModuleList = sysAclModuleMapper.selectChildDeptListByLevel(oldAclModule.getLevel());
		sysAclModuleList.forEach(item -> {
			// 0.6.9 0.6.9.10
			String level = oldAclModule.getLevel();
			item.setLevel(item.getLevel().replace(level,aclModule.getLevel()));
			sysAclModuleMapper.updateByPrimaryKey(item);
		});
	}
	
	private void checkNameExists(AclModuleParam param) {
		boolean flag = sysAclModuleMapper.selectByNameNotId(param.getName(),param.getId());
		if (flag) {
			throw new ParamException("AclModuleParam 命名重复");
		}
	}
	
	private void checkExists(AclModuleParam param) {
		SysAclModule parentId = sysAclModuleMapper.selectByPrimaryKey(param.getParentId());
		if (Objects.isNull(parentId)) {
			throw new ParamException("AclModuleParam 上级部门不存在");
		}
	}
	
	private SysAclModule getAclModule(AclModuleParam param) {
		SysAclModule build = SysAclModule.builder().parentId(param.getParentId()).name(param.getName()).remark(param.getRemark()).seq(param.getSeq()).status(param.getStatus()).build();
		build.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
		build.setOperateTime(new Date());
		build.setOperator(RequestHolder.getCurrentUser().getUsername());
		return build;
	}
	
	public boolean delete(int id) {
		return sysAclModuleMapper.deleteByPrimaryKey(id) != 0;
	}
	
}
