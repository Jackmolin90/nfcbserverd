package com.imooc.pojo.Form;

import com.imooc.form.BaseQueryForm;
import com.imooc.pojo.Param.BlockQueryParam;
import lombok.Data;

@Data
public class BlockQueryForm extends BaseQueryForm<BlockQueryParam> {
    private String address;

    private String revenueAddress;

    private Integer round;

    private Integer type;

    private Integer status;//

    private String startTime;//

    private String endTime;//

    private Long blockNumber;//

    private  String fromAddr;

    private String toAddr;

    private  String candidate;//

    private String txType;// //0 all, 1  normal tx , CandReq , CandExit  ,FlwReq ,FlwExit , Exch

    private String param1;

    private String param2;
    
    
    
    private String contract;
    
    private String authAddress;
}
