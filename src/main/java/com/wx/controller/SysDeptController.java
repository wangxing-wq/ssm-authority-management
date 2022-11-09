package com.wx.controller;

import com.wx.domain.dto.DeptLevelDto;
import com.wx.param.DeptParam;
import com.wx.service.SysDeptService;
import com.wx.service.SysTreeService;
import com.wx.domain.beans.JsonData;
import com.wx.util.ValidateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * TODO 都要进行操作权限的校验,可以考虑实用Aop进行权限拦截,同时对Dao进行命名规范
 * @author 22343
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/sys/dept")
public class SysDeptController {
	
	final SysDeptService sysDeptService;
	
	@Autowired
	SysTreeService sysTreeService;
	
	@GetMapping("/dept.page")
	public ModelAndView page() {
		return new ModelAndView("dept");
	}
	
	public SysDeptController(SysDeptService sysDeptService) {
		this.sysDeptService = sysDeptService;
	}
	@PostMapping("/save.json")
	public JsonData save(DeptParam deptParam){
		ValidateUtil.check(deptParam);
		if (!sysDeptService.save(deptParam)){
			// TODO: 2022/10/24 处理的不是特别完美,状态码可以替换,Validate可以使用切面
			return JsonData.fail("插入失败DeptParam");
		}
		return JsonData.success();
	}
	
	@GetMapping("/tree.json")
	public JsonData tree() {
		// TODO: 2022/10/25 可以优化树的排序
		List<DeptLevelDto> dtoList = sysTreeService.deptTree();
		return JsonData.success(dtoList);
	}
	
	@PostMapping("/update.json")
	public JsonData updateDept(DeptParam param) {
		ValidateUtil.check(param);
		// TODO: 2022/10/25 优化修改方式,Level字段可以取消
		sysDeptService.update(param);
		return JsonData.success();
	}
	@PostMapping("/delete.json")
	@ResponseBody
	public JsonData delete(@RequestParam("id") int id) {
		// TODO: 2022/10/26 应该安全校验是否有权限  我也是
		if (!sysDeptService.deleteByPrimaryKey(id)) {
			return JsonData.fail("删除Dept 失败");
		}
		return JsonData.success();
	}
	
	
}
