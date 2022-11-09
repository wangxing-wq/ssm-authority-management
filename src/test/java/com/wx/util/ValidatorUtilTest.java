package com.wx.util;

import junit.framework.TestCase;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.ScriptAssert;
import org.hibernate.validator.internal.engine.DefaultClockProvider;
import org.hibernate.validator.internal.engine.DefaultParameterNameProvider;
import org.hibernate.validator.internal.engine.ValidatorContextImpl;
import org.hibernate.validator.internal.engine.ValidatorFactoryImpl;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorContext;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotBlank;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.MethodType;
import javax.validation.metadata.PropertyDescriptor;
import java.util.Set;

/**
 * @author 22343
 * @version 1.0
 */
public class ValidatorUtilTest extends TestCase {
	
	
	@Data
	@ScriptAssert(script = "_this.name==_this.fullName", lang = "javascript")
	public class User {
		
		@NotBlank(message = "name can not null")
		private String name;
		
		@DecimalMax(value = "100",message = "age can more than 100")
		private Integer age;
		
		@Length(min=20 ,message = "fullName length less then 20")
		@NotBlank(message = "full name can not null")
		private String fullName;
	}
	
	@Test
	public void test1(){
		User user = new User();
		user.setName("Hello");
		user.setAge(200);
		user.setFullName("123456");
		
		Set<ConstraintViolation<User>> validate = AbstractValidatorUtil.obtainValidator().validate(user);
		AbstractValidatorUtil.printViolations(validate);
	}
	@Test
	public void test2(){
		User user = new User();
		user.setName("Hello");
		user.setAge(200);
		user.setFullName("123456");
		// 指定校验fullName
		Set<ConstraintViolation<User>> validate = AbstractValidatorUtil.obtainValidator().validateProperty(user,"fullName");
		AbstractValidatorUtil.printViolations(validate);
		
		// 指定校验age
		Set<ConstraintViolation<User>> validateAge = AbstractValidatorUtil.obtainValidator().validateProperty(user,"age");
		AbstractValidatorUtil.printViolations(validateAge);
	}
	@Test
	public void test3(){
		Set<ConstraintViolation<User>> fullName = AbstractValidatorUtil.obtainValidator().validateValue(User.class, "fullName", "123");
		AbstractValidatorUtil.printViolations(fullName);
	}
	@Test
	public void test4(){
		BeanDescriptor beanDescriptor = AbstractValidatorUtil.obtainValidator().getConstraintsForClass(User.class);
		System.out.println("此类是否需要校验："+beanDescriptor.isBeanConstrained());
		
		System.out.println("需要校验的属性："+beanDescriptor.getConstrainedProperties());
		System.out.println("需要校验的方法："+beanDescriptor.getConstrainedMethods(MethodType.GETTER));
		System.out.println("需要校验的构造器："+beanDescriptor.getConstrainedConstructors());
		
		PropertyDescriptor fullName = beanDescriptor.getConstraintsForProperty("fullName");
		System.out.println("fullName属性的约束注解个数 " + fullName.getConstraintDescriptors().size());
	}
	@Test
	public void test5() {
		User user = new User();
		user.setName("Hello");
		user.setAge(200);
		user.setFullName("123456");
		ValidatorFactoryImpl validatorFactory = (ValidatorFactoryImpl) AbstractValidatorUtil.obtainValidatorFactory();
		ValidatorContext validatorContext = new ValidatorContextImpl(validatorFactory)
				.parameterNameProvider(new DefaultParameterNameProvider())
				.clockProvider(DefaultClockProvider.INSTANCE);
		Set<ConstraintViolation<User>> validate = validatorContext.getValidator().validate(user);
		for (ConstraintViolation<User> userConstraintViolation : validate) {
			System.out.println(userConstraintViolation.getMessage() +"=====" + userConstraintViolation.getMessageTemplate());
		}
	}
	
	@Test
	public void test33() {
		Validator validator = AbstractValidatorUtil.obtainValidatorFactory().usingContext()
				.parameterNameProvider(new DefaultParameterNameProvider())
				.clockProvider(DefaultClockProvider.INSTANCE)
				.getValidator();
	}
	
	
}
