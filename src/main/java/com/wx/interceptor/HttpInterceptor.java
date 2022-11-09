package com.wx.interceptor;

import com.wx.domain.entity.SysUser;
import com.wx.helper.RequestHolder;
import com.wx.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * TODO 需要支持请求上下文,保留登录对象
 * @author 22343
 * @version 1.0
 */
@Slf4j
public class HttpInterceptor implements HandlerInterceptor {
	
	private static final String START_TIME = "start_time";
	
	
	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object handler) {
		String url = request.getRequestURI();
		Map<String,String[]> parameterMap = request.getParameterMap();
		log.info("request start. url:{}, params:{}",url,JsonUtil.obj2String(parameterMap));
		long start = System.currentTimeMillis();
		request.setAttribute(START_TIME,start);
		// TODO: 2022/10/26 替换成常量
		RequestHolder.add((SysUser) request.getSession().getAttribute("user"));
		RequestHolder.add(request);
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request,HttpServletResponse response,Object handler,ModelAndView modelAndView) throws Exception {
		// removeThreadLocalInfo();
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request,HttpServletResponse response,Object handler,Exception ex) {
		String url = request.getRequestURI();
		long start = (Long) request.getAttribute(START_TIME);
		long end = System.currentTimeMillis();
		log.info("request completed. url:{}, cost:{}",url,(end - start) / 1000.0);
		removeThreadLocalInfo();
	}
	
	
	public void removeThreadLocalInfo() {
		RequestHolder.remove();
		;
	}
	
}
