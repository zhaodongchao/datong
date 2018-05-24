package com.dongchao.datong.batch;

import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;
import org.springframework.beans.factory.InitializingBean;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.Set;

/**
 * 自定义领域模型校验器
 */
public class CsvBeanValidator<T> implements Validator<T>,InitializingBean {
    private javax.validation.Validator validator ;
    @Override
    public void validate(T value) throws ValidationException {
        //此处使用validator校验数据
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(value);
        if (constraintViolations.size()>0){
            StringBuilder message = new StringBuilder();
            //拼接校验的错误信息,手动抛出异常
            for (ConstraintViolation constraintViolation : constraintViolations){
                message.append(constraintViolation.getMessage());
            }
            throw new ValidationException(message.toString());
        }
    }

    /**
     * 使用JSR303的Validator校验数据，在此处，初始化Validator
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        validator = Validation.buildDefaultValidatorFactory().usingContext().getValidator();
    }
}
