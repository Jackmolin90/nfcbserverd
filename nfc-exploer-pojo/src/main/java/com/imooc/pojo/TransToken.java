package com.imooc.pojo;

import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class TransToken {

    @Column(name="id")
    private long id;

    @Column(name="transhash")
    private String transHash;

    @Column(name="logindex")
    private Integer loginIndex;

    /*0:ERC20  1:ERC721*/
    @Column(name="cointype")
    private Integer coinType;

    @Column(name="fromaddr")
    private String fromAddr;

    @Column(name="toaddr")
    private String toAddr;

    @Column(name="amount")
    private BigDecimal amount;

    @Column(name="contract")
    private  String  contract;

    @Column(name="blockhash")
    private String blockHash;

    @Column(name="blocknumber")
    private Long blockNumber;

    @Column(name="tokenid")
    private long tokenId;

    @Column(name="gasused")
    private Long gasUsed;

    @Column(name="gasprice")
    private Long gasPrice;

    @Column(name="gaslimit")
    private Long gasLimit;

    @Column(name="nonce")
    private Long nonce;

    @Column(name = "timestamp")
    private Date timeStamp;
}
