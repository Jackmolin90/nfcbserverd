package com.imooc.controller.job;

import com.imooc.Utils.AgentSvcTask;
import com.imooc.mapper.NfcFlowMinerMapper;
import com.imooc.mapper.TransferMinerMapper;
import com.imooc.pojo.NfcCltFlwdata;
import com.imooc.pojo.NfcFlowMiner;
import com.imooc.pojo.NfcNetStatics;
import com.imooc.pojo.TransferMiner;
import com.imooc.utils.Constants;
import com.imooc.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author zhc 2021-11-18 13:56
 */
public class MinerModifyJob extends AgentSvcTask {
    private static Logger logger = LoggerFactory.getLogger(MinerModifyJob.class);
    private NfcFlowMinerMapper nfcFlowMinerMapper;
    private TransferMinerMapper transferMinerMapper;
    private Date yesterday;

    public MinerModifyJob(Date yesterday,NfcFlowMinerMapper nfcFlowMinerMapper, TransferMinerMapper transferMinerMapper) {
        this.yesterday = yesterday;
        this.nfcFlowMinerMapper = nfcFlowMinerMapper;
        this.transferMinerMapper = transferMinerMapper;
    }

    protected  void runTask() {
        long starttime = System.currentTimeMillis();
        logger.info("MinerModifyJob start .....");
        minerModify();
        nfcDayLockRealseModify();
        long endtime = System.currentTimeMillis();
        logger.info("MinerModifyJob end ,spends total second ="+(endtime-starttime)/1000);
    }

    private void minerModify() {
        try {
            //miner status
            List<NfcFlowMiner> minerList = nfcFlowMinerMapper.getAllMinerAddress();
            if(minerList!=null&&minerList.size()>0){
                List<NfcCltFlwdata>  flwList = nfcFlowMinerMapper.queryNfcCltFlwdataDayGroupBYAddress();	//TODO
                Map<String,NfcCltFlwdata> cltAddressMap = new HashMap<>();
                for (NfcCltFlwdata data: flwList) {
                    cltAddressMap.put(data.getEn_address().toLowerCase(),data);
                }
                List<TransferMiner> transfList = transferMinerMapper.getReleaseamountGroupByAddress();
                Map<String,TransferMiner> transfAddressMap = new HashMap<>();
                for (TransferMiner data: transfList) {
                    transfAddressMap.put(data.getAddress().toLowerCase(),data);
                }
                List<NfcFlowMiner> list = new ArrayList<>();
                for (NfcFlowMiner minner: minerList) {
                    String mAddress = minner.getMiner_addr().toLowerCase();
                    NfcCltFlwdata  mflw = cltAddressMap.get(mAddress);
                    if (mflw != null) {
                        minner.setMiner_flow(mflw.getFlow_value());
                        minner.setPayful(mflw.getFulnum());
                 //       minner.setRevenue_amount(mflw.getProfitamount());
                        minner.setRevenue_amount(minner.getLock_amount());
                        cltAddressMap.remove(mAddress);
                    }
                    TransferMiner tm = transfAddressMap.get(mAddress);
                    if (tm != null) {
                    	minner.setRevenue_amount(tm.getTotalAmount());	//TODO
                        minner.setRelease_amount(tm.getReleaseamount());
                        minner.setLock_amount(tm.getTotalAmount());                        
                        transfAddressMap.remove(mAddress);
                    }
                    minner.setSync_time(new Date());
                    list.add(minner);
                    if(list.size()>0&&list.size()/Constants.BATCHCOUNT>=1){
                        nfcFlowMinerMapper.updateFlowMinerBatch(list);
                        list = new ArrayList<>();
                    }
                }
                if(list.size()>0){
                    nfcFlowMinerMapper.updateFlowMinerBatch(list);
                }
            }
        }catch (Exception e) {
            logger.error("MinerModifyJob error,"+e.getMessage(),e);
        }
    }
    private void nfcDayLockRealseModify() {
        try {
            List<NfcCltFlwdata> nfcCltList = nfcFlowMinerMapper.getFlwDataDayListByReportTime(DateUtil.getFormatDateTime(yesterday, "yyyy-MM-dd"));
            if(nfcCltList!=null&&nfcCltList.size()>0){
                List<TransferMiner> flowList = transferMinerMapper.getReleaseamountGroupByAddressAndType(5);
                Map<String,TransferMiner> flowMap = new HashMap<>();
                for (TransferMiner data: flowList) {
                    flowMap.put(data.getAddress().toLowerCase(),data);
                }
                List<TransferMiner> bandwidthList = transferMinerMapper.getReleaseamountGroupByAddressAndType(9);
                Map<String,TransferMiner> bandwidthMap = new HashMap<>();
                for (TransferMiner data: bandwidthList) {
                    bandwidthMap.put(data.getAddress().toLowerCase(),data);
                }
                List<NfcCltFlwdata> list = new ArrayList<>();
                for (NfcCltFlwdata clt: nfcCltList) {
                    String mAddress = clt.getEn_address().toLowerCase();
                    if(clt.getFwflag()==0){
                        TransferMiner  mflw = flowMap.get(mAddress);
                        if (mflw != null) {
                            clt.setLockamount(mflw.getTotalAmount());
                            clt.setReleaseamount(mflw.getReleaseamount());
                            list.add(clt);
                        }
                    }else{
                        TransferMiner  mbandwidth = bandwidthMap.get(mAddress);
                        if (mbandwidth != null) {
                            clt.setLockamount(mbandwidth.getTotalAmount());
                            clt.setReleaseamount(mbandwidth.getReleaseamount());
                            list.add(clt);
                        }
                    }
                    if(list.size()>0&&list.size()/Constants.BATCHCOUNT>=1){
                        nfcFlowMinerMapper.batchUpdateFlwDataDay(list);
                        list = new ArrayList<>();
                    }
                }
                if(list.size()>0){
                    nfcFlowMinerMapper.batchUpdateFlwDataDay(list);
                }
            }
        }catch (Exception e) {
            logger.error("nfcDayLockRealseModify error,"+e.getMessage(),e);
        }
    }

}
