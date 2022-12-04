package com.wx.helper;

import com.wx.exception.BizException;
import com.wx.exception.HandlerException;
import com.wx.exception.NotifyException;
import com.wx.util.ExceptionHolder;
import com.wx.util.JsonUtil;
import com.wx.web.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author 22343
 * @version 1.0
 * TODO 我觉得此处可以使用策略模式,同时配合数据加载.同时可以优化wx-common包
 * TODO springmvc HandlerExceptionResolver 异常处理器,可以查看是否和SpringBoot 全体异常处理逻辑一样
 */
@Slf4j
@Component
public class ExceptionHandler extends ExceptionHandlerExceptionResolver {
	
	private static final String JSON = ".json";
	private static final String PAGE = ".page";
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request,HttpServletResponse response,Object handler,Exception ex) {
		String url = request.getRequestURL().toString();
		String defaultMsg = "System error";
		// 后边过滤增加异常处理类型
		if (url.endsWith(JSON)) {
			return endJson(ex);
		} else if (url.endsWith(PAGE)) {
			return endPage(ex,url);
		} else {
			log.error("unknown exception, url:" + url,ex);
			return new ModelAndView("jsonView",Result.fail(defaultMsg));
		}
		
	}
	
	public ModelAndView endJson(Exception ex) {
		Result result = Result.fail();
		if (ex instanceof HandlerException) {
			log.error("异常信息:",ex);
			result = Result.fail((HandlerException) ex);
		}
		List<NotifyException> notifyExceptionList = ExceptionHolder.instance().getNotifyExceptionList();
		if (!notifyExceptionList.isEmpty()) {
			log.debug("可不处理异常:{}",JsonUtil.obj2String(notifyExceptionList));
			result = Result.fail(notifyExceptionList.get(0));
		}
		if (!(ex instanceof BizException)) {
			log.info("非业务异常:",ex);
		}
		return new ModelAndView("jsonView",result);
	}
	
	public ModelAndView endPage(Exception ex,String url) {
		log.error("unknown page exception, url:" + url,ex);
		return new ModelAndView("exception",Result.fail("404"));
	}
	
}
