package com.wx.service;

import cn.hutool.core.collection.CollUtil;
import com.wx.dao.SysUserMapper;
import com.wx.domain.beans.PageQuery;
import com.wx.domain.beans.PageResult;
import com.wx.domain.entity.SysUser;
import com.wx.helper.RequestHolder;
import com.wx.domain.param.UserParam;
import com.wx.util.IpUtil;
import com.wx.util.MD5Util;
import com.wx.util.PasswordUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author 22343
 * @version 1.0
 */
@Service
public class SysUserService {
	
	@Resource
	private SysUserMapper sysUserMapper;
	@Resource
	private SysLogService sysLogService;
	
	
	
	public int updateByPrimaryKeySelective(SysUser record) {
		return sysUserMapper.updateByPrimaryKeySelective(record);
	}
	
	
	public int updateByPrimaryKey(SysUser record) {
		return sysUserMapper.updateByPrimaryKey(record);
	}
	
	public SysUser findByKeyword(String username) {
		return sysUserMapper.selectByUsername(username);
	}
	
	public void save(UserParam param) {
		// TODO: 2022/10/26 优化Check逻辑校验
		Assert.isNull(checkPhone(param),"手机号已被占用");
		Assert.isNull(checkEmail(param),"邮箱已被占用");
		String password = PasswordUtil.randomPassword();
		String encryptedPassword = MD5Util.encrypt(password);
		SysUser user = SysUser.builder().username(param.getUsername()).telephone(param.getTelephone()).mail(param.getMail()).password(encryptedPassword).deptId(param.getDeptId()).status(param.getStatus()).remark(param.getRemark()).build();
		// TODO: 2022/10/27 发送邮箱
		user.setOperator(RequestHolder.getCurrentUser().getOperator());
		user.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
		user.setOperateTime(new Date());
		sysUserMapper.insertSelective(user);
		sysLogService.saveUserLog(null, user);
	}
	
	public void update(UserParam param) {
		Assert.isNull(checkPhone(param),"手机号已被占用");
		Assert.isNull(checkEmail(param),"邮箱已被占用");
		SysUser user = SysUser.builder().username(param.getUsername()).telephone(param.getTelephone()).mail(param.getMail()).deptId(param.getDeptId()).status(param.getStatus()).remark(param.getRemark()).build();
		user.setOperator(RequestHolder.getCurrentUser().getOperator());
		user.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
		user.setOperateTime(new Date());
		sysUserMapper.updateByPrimaryKey(user);
		sysLogService.saveUserLog(user, sysUserMapper.selectByPrimaryKey(param.getId()));
	}
	
	public PageResult<SysUser> getPageByDeptId(int deptId,PageQuery pageQuery) {
		List<SysUser> sysUserList = sysUserMapper.selectByPage(deptId,pageQuery);
		return PageResult.<SysUser>builder().data(sysUserList).total(pageQuery.getPageSize()).build();
	}
	
	private SysUser checkEmail(UserParam param) {
		return sysUserMapper.selectByCondition(SysUser.builder().mail(param.getMail()).build());
	}
	
	private SysUser checkPhone(UserParam param) {
		return sysUserMapper.selectByCondition(SysUser.builder().mail(param.getTelephone()).build());
	}
	
	public List<SysUser> findUserList() {
		return findUserList(null);
	}
	
	public int findUserCount(List<Integer> deptList) {
		if (CollUtil.isEmpty(deptList)){
			return 0;
		}
		return findUserList(deptList).size();
	}
	public List<SysUser> findUserList(List<Integer> deptList) {
		if (CollUtil.isEmpty(deptList)){
			return sysUserMapper.findAll();
		}
		return sysUserMapper.findUserListByDeptList(deptList);
	}
	
	public int deleteByIdList(List<Integer> deptList) {
		if (CollUtil.isEmpty(deptList)){
			return 0;
		}
		return sysUserMapper.deleteByIdList(deptList);
	}
}
