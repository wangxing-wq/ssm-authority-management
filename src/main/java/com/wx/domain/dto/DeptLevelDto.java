package com.wx.domain.dto;

import com.google.common.collect.Lists;
import com.wx.domain.entity.SysDept;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @author 22343
 * @version 1.0
 */
@Getter
@Setter
@ToString
public class DeptLevelDto extends SysDept {
	
	private List<DeptLevelDto> deptList = Lists.newArrayList();
	
	
	public static DeptLevelDto adapt(SysDept dept){
		DeptLevelDto deptLevelDto = new DeptLevelDto();
		BeanUtils.copyProperties(dept,deptLevelDto);
		return deptLevelDto;
	}
	
}
