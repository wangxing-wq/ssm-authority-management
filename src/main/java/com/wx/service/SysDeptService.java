package com.wx.service;

import com.wx.dao.SysDeptMapper;
import com.wx.domain.entity.SysDept;
import com.wx.exception.ParamException;
import com.wx.param.DeptParam;
import com.wx.helper.LevelHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author 22343
 * @version 1.0
 */
@Service
public class SysDeptService {
	
	@Resource
	private SysDeptMapper sysDeptMapper;
	
	
	public boolean deleteByPrimaryKey(Integer id) {
		return sysDeptMapper.deleteByPrimaryKey(id) != 0;
	}
	
	
	public boolean save(DeptParam deptParam) {
		// 逻辑校验
		// 查询是否已存在 1. 名称 2.ParentId 3
		// 计算Level,规则,获取父类ParentId 进行计算
		// 构建Bean
		SysDept sysDept = getSysDept(deptParam);
		return sysDeptMapper.insert(sysDept) != 0;
	}
	
	
	public SysDept selectByPrimaryKey(Integer id) {
		return sysDeptMapper.selectByPrimaryKey(id);
	}
	
	
	public int updateByPrimaryKeySelective(SysDept record) {
		return sysDeptMapper.updateByPrimaryKeySelective(record);
	}
	
	
	public int updateByPrimaryKey(SysDept record) {
		return sysDeptMapper.updateByPrimaryKey(record);
	}
	
	private String getLevel(Integer deptId) {
		SysDept dept = sysDeptMapper.selectByPrimaryKey(deptId);
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
		SysDept oldDep = sysDeptMapper.selectByPrimaryKey(deptParam.getId());
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
	}
	
	private SysDept getSysDept(DeptParam deptParam) {
		SysDept newDept = SysDept.builder().id(deptParam.getId()).name(deptParam.getName()).parentId(deptParam.getParentId()).seq(deptParam.getSeq()).remark(deptParam.getRemark()).build();
		newDept.setLevel(LevelHelper.calculateLevel(getLevel(deptParam.getParentId()),deptParam.getParentId()));
		newDept.setOperateIp("127.0.0.1");
		newDept.setOperator("ROOT");
		newDept.setOperateTime(new Date());
		return newDept;
	}
	
}
