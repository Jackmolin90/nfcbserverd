package com.imooc.controller.job;

import com.imooc.Utils.AgentSvcTask;
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

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author zhc 2021-11-18 13:56
 */
public class RewardLockReleaseDataJob extends AgentSvcTask {
    private static Logger logger = LoggerFactory.getLogger(RewardLockReleaseDataJob.class);
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

    public RewardLockReleaseDataJob(Date yesterday, Date timeStamp, Web3j web3j, Long blockNumber, NfcFlowMinerMapper nfcFlowMinerMapper, TransferMinerMapper transferMinerMapper) {
        this.yesterday = yesterday;
        this.timeStamp = timeStamp;
        this.web3j = web3j;
        this.blockNumber = blockNumber;
        this.nfcFlowMinerMapper = nfcFlowMinerMapper;
        this.transferMinerMapper = transferMinerMapper;
    }

    protected  void runTask() {
        long starttime = System.currentTimeMillis();
        logger.info("RewardLockReleaseDataJob start .....");
        try {
            // node -- candidatepledge
            AlienSnapshot.Snapshot candidatepledge=web3j.alienSnapshotReleaseAtNumber(blockNumber.intValue(),"candidatepledge").send().getSnapshot();
            pledgeLockRelease(candidatepledge.getCandidatepledge(), 3, 4);
//            //miner -- flowminerpledge
            AlienSnapshot.Snapshot flowminerpledge=web3j.alienSnapshotReleaseAtNumber(blockNumber.intValue(),"flowminerpledge").send().getSnapshot();
            pledgeLockRelease(flowminerpledge.getFlowminerpledge(), 7, 8);
//            //flow lock and release
            rewardLockRelease();

        }catch (Exception e) {
            logger.error("rewardLockReleaseDataJob error,"+e.getMessage(),e);
        }
        long endtime = System.currentTimeMillis();
        logger.info("rewardLockReleaseDataJob end ,spends total second ="+(endtime-starttime)/1000);
    }

