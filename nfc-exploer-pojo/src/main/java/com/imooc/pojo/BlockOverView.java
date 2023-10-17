package com.imooc.pojo;


import java.math.BigDecimal;
public class BlockOverView {
	
	private  BigDecimal totalnfc;
    private BigDecimal nfctogb;
    private  String  netelect;
    private double bandwidth;
    private BigDecimal totalflow;
    private BigDecimal pladgenum;
    private BigDecimal locknum;
    private BigDecimal fwpledgenum;
    private BigDecimal nodepledgenum;
    private BigDecimal nfc24;
    
    public BigDecimal getTotalnfc() {
        return totalnfc;
    }

    public void setTotalnfc(BigDecimal totalnfc) {
        this.totalnfc = totalnfc;
    }
    
    public BigDecimal getNfctogb() {
        return nfctogb;
    }

    public void setNfctogb(BigDecimal nfctogb) {
        this.nfctogb = nfctogb;
    }

    public String getNetelect() {
        return netelect;
    }

    public void setNetelect(String netelect) {
        this.netelect = netelect;
    }
    
    public double getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(double bandwidth) {
        this.bandwidth = bandwidth;
    }

    public BigDecimal getTotalflow() {
        return totalflow;
    }

    public void setTotalflow(BigDecimal totalflow) {
        this.totalflow = totalflow;
    }

    public BigDecimal getPladgenum() {
        return pladgenum;
    }

    public void setPladgenum(BigDecimal pladgenum) {
        this.pladgenum = pladgenum;
    }

    public BigDecimal getLocknum() {
        return locknum;
    }

    public void setLocknum(BigDecimal locknum) {
        this.locknum = locknum;
    }  

    public BigDecimal getFwpledgenum() {
        return fwpledgenum;
    }

    public void setFwpledgenum(BigDecimal fwpledgenum) {
        this.fwpledgenum = fwpledgenum;
    }

    public BigDecimal getNodepledgenum() {
        return nodepledgenum;
    }

    public void setNodepledgenum(BigDecimal nodepledgenum) {
        this.nodepledgenum = nodepledgenum;
    }

	public BigDecimal getNfc24() {
		return nfc24;
	}

	public void setNfc24(BigDecimal nfc24) {
		this.nfc24 = nfc24;
	}    
    
}
