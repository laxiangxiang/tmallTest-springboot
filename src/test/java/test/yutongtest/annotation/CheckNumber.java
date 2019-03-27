package test.yutongtest.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsBiggerThanZero.class)
public @interface CheckNumber {
    String message() default "";
    Class<?> [] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
