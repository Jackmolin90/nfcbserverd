package com.imooc.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.imooc.form.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zhc 2021-09-15 14:41
 */
@Data
public class NfcCltFlwdata extends BasePo {

    @Id
    private Long id;

    @Column(name = "en_address")
    private String en_address;

    @Column(name = "report_time")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date report_time;

    @Column(name = "flow_value")
    private Long flow_value;

    @Column(name = "real_value")
    private Long real_value;

    @Column(name = "router_address")
    private String router_address;

    @Column(name = "from_addr")
    private String from_addr;

    @Column(name = "to_addr")
    private String to_addr;

    @Column(name = "router_ipaddr")
    private String router_ipaddr;

    @Column(name = "trans_hash")
    private String trans_hash;


    @Column(name = "instime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date instime;

    private BigDecimal bandwidth;


    @Column(name = "fulnum")
    private BigDecimal fulnum;

    @Column(name = "blocknumber")
    private  Long blocknumber;
    @Column(name = "profitamount")
    private BigDecimal profitamount;
    private BigDecimal lock_amount;

    private BigDecimal release_amount;

    private BigDecimal draw_amount;
    private Long lockNumber;

    private  Integer fwflag;

    private String revenue_address;

    private BigDecimal bandwidthreward;

    private BigDecimal lockamount;

    private BigDecimal releaseamount;

    public NfcCltFlwdata() {
    }
    public NfcCltFlwdata(String en_address) {
        this.en_address = en_address;
    }
    public NfcCltFlwdata(String en_address, Long flow_value) {
        this.en_address = en_address;
        this.flow_value = flow_value;
    }
}
