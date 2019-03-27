package test.yutongtest.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidateTechInfo implements ConstraintValidator<CheckTechInfo,String> {

    @Override
    public void initialize(CheckTechInfo constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return techInfoAnalysisTest(value);
    }

    private boolean techInfoAnalysisTest(String techInfo){
        try{
            if (techInfo.contains("冲(")){
//                String value1 = techInfo.split("冲")[1];
                String value1 = techInfo.substring(techInfo.indexOf("冲"));
                String value = value1.substring(value1.indexOf("(")+1,value1.indexOf(")"));
                if (!value.contains("如图")){
                    String punchingSurface = value.substring(0,value.indexOf("-"));
                    Integer.parseInt(punchingSurface);
                }
            }
            if (techInfo.contains("B") && techInfo.contains("扳(")){
//                String value1 = techInfo.split("扳")[1];
                String value1 = techInfo.substring(techInfo.indexOf("扳"));
                String value = value1.substring(value1.indexOf("(")+1,value1.indexOf(")"));
                String wrench = value.substring(0,value.indexOf("-"));
                Integer.parseInt(wrench);
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
