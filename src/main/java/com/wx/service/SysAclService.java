package com.wx.service;

import cn.hutool.core.lang.UUID;
import com.wx.dao.SysAclMapper;
import com.wx.domain.beans.PageQuery;
import com.wx.domain.beans.PageResult;
import com.wx.domain.entity.SysAcl;
import com.wx.domain.param.AclParam;
import com.wx.enums.AclEnum;
import com.wx.helper.RequestHolder;
import com.wx.util.ExceptionHolder;
import com.wx.util.IpUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author 22343
 * @version 1.0
 */
@Service
public class SysAclService {
	
	@Resource
	private SysAclMapper sysAclMapper;
	@Resource
	private SysLogService sysLogService;
	
	@Transactional(rollbackFor=Exception.class)
	public boolean save(AclParam param) {
		ExceptionHolder.instance().isTrue(!checkName(param.getName(),false),AclEnum.BIND_NAME);
		ExceptionHolder.instance().isTrue(!checkUrl(param.getUrl(),false),AclEnum.BIND_URL_EXISTS);
		ExceptionHolder.instance().isTrue(checkAclModule(param.getAclModuleId(),false),
				AclEnum.BIND_ACL_MODULE_NO_EXISTS);
		SysAcl build = getSysAcl(param);
		build.setCode(UUID.fastUUID().toString(true));
		build.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
		build.setOperateTime(new Date());
		build.setOperator(RequestHolder.getCurrentUser().getUsername());
		// sysLogService.saveAclLog(null,build);
		return sysAclMapper.insert(build);
	}
	@Transactional(rollbackFor=Exception.class)
	public boolean update(AclParam param) {
		Assert.isTrue(checkName(param.getName(),false),AclEnum.BIND_NAME.getMessage());
		Assert.isTrue(checkUrl(param.getUrl(),false),AclEnum.BIND_URL_EXISTS.getMessage());
		Assert.isTrue(checkAclModule(param.getAclModuleId(),false),AclEnum.BIND_ACL_MODULE_NO_EXISTS.getMessage());
		// 校验URL是否已经拦截过,模块是否存在,Acl是否存在
		SysAcl oldAcl = sysAclMapper.findById(param.getId());
		SysAcl sysAcl = getSysAcl(param);
		sysLogService.saveAclLog(oldAcl, sysAcl);
		return sysAclMapper.updateById(sysAcl);
	}
	
	public PageResult<SysAcl> getPageByAclModuleId(Integer aclModuleId,PageQuery pageQuery) {
		List<SysAcl> aclList = sysAclMapper.findListByAclModuleId(aclModuleId,pageQuery);
		return PageResult.<SysAcl>builder().data(aclList).total(aclList.size()).build();
	}
	
	private SysAcl getSysAcl(AclParam param) {
		return SysAcl.builder().id(param.getId()).name(param.getName()).aclModuleId(param.getAclModuleId()).url(param.getUrl()).type(param.getType()).status(param.getStatus()).seq(param.getSeq()).remark(param.getRemark()).build();
	}
	
	private boolean checkName(String name,boolean self) {
		SysAcl condition = new SysAcl();
		condition.setName(name);
		return isExists(condition,self);
	}
	
	private boolean checkUrl(String url,boolean self) {
		SysAcl condition = new SysAcl();
		condition.setUrl(url);
		return isExists(condition,self);
	}
	
	private boolean checkAclModule(Integer aclModuleId,boolean self) {
		SysAcl condition = new SysAcl();
		condition.setAclModuleId(aclModuleId);
		return isExists(condition,self);
	}
	
	/**
	 * 根据字段查询是否存在
	 * @param sysAcl 查询条件
	 * @return 是否存在结果
	 */
	private boolean isExists(SysAcl sysAcl,boolean self) {
		if (Objects.isNull(sysAcl)) {
			return false;
		}
		int val = 0;
		List<SysAcl> byBeanSelective = sysAclMapper.findByBeanSelective(sysAcl);
		for (SysAcl acl : byBeanSelective) {
			if (!self && Objects.equals(sysAcl.getId(),acl.getId())) {
				val++;
			}
		}
		return byBeanSelective.size() > val;
	}
	
}
