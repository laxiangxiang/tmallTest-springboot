package test.yutongtest.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidatePcs implements ConstraintValidator<CheckPcs,Integer> {

    @Override
    public void initialize(CheckPcs constraintAnnotation) {

    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
//        if (value == null || value < 0){
//            return false;
//        }else {
//            return true;
//        }
        if (value == null){
            return true;
        }else if (value >= 0){
            return true;
        }else {
            return false;
        }

    }
}
