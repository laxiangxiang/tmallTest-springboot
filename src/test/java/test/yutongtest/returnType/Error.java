package test.yutongtest.returnType;

import lombok.Data;

@Data
public class Error {

    private String attribute;

    private String cause;

    public Error(String attribute, String cause) {
        this.attribute = attribute;
        this.cause = cause;
    }

    public Error() {

    }
}
