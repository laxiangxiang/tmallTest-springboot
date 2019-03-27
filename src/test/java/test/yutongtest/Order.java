package test.yutongtest;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import test.yutongtest.annotation.CheckPcs;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 生产订单
 */
@Entity
@Table(name = "mj_pro_order")
@Data
@SequenceGenerator(name = "orderSEQ",sequenceName = "orderDB_SEQ",allocationSize = 1)
@AllArgsConstructor
public class Order implements Serializable{

    @Id
//    @GenericGenerator(name = "idStrategy",strategy = "native")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "orderSEQ")
    private Long id;

    //sap中的订单id号，区分唯一性
    @Column(name = "order_id")
    @NotBlank(message = "订单id号不能为空")
    private String orderId;

    //生产标识
    @Column(name = "productionremark")
    @NotBlank(message = "生产标识不能为空")
    private String productionRemark;

    //物料号
    @Column(name = "material_no")
//    @CheckMaterial
    private String materialNo;

    //整车生产订单
    @Column(name = "total_order")
    @NotBlank(message = "整车生产订单不能为空")
    private String totalOrder;

    //台数
//    private Integer orderNumber;
    @Column(name = "pcs")
//    @NotNull(message = "台数不能为空")
    /*
    todo:2018-06-06 根据需求更改，sap中台数为0的数据传输过来，接收到的数据中没有pcs字段（应该为sap的问题）。台数不做检查，没传值记为0
     */
    @CheckPcs
    private Integer pcs;

    //制件生产订单
    @Column(name = "part_order")
    @NotBlank(message = "制件生产订单不能为空")
    private String partOrder;

    //需求数量
    @Column(name = "req_quantity")
//    @Pattern(message = "需求数量不能为空，并且需为正整数",regexp = "^\\d+$")
    @NotBlank(message = "需求数量不能为空")
    @Pattern(message = "需求数量必须为正整数",regexp = "[0-9]+([.]{1}[0-9]+){0,1}\\s*")
    private String reqQuantity;

    //班次工段
    @Column(name = "section")
    @NotBlank(message = "班次工段不能为空")
    private String section;

    //工位
    @Column(name = "station")
    @NotBlank(message = "工位不能为空")
    private String station;

    //日期 格式
    @Column(name = "order_date")
    @NotBlank(message = "日期不能为空")
    @Pattern(message = "日期格式为：yyyy-MM-dd",regexp = "[0-9]{4}-[0-1]{0,1}[0-9]{0,1}-[0-3]{0,1}[0-9]{0,1}")
    private String orderDate;

    //时间
    @Column(name = "order_time")
    private String orderTime;

    //批次
    @Column(name = "order_batch")
    @NotBlank(message = "批次不能为空")
    @Pattern(message = "批次只能为1，2，3，4",regexp = "^[1-4]{1}$")
    private String orderBatch;

    //保存批次更新前的批次号
    @Column(name = "old_order_batch")
    private String oldOrderBatch;

    /*
    todo:(使用数据时注意去空格，暂时没有具体检验要求)
     */
    //车型编号
    @Column(name = "model_code")
    private String modelCode;

    //车型描述
    @Column(name = "model_desc")
    private String modelDesc;

    //调度员
    @Column(name = "dispatcher")
    private String dispatcher;

    //仓库
    @Column(name = "location")
    private String location;

    //解析后工位
    @Column(name = "analysis_station")
    private String analysisStation;

    //获取时间
    @Column(name = "get_time")
    private String getTime;

    /*
    todo:07-19添加是否已排產字段由宇通填寫
     */
    //是否已經排產
    @Column(name = "has_scheduling")
    private String hasScheduling;

    public Order() {
    }

    public boolean equals(Object o){
        Order order = (Order)o;
        if (String.valueOf(this.id).equals(String.valueOf(order.getId()))){
            return true;
        }else {
            return false;
        }
    }

    public int hashCode(){
//        char[] strChars = this.orderId.toCharArray();
        return (int)(this.id^(this.id>>>32));
    }

}
