package com.wx.filter;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import com.wx.domain.entity.SysUser;
import com.wx.helper.RequestHolder;
import com.wx.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * @author 22343
 * @version 1.0
 */
@Slf4j
public class WatchdogFilter implements Filter {
	
	private static final String TIMER = "timer_Interceptor";
	private final TimeInterval timer = DateUtil.timer();
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	
	}
	
	@Override
	public void doFilter(ServletRequest servletRequest,ServletResponse servletResponse,FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse resp = (HttpServletResponse) servletResponse;
		log.info("filter start. url:{}, params:{}",
				request.getRequestURI(),
				JsonUtil.obj2String(Optional.of(request.getParameterMap()).orElse(MapUtil.newHashMap())));
		// TODO: 2022/10/26 登录用户不应该在这一层做
		RequestHolder.add((SysUser) request.getSession().getAttribute("user"));
		RequestHolder.add(request);
		// 打印请求参数,打印开始时间
		String uuid = IdUtil.fastSimpleUUID();
		timer.interval();
		doFilter(servletRequest,servletResponse,filterChain);
		log.info("filter url {} invoke end",request.getRequestURI());
		log.info("耗时:\t{}",timer.intervalPretty(uuid));
	}
	
	@Override
	public void destroy() {
	
	}
}