    /**
     * lockRelease record
     * @param candidatepledge flowminerpledge
     * @param i2
     * @param i3
     */
    private void pledgeLockRelease(Map<String, Map<String, Object>> candidatepledge, int i2, int i3) {
        if (null != candidatepledge && candidatepledge.size() > 0) {
            Map<String,Addresses> addressMap = new HashMap<>();
            TransferMiner tf = new TransferMiner();
            tf.setType(i2);
            List<TransferMiner> lockList = transferMinerMapper.getLockByType(tf);
            Map<String,TransferMiner> lockMap = new HashMap<>();
            for (TransferMiner lock:lockList) {
                lockMap.put(lock.getAddress(),lock);
            }
            List<TransferMiner> tfUpdate = new ArrayList<>();
            List<TransferMiner> tfInsert = new ArrayList<>();
            for (Map.Entry<String, Map<String, Object>> cand : candidatepledge.entrySet()) {
                Map<String, Object> lockVal = cand.getValue();
                Long startblocknumber = Long.parseLong(lockVal.get("startblocknumber").toString());
                BigDecimal playment = new BigDecimal(lockVal.get("playment").toString());
                TransferMiner node = lockMap.get(cand.getKey());
                if (startblocknumber.intValue() != 0 &&node != null) {
                        if (node.getReleaseamount().compareTo(playment) == -1) { //plament>node release
                            TransferMiner transferMiner = new TransferMiner();
                            transferMiner.setTxHash(UUID.randomUUID().toString());
                            transferMiner.setAddress(cand.getKey());
                            transferMiner.setBlockNumber(node.getBlockNumber());
                            transferMiner.setType(i3);
                            transferMiner.setRevenueaddress(lockVal.get("revenueaddress").toString());
                            transferMiner.setCurtime(timeStamp);
                            transferMiner.setReleaseamount(playment.subtract(node.getReleaseamount()));
                            tfInsert.add(transferMiner);
                            //updateMiner
                            node.setReleaseamount(playment);
                            tfUpdate.add(node);
                            if(null!=transferMiner.getRevenueaddress()){
                                addressMap.put(transferMiner.getRevenueaddress(),new Addresses(transferMiner.getRevenueaddress(),blockNumber));
                            }
                        }
                }
                if(tfUpdate.size()>0&&tfUpdate.size()/Constants.BATCHCOUNT>=1){
                    transferMinerMapper.updateMinerTransfRelaseBatch(tfUpdate);
                    tfUpdate = new ArrayList<>();
                }
                if(tfInsert.size()>0&&tfInsert.size()/Constants.BATCHCOUNT>=1){
                    transferMinerMapper.insertBatch(tfInsert);
                    tfInsert = new ArrayList<>();
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
    }

    private void rewardLockRelease() throws Exception {
        AlienSnapshot.Snapshot bandwidthlock = web3j.alienSnapshotReleaseAtNumber(blockNumber.intValue(),"bandwidthlock").send().getSnapshot();
        bandwidthlock(bandwidthlock);
        AlienSnapshot.Snapshot flowlock = web3j.alienSnapshotReleaseAtNumber(blockNumber.intValue(),"flowlock").send().getSnapshot();
        flowlock(flowlock);
    }

    private void flowlock(AlienSnapshot.Snapshot flowlock) {
        List<TransferMiner> tfUpdate = new ArrayList<>();
        List<TransferMiner> tfInsert = new ArrayList<>();
        long flowlockReleaseStarttime = System.currentTimeMillis();
        logger.info("flowlockReleaseStarttime start .....");
        Map<String, Map<String, Map<String, Object>>> flowrevenve= flowlock.getFlowrevenve();
        Set<Long> flowBlockSet=new HashSet<>();
        if(null!=flowrevenve&&flowrevenve.size()>0) {
            Map<String,Addresses> addressMap = new HashMap<>();
            Long maxDayFlowBlockNumber = transferMinerMapper.getDayFLowMaxBlockNumber();
            logger.info("blocknumber=" + blockNumber + " flowlock=" + flowrevenve.size());
            TransferMiner tf = new TransferMiner();
            tf.setType(5);
            List<TransferMiner> tm5 = transferMinerMapper.getTransferMinerList(tf);
            Map<String,TransferMiner> tm5Map = new HashMap<>();
            for (TransferMiner vo:tm5) {
                tm5Map.put(vo.getAddress().toLowerCase()+'_'+vo.getBlockNumber(),vo);
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
                        Map<String, Object> flowReward = lockV.get(flowRewardType);
                        if(flowReward!=null){
                            BigDecimal lockamount = new BigDecimal(flowReward.get("lockamount").toString());
                            BigDecimal playment = new BigDecimal(flowReward.get("playment").toString());
                            TransferMiner tm = tm5Map.get(address.toLowerCase()+"_"+bNumber);
                            tm5Map.remove(address.toLowerCase()+"_"+bNumber);
                            if(null==tm){
                                if(bNumber>maxDayFlowBlockNumber){
                                    flowBlockSet.add(bNumber);
                                }
                                TransferMiner transferMiner= new TransferMiner();
                                transferMiner.setTxHash(UUID.randomUUID().toString());
                                transferMiner.setAddress(address);
                                transferMiner.setBlockNumber(bNumber);
                                transferMiner.setTotalAmount(lockamount);
                                transferMiner.setType(5);
                                transferMiner.setReleaseHeigth(Long.parseLong(flowReward.get("releaseperiod").toString()));
                                transferMiner.setReleaseInterval(Long.parseLong(flowReward.get("releaseinterval").toString()));
                                transferMiner.setLockNumHeight(Long.parseLong(flowReward.get("lockperiod").toString()));
                                transferMiner.setRevenueaddress(flowReward.get("revenueaddress").toString());
                                transferMiner.setUnLockNumber(transferMiner.getBlockNumber()+transferMiner.getLockNumHeight());
                                transferMiner.setReleaseamount(playment);
                                tfInsert.add(transferMiner);
                                if(playment.compareTo(BigDecimal.ZERO)!=0){
                                    TransferMiner transRelease= new TransferMiner();
                                    transRelease.setTxHash(UUID.randomUUID().toString());
                                    transRelease.setRevenueaddress(flowReward.get("revenueaddress").toString());
                                    transRelease.setAddress(address);
                                    transRelease.setBlockNumber(bNumber);
                                    transRelease.setType(6);
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
                                    transferMiner.setRevenueaddress(flowReward.get("revenueaddress").toString());
                                    transferMiner.setAddress(address);
                                    transferMiner.setBlockNumber(bNumber);
                                    transferMiner.setType(6);
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
                            if(tfUpdate.size()>0&&tfUpdate.size()/Constants.BATCHCOUNT>=1){
                                transferMinerMapper.updateMinerTransfRelaseBatch(tfUpdate);
                                tfUpdate = new ArrayList<>();
                            }
                            if(tfInsert.size()>0&&tfInsert.size()/Constants.BATCHCOUNT>=1){
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
        new DayFlowDataJob(blockNumber,transferMinerMapper,yesterday,timeStamp,web3j,flowBlockSet,nfcFlowMinerMapper).start();
        long flowlockReleaseEndtime = System.currentTimeMillis();
        logger.info("blocknumber="+blockNumber+" flowlockRelease end ,spends total second ="+(flowlockReleaseEndtime-flowlockReleaseStarttime)/1000);

    }

    private void bandwidthlock(AlienSnapshot.Snapshot bandwidthlock) {
        List<NfcCltFlwdata> cltList = new ArrayList<>();
        List<TransferMiner> tfUpdate = new ArrayList<>();
        List<TransferMiner> tfInsert = new ArrayList<>();
        long bandwidthlockReleaseStarttime = System.currentTimeMillis();
        logger.info("bandwidthlockReleaseStarttime start .....");
        Map<String, Map<String, Map<String, Object>>> flowrevenve= bandwidthlock.getFlowrevenve();
        if(null!=flowrevenve&&flowrevenve.size()>0) {
            Map<String,Addresses> addressMap = new HashMap<>();
            logger.info("blocknumber=" + blockNumber + " bandwidthlock=" + flowrevenve.size());
            Map<String,Long> maxBlockMap = new HashMap<>();
            List<TransferMiner> maxBlockNumberList = transferMinerMapper.getMaxBlockNumberTransferGroupByAddress();
            for (TransferMiner vo:maxBlockNumberList) {
                maxBlockMap.put(vo.getAddress().toLowerCase(),vo.getBlockNumber());
            }
            TransferMiner tf = new TransferMiner();
            tf.setType(9);
            List<TransferMiner> tm9 = transferMinerMapper.getTransferMinerList(tf);
            Map<String,TransferMiner> tm9Map = new HashMap<>();
            for (TransferMiner vo:tm9) {
                tm9Map.put(vo.getAddress().toLowerCase()+'_'+vo.getBlockNumber(),vo);
            }
            for(Map.Entry<String, Map<String, Map<String, Object>>> record:flowrevenve.entrySet()){
                String address = record.getKey();
                Map<String, Map<String, Object>> rMap = record.getValue();
                Map<String, Object> lockbalance = rMap.get("lockbalance");
                //flowLock and bandwidthrewardlock maxblockNumber
                Long maxBlockNumber = maxBlockMap.get(address.toLowerCase())==null?0L:maxBlockMap.get(address.toLowerCase());
                maxBlockMap.remove(address.toLowerCase());
                //day  bandwidthreward
                NfcCltFlwdata clt = new NfcCltFlwdata(address);
                clt.setBlocknumber(blockNumber);
                clt.setBandwidthreward(BigDecimal.ZERO);
                clt.setReport_time(yesterday);
                clt.setInstime(new Date());
                if (null != lockbalance && lockbalance.size() > 0) {
                    for (Map.Entry<String, Object> lockB: lockbalance.entrySet()) {
                        Map<String, Object> lock = (Map<String, Object>)lockB.getValue();
                        Long bNumber = Long.parseLong(lockB.getKey());
                        Map<String,Map<String, Object>> lockV = (Map<String,  Map<String, Object>>)lockB.getValue();
                        Map<String, Object> bandwidthReward = lockV.get(bandwidthRewardType);
                        if(bandwidthReward!=null){
                            BigDecimal lockamount = new BigDecimal(bandwidthReward.get("lockamount").toString());
                            BigDecimal playment = new BigDecimal(bandwidthReward.get("playment").toString());
                            if(bNumber>maxBlockNumber){
                                clt.setBandwidthreward(TxDataParse.add(clt.getBandwidthreward(),lockamount));
                            }
                            TransferMiner tm = tm9Map.get(address.toLowerCase()+"_"+bNumber);
                            tm9Map.remove(address.toLowerCase()+"_"+bNumber);
                            if(null==tm){
                                TransferMiner transferMiner= new TransferMiner();
                                transferMiner.setTxHash(UUID.randomUUID().toString());
                                transferMiner.setAddress(address);
                                transferMiner.setBlockNumber(bNumber);
                                transferMiner.setTotalAmount(lockamount);
                                transferMiner.setType(9);
                                transferMiner.setReleaseHeigth(Long.parseLong(bandwidthReward.get("releaseperiod").toString()));
                                transferMiner.setReleaseInterval(Long.parseLong(bandwidthReward.get("releaseinterval").toString()));
                                transferMiner.setLockNumHeight(Long.parseLong(bandwidthReward.get("lockperiod").toString()));
                                transferMiner.setRevenueaddress(bandwidthReward.get("revenueaddress").toString());
                                transferMiner.setUnLockNumber(transferMiner.getBlockNumber()+transferMiner.getLockNumHeight());
                                transferMiner.setReleaseamount(playment);
                                tfInsert.add(transferMiner);
                                if(playment.compareTo(BigDecimal.ZERO)!=0){
                                    TransferMiner transRelease= new TransferMiner();
                                    transRelease.setTxHash(UUID.randomUUID().toString());
                                    transRelease.setRevenueaddress(bandwidthReward.get("revenueaddress").toString());
                                    transRelease.setAddress(address);
                                    transRelease.setBlockNumber(bNumber);
                                    transRelease.setType(10);
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
                                    transferMiner.setRevenueaddress(bandwidthReward.get("revenueaddress").toString());
                                    transferMiner.setAddress(address);
                                    transferMiner.setBlockNumber(bNumber);
                                    transferMiner.setType(10);
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
                            if(tfUpdate.size()>0&&tfUpdate.size()/Constants.BATCHCOUNT>=1){
                                transferMinerMapper.updateMinerTransfRelaseBatch(tfUpdate);
                                tfUpdate = new ArrayList<>();
                            }
                            if(tfInsert.size()>0&&tfInsert.size()/Constants.BATCHCOUNT>=1){
                                transferMinerMapper.insertBatch(tfInsert);
                                tfInsert = new ArrayList<>();
                            }
                        }
                    }
                }
                //day bandwidthreward
                if(clt.getBandwidthreward().compareTo(BigDecimal.ZERO)!=0){
                    clt.setProfitamount(clt.getBandwidthreward());
                    clt.setFwflag(1);
                    cltList.add(clt);
                }
                if(cltList.size()>0&&cltList.size()/Constants.BATCHCOUNT>=1){
                    nfcFlowMinerMapper.batchSaveFlwDataDay(cltList);
                    cltList = new ArrayList<>();
                }
            }
            if(cltList.size()>0){
                nfcFlowMinerMapper.batchSaveFlwDataDay(cltList);
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
        long bandwidthlockReleaseEndtime = System.currentTimeMillis();
        logger.info("blocknumber="+blockNumber+" bandwidthlockRelease end ,spends total second ="+(bandwidthlockReleaseEndtime-bandwidthlockReleaseStarttime)/1000);
    }
}
