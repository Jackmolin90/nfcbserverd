package com.imooc.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class NfcNodeMiner {

    @Column(name="id")
    private Long id;

    @Column(name="node_address")
    private String  node_address;

    @Column(name="revenue_address")
    private String  revenue_address;

    /*1 Candidate node 2 Witness node 3 Exit node*/
    @Column(name="node_type")
    private Integer node_type;

    @Column(name="fractions")
    private Integer fractions;

    @Column(name = "pledge_amount")
    private BigDecimal pledge_amount;

    @Column(name="blocknumber")
    private Long blocknumber;

    @Column(name = "join_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date join_time;

    @Column(name = "sync_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date sync_time;

    private BigDecimal leftamount;


    private BigDecimal lockamount;

    private BigDecimal releaseamount;


    private BigDecimal exitlockamount;

    private BigDecimal exitreleaseamount;









}
