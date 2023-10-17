package com.imooc.pojo.vo;

import java.util.Date;

public class NfcExchgList {

    private String exchgid ;
    /*
    兑换状态 0未兑换 1 已兑换 2兑换中 3 兑换失败
    */
    private Integer exchgStatus ;
    /*
    代币转入0地址或对应管理地址交易hash
    */
    private String applyHash ;
    private Long blocknumber ;
    /*
    兑换交易hash
    */
    private String exchgHash ;

    /*
    兑换地址
    */
    private String applyAddr ;
    /*
    兑换接收地址
    */
    private String exchgIpaddr ;
    /*
    兑换数量
    */
    private Double exchgNum ;
    /*
    兑换类别 NFCE 、NFCE6、NFCE12、NFCE18、NFCE24、OLD_NFC
    */
    private String exchgType ;
    /**
     * 0 待审批  1审批通过  2未通过
     */
    private Integer approveStatus;
    /*
    同步时间
    */
    private Date synctime ;
    /*
    转账时间
    */
    private Date transTime ;

    private String transError;
    private String exchgFrom;
    /*
     * 兑换模式
     */
    private Integer exchgMode;
    /*
     * 锁仓时长
     */
    private Integer lockday;
    /*
     * 释放时长
     */
    private Integer releaseday;
    /*
    *交易时间
     */
    private Date tradetime;
    /*
    兑换数量(NFC)
    */
    private Double exchgToken ;

    public Double getExchgToken() {
        return exchgToken;
    }

    public void setExchgToken(Double exchgToken) {
        this.exchgToken = exchgToken;
    }

    public Date getTradetime() {
        return tradetime;
    }

    public void setTradetime(Date tradetime) {
        this.tradetime = tradetime;
    }

    public Integer getExchgMode() {
        return exchgMode;
    }

    public void setExchgMode(Integer exchgMode) {
        this.exchgMode = exchgMode;
    }

    public Integer getLockday() {
        return lockday;
    }

    public void setLockday(Integer lockday) {
        this.lockday = lockday;
    }

    public Integer getReleaseday() {
        return releaseday;
    }

    public void setReleaseday(Integer releaseday) {
        this.releaseday = releaseday;
    }

    public NfcExchgList() {
    }

    public String getExchgid(){
        return  exchgid;
    }
    public void setExchgid(String exchgid ){
        this.exchgid = exchgid;
    }

    public String getApplyAddr() {
        return applyAddr;
    }

    public void setApplyAddr(String applyAddr) {
        this.applyAddr = applyAddr;
    }

    public Integer getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(Integer approveStatus) {
        this.approveStatus = approveStatus;
    }

    /**
     * 兑换状态 0未兑换 1 已兑换 2兑换中 3 兑换失败
     *@return
     */
    public Integer getExchgStatus(){
        return  exchgStatus;
    }
    /**
     * 兑换状态 0未兑换 1 已兑换 2兑换中 3 兑换失败
     *@param  exchgStatus
     */
    public void setExchgStatus(Integer exchgStatus ){
        this.exchgStatus = exchgStatus;
    }

    /**
     * 代币转入0地址或对应管理地址交易hash
     *@return
     */
    public String getApplyHash(){
        return  applyHash;
    }
    /**
     * 代币转入0地址或对应管理地址交易hash
     *@param  applyHash
     */
    public void setApplyHash(String applyHash ){
        this.applyHash = applyHash;
    }

    public Long getBlocknumber(){
        return  blocknumber;
    }
    public void setBlocknumber(Long blocknumber ){
        this.blocknumber = blocknumber;
    }

    /**
     * 兑换交易hash
     *@return
     */
    public String getExchgHash(){
        return  exchgHash;
    }
    /**
     * 兑换交易hash
     *@param  exchgHash
     */
    public void setExchgHash(String exchgHash ){
        this.exchgHash = exchgHash;
    }

    /**
     * 兑换地址
     *@return
     */
    public String getExchgIpaddr(){
        return  exchgIpaddr;
    }
    /**
     * 兑换地址
     *@param  exchgIpaddr
     */
    public void setExchgIpaddr(String exchgIpaddr ){
        this.exchgIpaddr = exchgIpaddr;
    }

    /**
     * 兑换数量
     *@return
     */
    public Double getExchgNum(){
        return  exchgNum;
    }
    /**
     * 兑换数量
     *@param  exchgNum
     */
    public void setExchgNum(Double exchgNum ){
        this.exchgNum = exchgNum;
    }

    /**
     * 兑换类别 NFCE 、NFCE6、NFCE12、NFCE18、NFCE24、OLD_NFC
     *@return
     */
    public String getExchgType(){
        return  exchgType;
    }
    /**
     * 兑换类别 NFCE 、NFCE6、NFCE12、NFCE18、NFCE24、OLD_NFC
     *@param  exchgType
     */
    public void setExchgType(String exchgType ){
        this.exchgType = exchgType;
    }

    /**
     * 同步时间
     *@return
     */
    public Date getSynctime(){
        return  synctime;
    }
    /**
     * 同步时间
     *@param  synctime
     */
    public void setSynctime(Date synctime ){
        this.synctime = synctime;
    }

    /**
     * 转账时间
     *@return
     */
    public Date getTransTime(){
        return  transTime;
    }
    /**
     * 转账时间
     *@param  transTime
     */
    public void setTransTime(Date transTime ){
        this.transTime = transTime;
    }

    public String getTransError() {
        return transError;
    }

    public void setTransError(String transError) {
        this.transError = transError;
    }

    public String getExchgFrom() {
        return exchgFrom;
    }

    public void setExchgFrom(String exchgFrom) {
        this.exchgFrom = exchgFrom;
    }
}
