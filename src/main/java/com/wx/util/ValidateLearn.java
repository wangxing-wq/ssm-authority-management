package com.wx.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * @author 22343
 * @version 1.0
 * ValidateLearn学习
 */
@Slf4j
public class ValidateLearn {
	
	// static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	
	public static void main(String[] args) {
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		ValidatePerson person = new ValidatePerson("王兴","男",18);
		Set<ConstraintViolation<ValidatePerson>> validate = validator.validate(person);
		for (ConstraintViolation<ValidatePerson> validatePersonConstraintViolation : validate) {
			// 获取消息  ****
			System.out.printf("1 %s\n", validatePersonConstraintViolation.getMessage());
			// 获取错误消息模板
			System.out.printf("2 %s\n", validatePersonConstraintViolation.getMessageTemplate());
			// 获取执行的Bean
			System.out.printf("3 %s\n", validatePersonConstraintViolation.getRootBean());
			// 获取执行的Bean Class
			System.out.printf("4 %s\n", validatePersonConstraintViolation.getRootBeanClass());
			// 返回约束对象
			System.out.printf("5 %s\n", validatePersonConstraintViolation.getLeafBean());
			// 获取可执行参数
			System.out.printf("6 %s\n", validatePersonConstraintViolation.getExecutableParameters());
			// 获取可执行返回值
			System.out.printf("7 %n", validatePersonConstraintViolation.getExecutableReturnValue());
			// 获取执行执行路径 ****
			System.out.printf("8 %s\n", validatePersonConstraintViolation.getPropertyPath());
			// 获取无效值 ****
			System.out.printf("9 %s\n", validatePersonConstraintViolation.getInvalidValue());
			// 获取约束信息
			System.out.printf("10 %s\n", validatePersonConstraintViolation.getConstraintDescriptor());
			// System.out.printf("%s\n", validatePersonConstraintViolation.unwrap());
		}
		// 2月基础 1个月
		// 框架 ssm + springboot 1 ~ 2 月
		// MySQL + redis + linux 1月
		// 微服务 ~
		// 6个月项目 ssm + springboot
		// 准备面试题
	}
	
	
	
	
	@Data @NoArgsConstructor @AllArgsConstructor
	static class ValidatePerson{
		
		@Size(message = "长度要大于1",min = 4,max = 5)
		String name;
		// @NotBlank(message = "{Message}")
		String sex;
		Integer age;
	
	}
	
	@ScriptAssert(script = "_this.name==_this.fullName", lang = "javascript")
	@Data
	static class User {
		
		@NotNull
		private String name;
		
		@Length(min = 20)
		@NotNull
		private String fullName;
	}
	
}
