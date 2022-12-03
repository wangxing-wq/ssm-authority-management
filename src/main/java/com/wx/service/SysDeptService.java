package com.wx.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.wx.dao.SysDeptMapper;
import com.wx.domain.entity.SysDept;
import com.wx.domain.entity.SysUser;
import com.wx.domain.param.DeptParam;
import com.wx.exception.BizException;
import com.wx.exception.ParamException;
import com.wx.helper.LevelHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 22343
 * @version 1.0
 */
@Service
public class SysDeptService {
	
	@Resource
	private SysDeptMapper sysDeptMapper;
	@Resource
	private SysLogService sysLogService;
	@Resource
	private SysUserService sysUserService;
	
	
	public boolean save(DeptParam deptParam) {
		// 逻辑校验
		// 查询是否已存在 1. 名称 2.ParentId 3
		// 计算Level,规则,获取父类ParentId 进行计算
		// 构建Bean
		SysDept sysDept = getSysDept(deptParam);
		boolean result = sysDeptMapper.insert(sysDept) != 0;
		sysLogService.saveDeptLog(null, sysDept);
		return result;
	}
	
	private String getLevel(Integer deptId) {
		SysDept dept = sysDeptMapper.findById(deptId);
		if (dept == null) {
			return null;
		}
		return dept.getLevel();
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void update(DeptParam deptParam) {
		// 逻辑业务校验
		// 1.查询是否存在
		// 2.查询重命名是否存在问题
		// TODO 对异常的完善,对层级功能的重新设计,增加批修
		SysDept oldDep = sysDeptMapper.findById(deptParam.getId());
		if (oldDep == null){
			throw new ParamException("dept id Not exits");
		}
		// TODO: 2022/10/26 如何优雅处理重命名,有问题需要思考
		SysDept continuation = SysDept.builder().name(deptParam.getName()).build();
		List<Integer> continuationResult = sysDeptMapper.selectBySelective(continuation);
		if (!CollectionUtils.isEmpty(continuationResult) && !continuationResult.contains(deptParam.getId())){
			throw new ParamException("不能命名重复菜单");
		}
		// 构建新的DeptParam
		SysDept newDept = getSysDept(deptParam);
		sysDeptMapper.updateByPrimaryKeySelective(newDept);
		List<SysDept> sysDeptList = sysDeptMapper.selectChildDeptListByLevel(oldDep.getLevel());
		// 批量修改层级
		for (SysDept sysDept : sysDeptList) {
			String level = sysDept.getLevel();
			if (level.indexOf(oldDep.getLevel()) == 0) {
				level = newDept.getLevel() + level.substring(oldDep.getLevel().length());
				sysDept.setLevel(level);
			}
			sysDeptMapper.updateByPrimaryKey(sysDept);
		}
		sysLogService.saveDeptLog(oldDep, newDept);
	}
	
	private SysDept getSysDept(DeptParam deptParam) {
		SysDept newDept = SysDept.builder().id(deptParam.getId()).name(deptParam.getName()).parentId(deptParam.getParentId()).seq(deptParam.getSeq()).remark(deptParam.getRemark()).build();
		newDept.setLevel(LevelHelper.calculateLevel(getLevel(deptParam.getParentId()),deptParam.getParentId()));
		newDept.setOperateIp("127.0.0.1");
		newDept.setOperator("ROOT");
		newDept.setOperateTime(new Date());
		return newDept;
	}
	
	public List<Integer> findChildrenById(int id) {
		SysDept byId = sysDeptMapper.findById(id);
		if (ObjectUtil.isEmpty(byId)){
			return ListUtil.empty();
		}
		List<Integer> childrenByLevel =
				sysDeptMapper.findChildrenByLevel(byId.getLevel().concat(".").concat(byId.getId().toString()));
		childrenByLevel.add(byId.getId());
		return childrenByLevel;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteMenuList(int id) {
		// 查看是该部门及下面部门
		List<Integer> deptList = findChildrenById(id);
		// 有多个子部门是否删除
		// Assert.isTrue(CollUtil.isEmpty(deptList),BizException::new);
		// 有存在用户是否删除
		List<SysUser> sysUserList = sysUserService.findUserList(deptList);
		// Assert.isTrue(CollUtil.isEmpty(sysUserList),BizException::new);
		List<Integer> sysUserIdList = sysUserList.stream().map(SysUser::getId).collect(Collectors.toList());
		// 删除存在用户和部门
		int deptDeleteCount = deleteByIdList(deptList);
		int userDeleteCount = sysUserService.deleteByIdList(sysUserIdList);
		Assert.isTrue(sysUserList.size() == 0,BizException::new);
		Assert.isTrue(deptDeleteCount == deptList.size(),BizException::new);
		Assert.isTrue(userDeleteCount == sysUserIdList.size(),BizException::new);
		return true;
	}
	
	private int deleteByIdList(List<Integer> sysUserIdList) {
		if (CollUtil.isEmpty(sysUserIdList)){
			return 0;
		}
		return sysDeptMapper.deleteByIdList(sysUserIdList);
	}
	
}
