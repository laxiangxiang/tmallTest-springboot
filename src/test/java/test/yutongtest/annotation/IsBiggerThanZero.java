package test.yutongtest.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsBiggerThanZero implements ConstraintValidator<CheckNumber,String> {


    @Override
    public void initialize(CheckNumber constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null){
            return false;
        }else {
            double num = Double.valueOf(value.trim());
            if (num > 0){
                return true;
            }else {
                return false;
            }
        }
    }
}
