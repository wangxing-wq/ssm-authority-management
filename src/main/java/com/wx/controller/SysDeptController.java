package com.wx.controller;

import com.wx.domain.dto.DeptLevelDto;
import com.wx.domain.param.DeptParam;
import com.wx.service.SysDeptService;
import com.wx.service.SysTreeService;
import com.wx.web.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * TODO 都要进行操作权限的校验,可以考虑实用Aop进行权限拦截,同时对Dao进行命名规范
 * TODO 对逻辑权限校验进行补充
 * TODO 优化树形结构
 * TODO 都要进行操作权限的校验,可以考虑实用Aop进行权限拦截,同时对Dao进行命名规范
 * C(插入时候的逻辑校验,插入时候的权限校验)
 * U(插入时候的逻辑校验,更新时候的权限校验)
 * R(优化树形结构,查询的权限校验)
 * D(删除时候的权限校验)
 * @author 22343
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/sys/dept")
public class SysDeptController {
	
	final SysDeptService sysDeptService;
	
	final SysTreeService sysTreeService;
	
	@GetMapping("/dept.page")
	public ModelAndView page() {
		return new ModelAndView("dept");
	}
	
	public SysDeptController(SysDeptService sysDeptService,SysTreeService sysTreeService) {
		this.sysDeptService = sysDeptService;
		this.sysTreeService = sysTreeService;
	}
	@PostMapping("/save.json")
	public Result save(DeptParam deptParam){
		if (!sysDeptService.save(deptParam)){
			return Result.fail("插入失败DeptParam");
		}
		return Result.success();
	}
	
	@GetMapping("/tree.json")
	public Result tree() {
		List<DeptLevelDto> dtoList = sysTreeService.deptTree();
		return Result.success(dtoList);
	}
	
	@PostMapping("/update.json")
	public Result updateDept(DeptParam param) {
		sysDeptService.update(param);
		return Result.success();
	}
	@PostMapping("/delete.json")
	@ResponseBody
	public Result delete(@RequestParam("id") int id) {
		boolean result = sysDeptService.deleteMenuList(id);
		if (result){
			return Result.success();
		}
		return Result.fail();
	}
	
	
}
