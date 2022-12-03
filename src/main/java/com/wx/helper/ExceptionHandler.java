package com.wx.helper;

import com.wx.exception.ParamException;
import com.wx.exception.PermissionException;
import com.wx.web.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		String view = "jsonView";
		Result result;
		String defaultMsg = "System error";
		
		// 这里我们要求项目中所有请求json数据，都使用.json结尾
		if (url.endsWith(JSON)) {
			if (ex instanceof PermissionException || ex instanceof ParamException || ex instanceof IllegalArgumentException) {
				result = Result.fail(ex.getMessage());
			} else {
				log.error("unknown json exception, url: {}",url,ex);
				result = Result.fail(defaultMsg);
			}
		} else if (url.endsWith(PAGE)) {
			log.error("unknown page exception, url:" + url,ex);
			result = Result.fail(defaultMsg);
			view = "exception";
		} else {
			log.error("unknown exception, url:" + url,ex);
			result = Result.fail(defaultMsg);
		}
		return new ModelAndView(view,result);
	}
}
