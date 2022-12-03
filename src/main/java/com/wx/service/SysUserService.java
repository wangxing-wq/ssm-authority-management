package com.wx.service;

import cn.hutool.core.collection.CollUtil;
import com.wx.dao.SysUserMapper;
import com.wx.domain.beans.PageQuery;
import com.wx.domain.beans.PageResult;
import com.wx.domain.entity.SysUser;
import com.wx.domain.param.UserParam;
import com.wx.helper.RequestHolder;
import com.wx.util.IpUtil;
import com.wx.util.MD5Util;
import com.wx.util.PasswordUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author 22343
 * @version 1.0
 * TODO 优化check校验<br/>
 * TODO 优化邮件发送,最好不要存在常量全通过配置<br/>
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
		Assert.isNull(checkPhone(param),"手机号已被占用");
		Assert.isNull(checkEmail(param),"邮箱已被占用");
		String password = PasswordUtil.randomPassword();
		String encryptedPassword = MD5Util.encrypt(password);
		SysUser user = SysUser.builder().username(param.getUsername()).telephone(param.getTelephone()).mail(param.getMail()).password(encryptedPassword).deptId(param.getDeptId()).status(param.getStatus()).remark(param.getRemark()).build();
		ArrayList<String> tos = CollUtil.newArrayList("wx2234346829@126.com",param.getMail());
		// MailUtil.send(tos,"权限系统分发账号","账号:\t" + param.getUsername() + "\t密码:\t" + password,false);
		user.setOperator(RequestHolder.getCurrentUser().getOperator());
		user.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
		user.setOperateTime(new Date());
		sysUserMapper.insertSelective(user);
		sysLogService.saveUserLog(null,user);
	}
	
	public void update(UserParam param) {
		Assert.isTrue(Objects.equals(checkPhone(param).getId(),param.getId()),"手机号已被占用");
		Assert.isTrue(Objects.equals(checkEmail(param).getId(),param.getId()),"邮箱已被占用");
		SysUser user = SysUser.builder().id(param.getId()).username(param.getUsername()).telephone(param.getTelephone()).mail(param.getMail()).deptId(param.getDeptId()).status(param.getStatus()).remark(param.getRemark()).build();
		user.setOperator(RequestHolder.getCurrentUser().getOperator());
		user.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
		user.setOperateTime(new Date());
		sysUserMapper.updateByPrimaryKeySelective(user);
		sysLogService.saveUserLog(user,sysUserMapper.selectByPrimaryKey(param.getId()));
	}
	
	public PageResult<SysUser> getPageByDeptId(int deptId,PageQuery pageQuery) {
		List<SysUser> sysUserList = sysUserMapper.selectByPage(deptId,pageQuery);
		return PageResult.<SysUser>builder().data(sysUserList).total(pageQuery.getPageSize()).build();
	}
	
	private SysUser checkEmail(UserParam param) {
		return sysUserMapper.selectByCondition(SysUser.builder().mail(param.getMail()).build());
	}
	
	private SysUser checkPhone(UserParam param) {
		return sysUserMapper.selectByCondition(SysUser.builder().telephone(param.getTelephone()).build());
	}
	
	public List<SysUser> findUserList() {
		return findUserList(null);
	}
	
	public int findUserCount(List<Integer> deptList) {
		if (CollUtil.isEmpty(deptList)) {
			return 0;
		}
		return findUserList(deptList).size();
	}
	
	public List<SysUser> findUserList(List<Integer> deptList) {
		if (CollUtil.isEmpty(deptList)) {
			return sysUserMapper.findAll();
		}
		return sysUserMapper.findUserListByDeptList(deptList);
	}
	
	public int deleteByIdList(List<Integer> deptList) {
		if (CollUtil.isEmpty(deptList)) {
			return 0;
		}
		return sysUserMapper.deleteByIdList(deptList);
	}
}
