package test.yutongtest.returnType;

import lombok.Data;

import java.util.List;

@Data
public class ReturnData {

    //返回状态
    private String status;

    //校验错误的数据
    private Object object;

    //错误原因
    private List<Error> errors;

    public ReturnData(String status) {
        this.status = status;
    }

    public static ReturnData success(){
        return new ReturnData("S");
    }

    public static ReturnData fail(){
        return new ReturnData("F");
    }

    public ReturnData(String status, Object object,List<Error> errors) {
        this.status = status;
        this.object = object;
        this.errors = errors;
    }
    public static ReturnData fail(Object object, List<Error> errors){
        ReturnData returnData = ReturnData.fail();
        returnData.object = object;
        returnData.errors = errors;
        return returnData;
    }

    public static ReturnData success(Object object){
        ReturnData returnData = ReturnData.success();
        returnData.object = object;
        return  returnData;
    }

    @Override
    public String toString() {
        return "ReturnData{" +
                "status='" + status + '\'' +
                ", object=" + object +
                ", errors=" + errors +
                '}';
    }
}
