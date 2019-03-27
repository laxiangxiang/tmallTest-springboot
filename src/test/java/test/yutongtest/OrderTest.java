package test.yutongtest;

import test.yutongtest.returnType.ReturnData;

/**
 * Created by LXX on 2019/3/11.
 */
public class OrderTest {

    public static void main(String[] args) {
        Order order = new Order(null,"0003030031710003820562649412-025040060061W?183020190308",
                "2019-03-08","9412-025040060061","000303003171",3,"000382056264","3.000 ","W","空",
                "2019-03-13","15:00:00","1",null,"B369","ZK6120HQ(11.6?20?????????)","Z08","BZ01","空",null,null);
        String orderStr =
                "orderId=0003030031710003820562649412-025040060061W?183020190308, " +
                "productionRemark=2019-03-08, " +
                "materialNo=9412-025040060061, " +
                "totalOrder=000303003171, " +
                "pcs=3, " +
                "partOrder=000382056264, " +
                "reqQuantity=3.000 , " +
                "section=W, " +
                "station=空, " +
                "orderDate=2019-03-13, " +
                "orderTime=15:00:00, " +
                "orderBatch=1, " +
                "oldOrderBatch=null, " +
                "modelCode=B369, " +
                "modelDesc=ZK6120HQ(11.6?20?????????), " +
                "dispatcher=Z08, " +
                "location=BZ01, " +
                "analysisStation=?, " +
                "getTime=null, " +
                "hasScheduling=null";
        ReturnData returnData = BeanValidator.validateObject(order);
    }
}
