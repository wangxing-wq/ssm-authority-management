package com.wx.controller;

import com.wx.domain.param.AclModuleParam;
import com.wx.service.SysAclModuleService;
import com.wx.service.SysTreeService;
import com.wx.web.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@Controller
@RequestMapping("/sys/aclModule")
@Slf4j
public class SysAclModuleController {
	
	@Resource
	private SysAclModuleService sysAclModuleService;
	@Resource
	private SysTreeService sysTreeService;
	
	@GetMapping("/acl.page")
	public ModelAndView page() {
		return new ModelAndView("acl");
	}
	
	@PostMapping("/save.json")
	@ResponseBody
	public Result saveAclModule(AclModuleParam param) {
		sysAclModuleService.save(param);
		return Result.success();
	}
	
	@PostMapping("/update.json")
	@ResponseBody
	public Result updateAclModule(AclModuleParam param) {
		sysAclModuleService.update(param);
		return Result.success();
	}
	
	@GetMapping("/tree.json")
	@ResponseBody
	public Result tree() {
		return Result.success(sysTreeService.aclModuleTree());
	}
	
	@PostMapping("/delete.json")
	@ResponseBody
	public Result delete(@RequestParam("id") int id) {
		boolean delete = sysAclModuleService.delete(id);
		if (delete) {
			return Result.fail("删除失败 id: " + id);
		}
		return Result.success();
	}
}
