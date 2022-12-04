package com.wx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 控制台页面跳转
 * TODO 可以对静态页面跳转进行优化
 * @author 22343
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@GetMapping("index.page")
	public ModelAndView index() {
		return new ModelAndView("admin");
	}
}
