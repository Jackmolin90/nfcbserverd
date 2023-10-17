package com.imooc.pojo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "addresses")
@Data
public class Addresses {
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

    @Column(name="inserted_time")
    private Date insertedTime;

    @Column(name = "haslock")
    private Integer haslock;

    @Column(name = "ful_balance")
    private BigDecimal ful_balance;

    @Column(name = "ful_nonce")
    private Long ful_nonce;
    
    @Column(name = "ful_block")
    private Long ful_block;

    @Column(name = "ful_owe")
    private BigDecimal ful_owe;

    public Addresses(String address, Long blockNumber) {
        this.address = address;
        this.blockNumber = blockNumber;
    }

    public Addresses() {
    }
}
