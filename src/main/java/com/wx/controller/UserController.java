package com.wx.controller;

import cn.hutool.core.util.StrUtil;
import com.wx.domain.entity.SysUser;
import com.wx.service.SysUserService;
import com.wx.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 22343
 */
@Slf4j
@Controller
public class UserController {
	
	@Resource
	private SysUserService sysUserService;
	
	@GetMapping("/logout.page")
	public void logout(HttpServletRequest request,HttpServletResponse response) throws IOException {
		request.getSession().invalidate();
		String path = "signin.jsp";
		response.sendRedirect(path);
	}
	
	@PostMapping("/login.page")
	public void login(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
		// TODO: 2022/10/26 优化成Vo对象
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		SysUser sysUser = sysUserService.findByKeyword(username);
		String errorMsg = "";
		String ret = request.getParameter("ret");
		// TODO: 2022/10/26 等待优化IF ELSE
		if (ObjectUtils.isEmpty(username)) {
			errorMsg = "用户名不可以为空";
		} else if (ObjectUtils.isEmpty(password)) {
			errorMsg = "密码不可以为空";
		} else if (sysUser == null) {
			errorMsg = "查询不到指定的用户";
		} else if (!sysUser.getPassword().equals(MD5Util.encrypt(password))) {
			errorMsg = "用户名或密码错误";
		} else if (sysUser.getStatus() != 1) {
			errorMsg = "用户已被冻结，请联系管理员";
		} else {
			// login success
			request.getSession().setAttribute("user",sysUser);
			if (StringUtils.hasLength(ret)) {
				response.sendRedirect(ret);
			} else {
				response.sendRedirect("/admin/index.page");
			}
		}
		request.setAttribute("username",username);
		request.setAttribute("ret",ret);
		if(StrUtil.isNotBlank(errorMsg)){
			request.setAttribute("error",errorMsg);
			String path = "/signin.jsp";
			response.setHeader("method", "GET");
			request.getRequestDispatcher(path).forward(request,response);
		}
		
	}
}
