package com.imooc.controller.job;

import com.imooc.Utils.AgentSvcTask;
import com.imooc.Utils.TransferAddUtil;
import com.imooc.mapper.NfcFlowMinerMapper;
import com.imooc.mapper.TransferMinerMapper;
import com.imooc.pojo.Addresses;
import com.imooc.pojo.NfcCltFlwdata;
import com.imooc.pojo.TransferMiner;
import com.imooc.service.util.TxDataParse;
import com.imooc.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.AlienSnapshot;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author zhc 2021-11-18 13:56
 */
public class BlockLockReleaseDataJob extends AgentSvcTask {
    private static Logger logger = LoggerFactory.getLogger(BlockLockReleaseDataJob.class);
    private NfcFlowMinerMapper nfcFlowMinerMapper;
    private TransferMinerMapper transferMinerMapper;
    private Long blockNumber;
    private Web3j web3j;
    private  Date timeStamp;
    private Date yesterday;
    private String decimals = "1000000000000000000";
    private String blockRewardType = "3";
    private String flowRewardType = "4";
    private String bandwidthRewardType = "5";

    public BlockLockReleaseDataJob(Date yesterday, Date timeStamp, Web3j web3j, Long blockNumber, NfcFlowMinerMapper nfcFlowMinerMapper, TransferMinerMapper transferMinerMapper) {
        this.yesterday = yesterday;
        this.timeStamp = timeStamp;
        this.web3j = web3j;
        this.blockNumber = blockNumber;
        this.nfcFlowMinerMapper = nfcFlowMinerMapper;
        this.transferMinerMapper = transferMinerMapper;
    }

    protected  void runTask() {
        long starttime = System.currentTimeMillis();
        logger.info("BlockLockReleaseDataJob start .....");
        try {
//            //blockLockRelease
            blockLockRelease();
            TransferAddUtil t = new TransferAddUtil();
            t.transferAdd(transferMinerMapper,blockNumber,1);
        }catch (Exception e) {
            logger.error("BlockLockReleaseDataJob error,"+e.getMessage(),e);
        }
        long endtime = System.currentTimeMillis();
        logger.info("BlockLockReleaseDataJob end ,spends total second ="+(endtime-starttime)/1000);
    }


    private void blockLockRelease() throws Exception {
        AlienSnapshot.Snapshot rewardlock = web3j.alienSnapshotReleaseAtNumber(blockNumber.intValue(),"rewardlock").send().getSnapshot();
        lockRewardlock(rewardlock);
    }

