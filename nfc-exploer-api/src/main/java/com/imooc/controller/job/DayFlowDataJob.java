package com.imooc.controller.job;

import com.imooc.Utils.AgentSvcTask;
import com.imooc.mapper.NfcFlowMinerMapper;
import com.imooc.mapper.TransferMinerMapper;
import com.imooc.pojo.NfcCltFlwdata;
import com.imooc.pojo.NfcFlowMiner;
import com.imooc.pojo.NfcNetStatics;
import com.imooc.pojo.TransferMiner;
import com.imooc.service.util.TxDataParse;
import com.imooc.utils.Constants;
import com.imooc.utils.DateUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.AlienSnapshot;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author zhc 2021-11-18 13:56
 */
public class DayFlowDataJob extends AgentSvcTask {
    private static Logger logger = LoggerFactory.getLogger(DayFlowDataJob.class);
    private NfcFlowMinerMapper nfcFlowMinerMapper;
    private TransferMinerMapper transferMinerMapper;
    private Set<Long> flowBlockSet;
    private Long blockNumber;
    private Web3j web3j;
    private Date timeStamp;
    private Date yesterday;
    private String decimals = "1000000000000000000";
    private int M_G = 1024;

    public DayFlowDataJob(Long blockNumber,TransferMinerMapper transferMinerMapper,Date yesterday, Date timeStamp, Web3j web3j,Set<Long> flowBlockSet, NfcFlowMinerMapper nfcFlowMinerMapper) {
        this.transferMinerMapper = transferMinerMapper;
        this.blockNumber = blockNumber;
        this.yesterday = yesterday;
        this.timeStamp = timeStamp;
        this.web3j = web3j;
        this.flowBlockSet = flowBlockSet;
        this.nfcFlowMinerMapper = nfcFlowMinerMapper;
    }

    protected  void runTask() {
        long starttime = System.currentTimeMillis();
        logger.info("DayFlowDataJob start .....");
        try {
            //dayFlow
            for (Long blockNumber : flowBlockSet) {
                logger.info("DayFlowDataClt blockNumber="+blockNumber);
                AlienSnapshot.Snapshot snapDay = web3j.alienSnapshotFlowAtNumber(blockNumber.intValue()).send().getSnapshot();
                dayFlow(snapDay,blockNumber);
            }
            //netStatics
            netStatics();
            //Re-release the last record
            new TransferAddJob(yesterday,blockNumber,nfcFlowMinerMapper,transferMinerMapper).start();
        }catch (Exception e) {
            logger.error("DayFlowDataJob error,"+e.getMessage(),e);
        }
        long endtime = System.currentTimeMillis();
        logger.info("DayFlowDataJob end ,spends total second ="+(endtime-starttime)/1000);
    }

    private void dayFlow(AlienSnapshot.Snapshot snapDay,Long blockNumber){
        List<NfcCltFlwdata> cltList = new ArrayList<>();
        List<TransferMiner> tfUpdate = new ArrayList<>();
        List<TransferMiner> tfInsert = new ArrayList<>();
        long dayFlowStarttime = System.currentTimeMillis();
        logger.info("dayFlow start .....");
        BigDecimal rate= new BigDecimal(decimals);
        //dayLockFLow
        List<Map<String, Object>> daylockreward=snapDay.getFlowrecords();
        if (null!=daylockreward&&daylockreward.size()>0) {
            for(Map<String, Object> dayLock:daylockreward){
                NfcCltFlwdata clt = new NfcCltFlwdata((String) dayLock.get("Target"));
                clt.setBlocknumber(blockNumber);
                clt.setProfitamount(new BigDecimal(dayLock.get("Amount").toString()));
                clt.setReport_time(yesterday);
                clt.setInstime(new Date());
                clt.setFlow_value(Long.parseLong(dayLock.get("validFlowvalue").toString()));
                clt.setReal_value(Long.parseLong(dayLock.get("realFlowvalue").toString()));
                clt.setFulnum(new BigDecimal(clt.getReal_value()).multiply(new BigDecimal(Constants.M_FUl))
                        .multiply(new BigDecimal(Constants.decimals)));
                clt.setFwflag(0);
                cltList.add(clt);
                if(cltList.size()>0&&cltList.size()/Constants.BATCHCOUNT>=1){
                    nfcFlowMinerMapper.batchSaveFlwDataDay(cltList);
                    cltList = new ArrayList<>();
                }
            }
            if(cltList.size()>0){
                nfcFlowMinerMapper.batchSaveFlwDataDay(cltList);
                cltList = new ArrayList<>();
            }
            logger.info("blocknumber="+blockNumber+" dayFlow="+daylockreward.size());
        }
        long dayFlowEndtime = System.currentTimeMillis();
        logger.info("blocknumber="+blockNumber+" dayFlow end ,spends total second ="+(dayFlowEndtime-dayFlowStarttime)/1000);
    }

    private  void netStatics(){
        long starttime = System.currentTimeMillis();
        logger.info("NetStatics start .....");
        //NetStatics
        NfcNetStatics nns = new NfcNetStatics();
        nns.setCtime(yesterday);
        nns.setTotal_nfc(BigDecimal.ZERO);
        nns.setTotalflow(BigDecimal.ZERO);
        nns.setTotal_bw(BigDecimal.ZERO);
        NfcCltFlwdata flw = nfcFlowMinerMapper.queryNfcCltFlwdataDayByTime(DateUtil.getFormatDateTime(yesterday, "yyyy-MM-dd"));
        if(flw!=null){
            nns.setTotal_nfc(flw.getProfitamount());
            nns.setTotalflow(new BigDecimal(flw.getFlow_value()));
        }
        NfcFlowMiner miner = nfcFlowMinerMapper.getMinerSum();
        if(miner!=null){
            nns.setTotal_bw(miner.getBandwidth());
        }
        NfcNetStatics prenns =  nfcFlowMinerMapper.queryNfcNetStaticsByCtime(DateUtil.getFormatDateTime(DateUtil.addDaysToDate(yesterday,-1), "yyyy-MM-dd"));
        if(prenns!=null){
            nns.setIncre_bw(nns.getTotal_bw().subtract(prenns.getTotal_bw()));
            nns.setIncre_flow(nns.getTotalflow().subtract(prenns.getTotalflow()));
        }else{
            nns.setIncre_bw(nns.getTotal_bw());
            nns.setIncre_flow(nns.getTotalflow());
        }
        if(nns.getTotalflow().compareTo(BigDecimal.ZERO)==0){
            nns.setNfc_gbRate(BigDecimal.ZERO);
        }else{
            nns.setNfc_gbRate(nns.getTotal_nfc().divide(new BigDecimal(decimals),20, BigDecimal.ROUND_HALF_UP).
                    multiply(new BigDecimal(M_G)).divide(nns.getTotalflow(),4, BigDecimal.ROUND_HALF_UP));
        }
        nfcFlowMinerMapper.insertNfcNetStatics(nns);
        long endtime = System.currentTimeMillis();
        logger.info("NetStatics end ,spends total second ="+(endtime-starttime)/1000);
    }
}
