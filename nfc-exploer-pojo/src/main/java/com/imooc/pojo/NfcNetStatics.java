package com.imooc.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class NfcNetStatics {


    /* yyyy-MM-dd*/
    @Column(name="ctime")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date  ctime;

    /**
     * Exchange traffic value per GB
     */
    @Column(name = "nfc_gbRate")
    private BigDecimal nfc_gbRate;

    /**
     * The number of NFCs produced by traffic mining and bandwidth mining in the last 24 hours
     */
    @Column(name = "total_nfc")
    private BigDecimal total_nfc;

    /**
     * Total daily traffic MB
     */
    @Column(name = "totalflow")
    private BigDecimal totalflow;


    /**
     * Increased traffic compared to the previous day
      */
    @Column(name = "incre_flow")
    private BigDecimal incre_flow;
    /**
     * Total network bandwidth
     */
    @Column(name = "total_bw")
    private BigDecimal total_bw;
    /**
     * Increased bandwidth compared to the previous day
     */
    @Column(name = "incre_bw")
    private BigDecimal incre_bw;





}
