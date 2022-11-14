package com.wx.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.wx.dao.SysAclMapper;
import com.wx.dao.SysAclModuleMapper;
import com.wx.dao.SysDeptMapper;
import com.wx.dao.SysRoleAclMapper;
import com.wx.domain.dto.AclDto;
import com.wx.domain.dto.AclModuleLevelDto;
import com.wx.domain.dto.DeptLevelDto;
import com.wx.domain.entity.SysAcl;
import com.wx.domain.entity.SysAclModule;
import com.wx.domain.entity.SysDept;
import com.wx.helper.LevelHelper;
import com.wx.helper.RequestHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * @author 22343
 * @version 1.0
 */
@Service
public class SysTreeService {
	
	@Resource
	private SysDeptMapper sysDeptMapper;
	@Resource
	private SysAclMapper sysAclMapper;
	@Resource
	private SysRoleAclMapper sysRoleAclMapper;
	@Resource
	private SysAclModuleMapper sysAclModuleMapper;
	@Resource
	private SysCoreService sysCoreService;
	
	public List<DeptLevelDto> deptTree() {
		List<SysDept> sysDepts = sysDeptMapper.findAll();
		if (CollectionUtils.isEmpty(sysDepts)) {
			return null;
		}
		List<DeptLevelDto> collect = sysDepts.stream().map(DeptLevelDto::adapt).collect(toList());
		return deptListToTree(collect);
	}
	
	public List<DeptLevelDto> deptListToTree(List<DeptLevelDto> collect) {
		Map<Integer,DeptLevelDto> dtoMap = collect.stream().collect(toMap(DeptLevelDto::getId,(deptLevelDto -> deptLevelDto)));
		return collect.stream().peek(item -> {
			DeptLevelDto deptLevelDto = dtoMap.get(item.getParentId());
			if (deptLevelDto != null) {
				deptLevelDto.getDeptList().add(item);
			}
		}).filter(item -> Objects.equals(item.getLevel(),LevelHelper.ROOT)).collect(toList());
	}
	
	public List<AclModuleLevelDto> userAclTree(int userId) {
		List<SysAcl> userAclList = sysCoreService.getUserAclList(userId);
		List<AclDto> aclDtoList = Lists.newArrayList();
		for (SysAcl acl : userAclList) {
			AclDto dto = AclDto.adapt(acl);
			dto.setHasAcl(true);
			dto.setChecked(true);
			aclDtoList.add(dto);
		}
		return aclListToTree(aclDtoList);
	}
	
	/**
	 * 获取权限点
	 * @return 部门权限树
	 */
	public List<AclModuleLevelDto> aclModuleTree() {
		List<SysAclModule> sysAclModuleList = sysAclModuleMapper.findAll();
		if (CollectionUtils.isEmpty(sysAclModuleList)) {
			return null;
		}
		List<AclModuleLevelDto> collect = sysAclModuleList.stream().map(AclModuleLevelDto::adapt).collect(toList());
		return aclModuleListToTree(collect);
	}
	
	public List<AclModuleLevelDto> aclModuleListToTree(List<AclModuleLevelDto> collect) {
		Map<Integer,AclModuleLevelDto> dtoMap = collect.stream().collect(toMap(AclModuleLevelDto::getId,(deptLevelDto -> deptLevelDto)));
		return collect.stream().peek(item -> {
			AclModuleLevelDto aclModuleLevelDto = dtoMap.get(item.getParentId());
			if (aclModuleLevelDto != null) {
				aclModuleLevelDto.getAclModuleList().add(item);
			}
		}).filter(item -> Objects.equals(item.getLevel(),LevelHelper.ROOT)).collect(toList());
	}
	
	public List<AclModuleLevelDto> roleTree(int roleId) {
		// 查询角色所有权限
		List<Integer> roleAclList = sysRoleAclMapper.findAclListByRoleId(roleId);
		// 查询用户所有权限
		List<Integer> userAclList = sysRoleAclMapper.findAclListByUserId(RequestHolder.getCurrentUser().getId());
		if (RequestHolder.getCurrentUser().getId() == 1){
			userAclList.addAll(sysAclMapper.findAll().stream().map(SysAcl::getId).collect(toList()));
		}
		List<SysAcl> sysAclList = sysAclMapper.findAll();
		List<AclDto> aclDtoList = sysAclList.stream().map(item -> {
			AclDto dto = AclDto.adapt(item);
			dto.setChecked(roleAclList.contains(item.getId()));
			dto.setHasAcl(userAclList.contains(item.getId()));
			return dto;
		}).collect(toList());
		// 合并成树
		return aclListToTree(aclDtoList);
	}
	
	/**
	 * 权限点列表转树
	 * @param aclDtoList 权限点列表
	 * @return 部门树
	 */
	public List<AclModuleLevelDto> aclListToTree(List<AclDto> aclDtoList) {
		if (CollectionUtils.isEmpty(aclDtoList)) {
			return Lists.newArrayList();
		}
		List<AclModuleLevelDto> aclModuleLevelList = aclModuleTree();
		Multimap<Integer, AclDto> moduleIdAclMap = ArrayListMultimap.create();
		for(AclDto acl : aclDtoList) {
			if (acl.getStatus() == 1) {
				moduleIdAclMap.put(acl.getAclModuleId(), acl);
			}
		}
		bindAclListWithOrder(aclModuleLevelList, moduleIdAclMap);
		return aclModuleLevelList;
	}
	
	/**
	 * 给模块绑定权限树
	 * @param aclModuleLevelList 部门 树形列表,含权限点
	 * @param moduleIdAclMap 权限Map
	 */
	public void bindAclListWithOrder(List<AclModuleLevelDto> aclModuleLevelList,
			Multimap<Integer, AclDto> moduleIdAclMap) {
		if (CollectionUtils.isEmpty(aclModuleLevelList)) {
			return;
		}
		for (AclModuleLevelDto dto : aclModuleLevelList) {
			List<AclDto> aclDtoList = (List<AclDto>)moduleIdAclMap.get(dto.getId());
			if (!CollectionUtils.isEmpty(aclDtoList)) {
				dto.setAclList(aclDtoList);
				aclDtoList.sort(Comparator.comparingInt(SysAcl::getSeq));
			}
			bindAclListWithOrder(dto.getAclModuleList(), moduleIdAclMap);
		}
	}
	
}
