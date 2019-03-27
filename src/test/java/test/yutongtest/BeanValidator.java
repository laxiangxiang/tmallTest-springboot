package test.yutongtest;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import test.yutongtest.returnType.ReturnData;
import test.yutongtest.returnType.Error;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

@Slf4j
public class BeanValidator {

    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    private static List<Object> objectList = new ArrayList<>();

    private static List<Map<String,String>> errors = new ArrayList<>();

    /**
     * 单个参数校验
     * @param t
     * @param groups
     * @param <T>
     * @return
     */
    public static <T>void validate(T t, Class... groups){
        Validator validator = validatorFactory.getValidator();
        Set validateResult = validator.validate(t,groups);
        //判断校验结果
//        if (validateResult.isEmpty()){
//            //校验没有错误
//            return Collections.emptyMap();
//        }else {
//            LinkedHashMap error = Maps.newLinkedHashMap();
//            Iterator iterator = validateResult.iterator();
//            while (iterator.hasNext()){
//                ConstraintViolation validation = (ConstraintViolation)iterator.next();
//                error.put(validation.getPropertyPath().toString(),validation.getMessage());
//            }
//            return error;
//        }

        if (!validateResult.isEmpty()){
            objectList.add(t);
            LinkedHashMap error = Maps.newLinkedHashMap();
            Iterator iterator = validateResult.iterator();
            while (iterator.hasNext()){
                ConstraintViolation validation = (ConstraintViolation)iterator.next();
                error.put(validation.getPropertyPath().toString(),validation.getMessage());
            }
            errors.add(error);
        }
        return;
    }

    public static <T>ReturnData validateObject(T t, Class... groups){
        Validator validator = validatorFactory.getValidator();
        List<Error> errors = new ArrayList<>();
        Set validateResult = validator.validate(t,groups);
        ReturnData returnData = null;
        if (!validateResult.isEmpty()){
            Iterator iterator = validateResult.iterator();
            while (iterator.hasNext()){
                ConstraintViolation validation = (ConstraintViolation)iterator.next();
                Error error = new Error(validation.getPropertyPath().toString(),validation.getMessage());
                errors.add(error);
            }
            returnData = ReturnData.fail(t,errors);
        }else {
            returnData = ReturnData.success(t);
        }
        return returnData;
    }

    /**
     * 集合参数校验
     * @param collection
     * @return
     */
    public static ReturnData validateList(Collection<?> collection){
        log.debug("开始验证集合参数。。。");
        if (objectList.size() != 0 ){
            objectList.clear();
        }
        if (errors.size() != 0){
            errors.clear();
        }
        //判断集合是否为空
        Preconditions.checkNotNull(collection);
        Iterator iterator = collection.iterator();
//        Map error ;
//        do {
//            if (!iterator.hasNext()){
//                return Collections.emptyMap();
//            }
//            Object object = iterator.next();
//            error = validate(object,new Class[0]);
//        }while (error.isEmpty());
//        return error;
        while (iterator.hasNext()){
            Object obj = iterator.next();
            log.debug(obj.toString());
            validate(obj,new Class[0]);
        }
        ReturnData returnData = null;
        if (objectList.size() == 0 && errors.size() == 0){
            returnData = ReturnData.success();
        }
        return returnData;
//        else {
//            ReturnData returnData = ReturnData.fail(objectList,errors);
//            return returnData;
//        }
    }

    /**
     * 对以上两个方法进行包装
     * @param first
     * @param objects
     * @return
     */
//    public static Map<String,String> validateObject(Object first,Object... objects){
//        if (objects != null && objects.length > 0){
//            return validateList(Lists.asList(first,objects));
//        }else {
//            return validate(first,new Class[0]);
//        }
//    }

    /**
     * 参数校验
     */
    public static void check(Object param){
        log.info("validate param");
//        Map<String,String> map = BeanValidator.validateObject(param);
//        if (MapUtils.isNotEmpty(map)){
//            throw new ParamException(map.toString());
//        }
    }
}
