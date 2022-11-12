package com.wx.interceptor;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import com.wx.domain.entity.SysUser;
import com.wx.helper.RequestHolder;
import com.wx.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * TODO 需要支持请求上下文,保留登录对象
 * @author 22343
 * @version 1.0
 */
@Slf4j
public class HttpInterceptor implements HandlerInterceptor {
	
	private static final String TIMER = "timer_Interceptor";
	private final TimeInterval timer = DateUtil.timer();
	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object handler) {
		log.info("request start. url:{}, params:{}",
				request.getRequestURI(),
				JsonUtil.obj2String(Optional.of(request.getParameterMap()).orElse(MapUtil.newHashMap())));
		// TODO: 2022/10/26 登录用户不应该在这一层做
		RequestHolder.add((SysUser) request.getSession().getAttribute("user"));
		RequestHolder.add(request);
		// 打印请求参数,打印开始时间
		String uuid = IdUtil.fastSimpleUUID();
		request.setAttribute(TIMER,uuid);
		timer.interval(uuid);
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request,HttpServletResponse response,Object handler,ModelAndView modelAndView) {
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request,HttpServletResponse response,Object handler,Exception ex) {
		if (ex != null){
			log.error("request url {},invoke fail,error",request.getRequestURI(),ex);
		}
		log.info("request url {} invoke end",request.getRequestURI());
		removeThreadLocalInfo();
		log.info("耗时:\t{}",timer.intervalPretty(request.getAttribute(TIMER).toString()));
	}
	
	
	public void removeThreadLocalInfo() {
		if (log.isDebugEnabled()){
			log.info("RequestHolder invoke remove");
		}
		RequestHolder.remove();
	}
	
}
