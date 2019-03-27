package test.yutongtest.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidatePcs.class)
public @interface CheckPcs {
    String message() default "台数必须大于或者等于0";
    Class<?> [] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
