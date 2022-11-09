package com.wx.service;

import com.wx.dao.SysAclMapper;
import com.wx.domain.entity.SysAcl;
import com.wx.domain.entity.SysAclModule;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 22343
 * @version 1.0
 */
@Service
public class SysCoreService {
	
	@Resource
	private SysAclMapper sysAclMapper;
	
	public List<SysAcl> getUserAclList(int userId) {
		return sysAclMapper.findListByUserId(userId);
	}
}
