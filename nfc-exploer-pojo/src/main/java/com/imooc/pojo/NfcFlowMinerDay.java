package com.imooc.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.imooc.form.BasePo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zhc 2021-09-15 14:41
 */
@Data
public class NfcFlowMinerDay extends BasePo {


    private String miner_addr;

    private String revenue_address;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date time;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date ctime;

    private BigDecimal bandwidth;

    private Long minerflow;

    private BigDecimal fulnum;

    private BigDecimal profitamount;

    //0 traffic revenue ,1 bandwidth reward
    private  Integer fwflag;


}
