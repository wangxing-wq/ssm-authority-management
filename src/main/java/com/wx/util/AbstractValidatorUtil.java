package com.wx.util;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import java.util.Set;

/**
 * @author 22343
 */
public abstract class AbstractValidatorUtil {

    public static ValidatorFactory obtainValidatorFactory() {
        return Validation.buildDefaultValidatorFactory();
    }
    
    public static Validator obtainValidator() {
        return obtainValidatorFactory().getValidator();
    }

    public static ExecutableValidator obtainExecutableValidator() {
        return obtainValidator().forExecutables();
    }
    
    public static <T> void printViolations(Set<ConstraintViolation<T>> violations) {
        violations.stream().map(v -> v.getPropertyPath() + " " + v.getMessage() + ": " + v.getInvalidValue()).forEach(System.out::println);
    }
    
   
    
    
    
}
