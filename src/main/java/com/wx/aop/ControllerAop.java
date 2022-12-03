package com.wx.aop;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.ArrayUtil;
import com.wx.util.ValidateUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author 22343
 * @version 1.0
 * 对Controller层进行统一操作
 * <br>TODO: 不知道为什么需要放在Web.xml里面</br>
 * <br>TODO: 添加Dao层的Aop管理,如果没有对应的权限不能进行删除</br>
 */
@Slf4j
@Aspect
@Component
public class ControllerAop {

	private final TimeInterval timer = DateUtil.timer(true);
	
	@Pointcut(value = "within(com.wx.controller.*)")
	public void pointcut() {
	}
	
	@Before("pointcut()")
	public void before(JoinPoint joinPoint){
		// Console.log("{}",joinPoint.getArgs()); 参数
		// Console.log("{}",joinPoint.getKind()); 类型
		// Console.log("{}",joinPoint.getSignature()); 方法签名
		// Console.log("{}",joinPoint.getSourceLocation()); 源代码位置
		// Console.log("{}",joinPoint.getStaticPart()); 表达式
		// Console.log("{}",joinPoint.getTarget()); 替代对象
		// Console.log("{}",joinPoint.getThis()); 当前对象
		// TODO: 2022/11/13 统一参数校验,暂时完成,待测试
		if (ArrayUtil.isNotEmpty(joinPoint.getArgs())) {
			log.debug("统一参数校验:校验源 {}",Arrays.toString(joinPoint.getArgs()));
			ValidateUtil.check(joinPoint.getArgs());
		}
		timer.interval(joinPoint.getSignature().toString());
		log.debug("request mapping method {} invoke ",joinPoint.getSignature());
	}

	@After("pointcut()")
	public void after(JoinPoint joinPoint){
		timer.interval(joinPoint.getSignature().toString());
		log.debug("request mapping method {} invoke end,耗时:\t{}",joinPoint.getSignature(),timer.intervalPretty(joinPoint.getSignature().toString()));
	}
	
}
