package com.wx.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wx.domain.vo.TestVo;
import com.wx.exception.ParamException;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.*;

/**
 * @author 22343
 * @version 1.0
 */
public class ValidateUtil {
	
	/**
	 * Validator
	 */
	private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
	
	
	public static LinkedHashMap<String,String> validateObject(Object bean,Class<?>... groups){
		List<LinkedHashMap<String,String>> validator = validator(Lists.newArrayList(bean),groups);
		return validator.size() == 0 ? null : validator.get(0);
	}
	public static List<LinkedHashMap<String,String>>  validateList(List<Object> beans,Class<?>... groups){
		return validator(beans,groups);
	}
	
	private static List<LinkedHashMap<String,String>> validator(List<Object> beans,Class<?>... groups) {
		LinkedList<LinkedHashMap<String,String>> objects = Lists.newLinkedList();
		if (beans == null) {
			return objects;
		}
		for (Object bean : beans) {
			Set<ConstraintViolation<Object>> validateResult = VALIDATOR.validate(bean,groups);
			if (CollectionUtils.isEmpty(validateResult)){
				continue;
			}
			LinkedHashMap<String,String> errorMsg = Maps.newLinkedHashMap();
			for (ConstraintViolation<Object> objectConstraintViolation : validateResult) {
				Path propertyPath = objectConstraintViolation.getPropertyPath();
				errorMsg.put(propertyPath.toString(),objectConstraintViolation.getMessage());
			}
			objects.add(errorMsg);
		}
		return objects;
	}
	
	
	public static void check(Object param) throws ParamException {
		Map<String, String> map = ValidateUtil.validateObject(param);
		if (!CollectionUtils.isEmpty(map)) {
			throw new ParamException(map.toString());
		}
	}
	

}