    private void lockRewardlock(AlienSnapshot.Snapshot rewardlock) {
        List<TransferMiner> tfUpdate = new ArrayList<>();
        List<TransferMiner> tfInsert = new ArrayList<>();
        long rewardLockReleaseStarttime = System.currentTimeMillis();
        logger.info("blockLockReleaseStarttime start .....");
        Map<String, Map<String, Map<String, Object>>> flowrevenve= rewardlock.getFlowrevenve();
        //flow lock and release
        if(null!=flowrevenve&&flowrevenve.size()>0){
            Map<String,Addresses> addressMap = new HashMap<>();
            logger.info("blocknumber="+blockNumber+" rewardlock="+flowrevenve.size());
            TransferMiner tf = new TransferMiner();
            tf.setType(1);
            List<TransferMiner> tm1 = transferMinerMapper.getTransferMinerList(tf);
            Map<String,TransferMiner> tm1Map = new HashMap<>();
            for (TransferMiner vo:tm1) {
                tm1Map.put(vo.getAddress().toLowerCase()+'_'+vo.getBlockNumber(),vo);
            }
            for(Map.Entry<String, Map<String, Map<String, Object>>> record:flowrevenve.entrySet()){
                String address = record.getKey();
                Map<String, Map<String, Object>> rMap = record.getValue();
                Map<String, Object> lockbalance = rMap.get("lockbalance");
                if (null != lockbalance && lockbalance.size() > 0) {
                    for (Map.Entry<String, Object> lockB: lockbalance.entrySet()) {
                        Map<String, Object> lock = (Map<String, Object>)lockB.getValue();
                        Long bNumber = Long.parseLong(lockB.getKey());
                        Map<String,Map<String, Object>> lockV = (Map<String,  Map<String, Object>>)lockB.getValue();
                        Map<String, Object> blockReward = lockV.get(blockRewardType);
                        if(blockReward!=null){
                            BigDecimal lockamount = new BigDecimal(blockReward.get("lockamount").toString());
                            BigDecimal playment = new BigDecimal(blockReward.get("playment").toString());
                            TransferMiner tm = tm1Map.get(address.toLowerCase()+"_"+bNumber);
                            tm1Map.remove(address.toLowerCase()+"_"+bNumber);
                            if(null==tm){
                                TransferMiner transferMiner= new TransferMiner();
                                transferMiner.setTxHash(UUID.randomUUID().toString());
                                transferMiner.setAddress(address);
                                transferMiner.setBlockNumber(bNumber);
                                transferMiner.setTotalAmount(lockamount);
                                transferMiner.setType(1);
                                transferMiner.setReleaseHeigth(Long.parseLong(blockReward.get("releaseperiod").toString()));
                                transferMiner.setReleaseInterval(Long.parseLong(blockReward.get("releaseinterval").toString()));
                                transferMiner.setLockNumHeight(Long.parseLong(blockReward.get("lockperiod").toString()));
                                transferMiner.setRevenueaddress(blockReward.get("revenueaddress").toString());
                                transferMiner.setUnLockNumber(transferMiner.getBlockNumber()+transferMiner.getLockNumHeight());
                                transferMiner.setReleaseamount(playment);
                                tfInsert.add(transferMiner);
                                if(playment.compareTo(BigDecimal.ZERO)!=0){
                                    TransferMiner transRelease= new TransferMiner();
                                    transRelease.setTxHash(UUID.randomUUID().toString());
                                    transRelease.setRevenueaddress(blockReward.get("revenueaddress").toString());
                                    transRelease.setAddress(address);
                                    transRelease.setBlockNumber(bNumber);
                                    transRelease.setType(2);
                                    transRelease.setCurtime(timeStamp);
                                    transRelease.setReleaseamount(playment);
                                    tfInsert.add(transRelease);
                                    if(null!=transferMiner.getRevenueaddress()){
                                        addressMap.put(transferMiner.getRevenueaddress(),new Addresses(transferMiner.getRevenueaddress(),blockNumber));
                                    }
                                }
                            }else{
                                if(tm.getReleaseamount().compareTo(playment) == -1){ //plament>node release
                                    TransferMiner transferMiner= new TransferMiner();
                                    transferMiner.setTxHash(UUID.randomUUID().toString());
                                    transferMiner.setRevenueaddress(blockReward.get("revenueaddress").toString());
                                    transferMiner.setAddress(address);
                                    transferMiner.setBlockNumber(bNumber);
                                    transferMiner.setType(2);
                                    transferMiner.setCurtime(timeStamp);
                                    transferMiner.setReleaseamount(playment.subtract(tm.getReleaseamount()));
                                    tfInsert.add(transferMiner);
                                    tm.setReleaseamount(playment);
                                    tfUpdate.add(tm);
                                    if(null!=transferMiner.getRevenueaddress()){
                                        addressMap.put(transferMiner.getRevenueaddress(),new Addresses(transferMiner.getRevenueaddress(),blockNumber));
                                    }
                                }
                            }
                            if(tfUpdate.size()>0&& tfUpdate.size()/Constants.BATCHCOUNT>=1){
                                transferMinerMapper.updateMinerTransfRelaseBatch(tfUpdate);
                                tfUpdate = new ArrayList<>();
                            }
                            if(tfInsert.size()>0&& tfInsert.size()/Constants.BATCHCOUNT>=1){
                                transferMinerMapper.insertBatch(tfInsert);
                                tfInsert = new ArrayList<>();
                            }
                        }
                    }
                }
            }
            if(tfUpdate.size()>0){
                transferMinerMapper.updateMinerTransfRelaseBatch(tfUpdate);
            }
            if(tfInsert.size()>0){
                transferMinerMapper.insertBatch(tfInsert);
            }
            for (Map.Entry<String,Addresses> address : addressMap.entrySet()) {
                AddressBalanceModifyJob.getInstance().putq(address.getValue());
            }
        }
        long rewardLockReleaseEndtime = System.currentTimeMillis();
        logger.info("blocknumber="+blockNumber+" blockLockReleaseRelease end ,spends total second ="+(rewardLockReleaseEndtime-rewardLockReleaseStarttime)/1000);
    }
}
