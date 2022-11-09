package com.wx.helper;

import com.wx.domain.beans.JsonData;
import com.wx.exception.ParamException;
import com.wx.exception.PermissionException;
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
 */
@Slf4j
@Component
public class ExceptionHandler extends ExceptionHandlerExceptionResolver {
	
	private static final String JSON = ".json";
	private static final String PAGE = ".page";
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		String url = request.getRequestURL().toString();
		ModelAndView mv;
		String defaultMsg = "System error";
		
		// 这里我们要求项目中所有请求json数据，都使用.json结尾
		if (url.endsWith(JSON)) {
			if (ex instanceof PermissionException || ex instanceof ParamException || ex instanceof IllegalArgumentException) {
				JsonData result = JsonData.fail(ex.getMessage());
				mv = new ModelAndView("jsonView", result.toMap());
			} else {
				log.error("unknown json exception, url: {}",url, ex);
				JsonData result = JsonData.fail(defaultMsg);
				mv = new ModelAndView("jsonView", result.toMap());
			}
			// 这里我们要求项目中所有请求page页面，都使用.page结尾
		} else if (url.endsWith(PAGE)){
			log.error("unknown page exception, url:" + url, ex);
			JsonData result = JsonData.fail(defaultMsg);
			mv = new ModelAndView("exception", result.toMap());
		} else {
			log.error("unknow exception, url:" + url, ex);
			JsonData result = JsonData.fail(defaultMsg);
			mv = new ModelAndView("jsonView", result.toMap());
		}
		
		return mv;
	}
}
