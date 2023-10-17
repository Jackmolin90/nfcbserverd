package com.imooc.pojo.vo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.math.BigDecimal;
@Data
public class AddressVo {
    @Id
    private Long id;

    @Column(name = "address")
    private String address;

    @Column(name = "contract")
    private String contract;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "nonce")
    private Long nonce;

    @Column(name = "blocknumber")
    private Long blockNumber;

    private String proportion;

    private Long toCount;

    @Column(name = "haslock")
    private Integer haslock;


    @Column(name = "ful_balance")
    private BigDecimal ful_balance;

    @Column(name = "ful_nonce")
    private Long ful_nonce;

    @Column(name = "ful_owe")
    private BigDecimal ful_owe;


    private String appid;

    private String random;

    private String sign;

}
