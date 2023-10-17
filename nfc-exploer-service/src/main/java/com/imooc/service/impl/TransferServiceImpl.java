package com.imooc.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imooc.mapper.NfcFlowMinerMapper;
import com.imooc.mapper.NodeExitMapper;
import com.imooc.mapper.TransferMinerMapper;
import com.imooc.pojo.*;
import com.imooc.pojo.Form.*;

import com.imooc.pojo.vo.TransferMinerVo;
import com.imooc.service.TransferMinerService;
import com.imooc.utils.HttpUtils;
import com.imooc.utils.ResultMap;
import com.imooc.utils.Web3jUtils;

//import jnr.ffi.annotations.In;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.AlienSnapshot;
import org.web3j.utils.Numeric;

@Service
public class TransferServiceImpl implements TransferMinerService {
    @Autowired
    private TransferMinerMapper transferMinerMapper;

    @Autowired
    private NodeExitMapper nodeExitMapper;
    @Autowired
    private NfcFlowMinerMapper nfcFlowMinerMapper;

    private Logger logger = LoggerFactory.getLogger(TransferServiceImpl.class);

    @Value("${ipaddress}")
    private String ipaddress;

    @Value("${teamlock}")
    private String teamlock;

    @Value("${flowrevenue}")
    private String flowrevenue;

    @Value("${lockFeeNumber}")
    private String lockFeeNumber;

    @Value("${dayOneNumber}")
    private String dayOneNumber;

     @Value("${profitUrl}")
    private  String profitUrl;

    public ResultMap getMinerInfoForAddress(TransferMinerForm transferMinerForm) {
        Integer[] types = transferMinerForm.getType();
        Long id = transferMinerForm.getId();
        List<TransferMiner> listInfo = transferMinerMapper.getAllMinerForInfo(id, types);
        return ResultMap.getSuccessfulResult(listInfo);
    }

    @Override
    public ResultMap getAllMinerInfoWallet(MinerQueryForm minerQueryForm) {
        Integer[] types = minerQueryForm.getType();
        String address = minerQueryForm.getAddress();
        List<TransferMiner> listInfo = transferMinerMapper.getAllMinerInfo(address, types);
        return ResultMap.getSuccessfulResult(listInfo);
    }

    @Override
    public ResultMap<Page<TransferMiner>> getLockAndPledgeInfo(LockUpTransferForm lockUpTransferForm) {
        Page page = lockUpTransferForm.newFormPage();
        Integer type = lockUpTransferForm.getType();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("type", type);
        Long pageSize=page.getSize();
        Long pageCurrent =page.getCurrent();
        pageCurrent=(pageCurrent-1)*pageSize;
        lockUpTransferForm.setCurrent(pageCurrent);
        lockUpTransferForm.setSize(pageSize);
        List<TransferMiner> listInfos = transferMinerMapper.getPledgeInfo(lockUpTransferForm);
        page.setRecords(listInfos);
        Long total=transferMinerMapper.getPledgeInfoCount(lockUpTransferForm);
        page.setTotal(total);
        return ResultMap.getSuccessfulResult(page);
    }

    public  ResultMap getNfcMinerLockSum(BlockQueryForm blockQueryForm) throws Exception{
        Map<String,Object> resultMap = new HashMap<>();
        TransferMinerVo tm = transferMinerMapper.getMinerSum(blockQueryForm);
        if(tm!=null){
            resultMap.put("lockamount",tm.getTotalAmount());
            resultMap.put("releaseamount",tm.getReleaseamount());
        }
        if(blockQueryForm.getType()==1||blockQueryForm.getType()==3){
            NfcNodeMinerQueryForm queryForm = new  NfcNodeMinerQueryForm();
            queryForm.setNode_address(blockQueryForm.getAddress());
            NfcNodeMiner pre_node = nodeExitMapper.getNode(queryForm);
            if(null!=pre_node){
                resultMap.put("minerAddress",pre_node.getNode_address());
                resultMap.put("revenueAddress",pre_node.getRevenue_address());
            }
        }else{
            NfcFlowMiner miner = nfcFlowMinerMapper.getSingleMiner(blockQueryForm.getAddress());
            if(null!=miner){
                resultMap.put("minerAddress",miner.getMiner_addr());
                resultMap.put("revenueAddress",miner.getRevenue_address());
            }
        }
        return ResultMap.getSuccessfulResult(resultMap);
    }
    @Override
    public ResultMap<Page<TransferMinerVo>> getLockNfcMinerInfo(BlockQueryForm blockQueryForm)throws Exception {
        Page page = blockQueryForm.newFormPage();
        Long pageSize=page.getSize();
        Long pageCurrent =page.getCurrent();
        pageCurrent=(pageCurrent-1)*pageSize;
        String address=blockQueryForm.getAddress();
        List<TransferMinerVo> listInfos =null;
        blockQueryForm.setCurrent(pageCurrent);
        blockQueryForm.setSize(pageSize);
        Long total=0L;
        listInfos =transferMinerMapper.getMinerInfoForNfc(blockQueryForm);
        total=transferMinerMapper.getTotalMinerForNfc(blockQueryForm);
        /*if(address==null ||address.equals("")){
            listInfos =transferMinerMapper.getMinerInfoForNfc(blockQueryForm);
            total=transferMinerMapper.getTotalMinerForNfc(blockQueryForm);
        }else{
            listInfos =transferMinerMapper.getMinerInfoNfcSerachForNfc(blockQueryForm);
            total= transferMinerMapper.getTotalMinerSerachForNfc(blockQueryForm);
        }*/
        Long dayNumber=Long.valueOf(dayOneNumber);
        Long dayNumberThree =30*dayNumber;
        Long dayNumberSix =180*dayNumber;
        Long yearNumberSix=2190*dayNumber;
        Long yearNumberOne=365*dayNumber;
       /* for(TransferMinerVo tx:listInfos){
            if(tx.getType()==1){
                Integer value=getLockParam();
                Integer num =Integer.valueOf(lockFeeNumber); //
                if(value != null && value > num){
                    value = value - num;
                }
                tx.setLockNumber(value.longValue());
                tx.setAvgBlockTime(getPeriod());
                tx.setYearNumberOne(yearNumberOne);
                tx.setYearNumberSix(yearNumberSix);
            }
            if(tx.getType()==5){
                Integer value=getMinerParam();
                tx.setLockNumber(value.longValue());
                tx.setAvgBlockTime(getPeriod());
                tx.setDayNumberThree(dayNumberThree);
                tx.setDayNumberSix(dayNumberSix);
            }
        }*/
        page.setRecords(listInfos);
        page.setTotal(total);
        return ResultMap.getSuccessfulResult(page);
    }

    @Override
    public ResultMap<Page<TransferMinerVo>> getLockNfcMinersByRevAddress(BlockQueryForm blockQueryForm)throws Exception {
        Page page = blockQueryForm.newFormPage();
        Long pageSize=page.getSize();
        Long pageCurrent =page.getCurrent();
        pageCurrent=(pageCurrent-1)*pageSize;
        blockQueryForm.setCurrent(pageCurrent);
        blockQueryForm.setSize(pageSize);
        List<TransferMinerVo> listInfos =transferMinerMapper.getLockNfcMinersByRevAddress(blockQueryForm);
        Long total=transferMinerMapper.getTotalLockNfcMinersByRevAddress(blockQueryForm);
        page.setRecords(listInfos);
        page.setTotal(total);
        return ResultMap.getSuccessfulResult(page);
    }
    public ResultMap getLockSummaryByRevAddress(BlockQueryForm blockQueryForm) throws Exception{
        Map<String,Object> resultMap = new HashMap<>();
        TransferMinerVo vo = transferMinerMapper.getLockSummaryByRevAddress(blockQueryForm);
        if(vo!=null){
            resultMap.put("totalamount",vo.getTotalAmount());
            resultMap.put("releaseamount",vo.getReleaseamount());
            resultMap.put("leftamount",vo.getLeftAmount());
        }else{
            resultMap.put("totalamount",0);
            resultMap.put("releaseamount",0);
            resultMap.put("leftamount",0);
        }
        blockQueryForm.setType(blockQueryForm.getType()+1);
        resultMap.put("releasecount",transferMinerMapper.getTotalRealease(blockQueryForm));
        return ResultMap.getSuccessfulResult(resultMap);
    }

    @Override
    public ResultMap<Page<TransferMinerVo>> getLockNfcMinerInfoForTotal(BlockQueryForm blockQueryForm)throws Exception {
        Page page = blockQueryForm.newFormPage();
        Long pageSize=page.getSize();
        Long pageCurrent =page.getCurrent();
        pageCurrent=(pageCurrent-1)*pageSize;
        String address=blockQueryForm.getAddress();
        List<TransferMinerVo> listInfos =null;
        blockQueryForm.setCurrent(pageCurrent);
        blockQueryForm.setSize(pageSize);
        Long total=0L;
        if(address==null ||address.equals("")){
            listInfos =transferMinerMapper.getMinerInfoForNfcTotal(blockQueryForm);
            total=transferMinerMapper.getTotalMinerForNfc(blockQueryForm);
        }else{
            listInfos =transferMinerMapper.getMinerInfoNfcSerachForNfc(blockQueryForm);
            total= transferMinerMapper.getTotalMinerSerachForNfc(blockQueryForm);
        }
        page.setRecords(listInfos);
        page.setTotal(total);
        return ResultMap.getSuccessfulResult(page);
    }

    @Override
    public ResultMap<Page<TransferMinerVo>> lockNfcMinerSerach(BlockQueryForm blockQueryForm) throws Exception {
        Page page = blockQueryForm.newFormPage();
        Long pageSize=page.getSize();
        Long pageCurrent =page.getCurrent();
        pageCurrent=(pageCurrent-1)*pageSize;
        String address=blockQueryForm.getAddress();
        List<TransferMinerVo> listInfos = transferMinerMapper.getMinerInfoNfcSerach(address,pageCurrent,pageSize);
        for(TransferMinerVo tx:listInfos){
            if(tx.getType()==1){
                Integer value=getLockParam();
                tx.setLockNumber(value.longValue());
                tx.setAvgBlockTime(getPeriod());
            }
            if(tx.getType()==5){
                Integer value=getMinerParam();
                tx.setLockNumber(value.longValue());
                tx.setAvgBlockTime(getPeriod());
            }
        }
        long total=transferMinerMapper.getTotalMinerSerach(address);
        page.setRecords(listInfos);
        page.setTotal(total);
        return ResultMap.getSuccessfulResult(page);
    }

    @Override
    public ResultMap<Page<TransferMiner>> getOverData(BlockQueryForm blockQueryForm) {
        Page page = blockQueryForm.newFormPage();
        Long pageSize=page.getSize();
        Long pageCurrent =page.getCurrent();
        pageCurrent=(pageCurrent-1)*pageSize;
        blockQueryForm.setCurrent(pageCurrent);
        blockQueryForm.setSize(pageSize);
        List<TransferMiner> list=transferMinerMapper.getOverData(blockQueryForm);
        if(list.size()>0 &&list !=null){
            for(TransferMiner transferMiner:list){
                String value=getProfitVal(transferMiner.getTxHash());
                transferMiner.setProfitValReward(new BigDecimal(value));//
                transferMiner.setValue(getTotalValueForTxHash(transferMiner.getTxHash()));
            }
            page.setRecords(list);
        }
        Long total=transferMinerMapper.getTotalInfo(blockQueryForm);
        page.setTotal(total);
        return ResultMap.getSuccessfulResult(page);
    }

    public BigDecimal getTotalValueForTxHash(String txHash){
         BigDecimal value = transferMinerMapper.getTotalValueForTxHash(txHash);
         return value;
    }


    public String getProfitVal(String txHash) {
        long timeStamp = System.currentTimeMillis();
        String url =profitUrl+ "random" + "=" + timeStamp;
        JSONObject json = new JSONObject();
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("drawhash",txHash);
        json.put("data", paramMap);
        String param = json.toString();
        String result = HttpUtils.HttpPostWithJson(url, param);
        if (result.equals("0.0")) {
            return "0.0";
        }
        JSONObject jsons = JSONObject.fromObject(result);
        String value;
        if (jsons.getString("result").equals("0")) {
            String data = jsons.getString("data");
            JSONObject json1 = JSONObject.fromObject(data);
            value = json1.getString("profitval");
            return value;
        } else {
            return "0.0";
        }

    }

    @Override
    public ResultMap getPledgeExit() throws Exception{
        String admin="524e3758e1eec73c9d970c1b528ddbb68852ceae";
        admin="000000000000000000000000"+admin;
        admin="0x662e5ba7"+admin;
        Web3j web3j = Web3jUtils.getWeb3j(ipaddress);
        org.web3j.protocol.core.methods.request.Transaction transaction =
                new org.web3j.protocol.core.methods.request.Transaction(null, null, null, null, "0xDA477d4763e1DC8c1672697C37C2aE4eB5415F37", null, admin);
        BigInteger number = new BigInteger("3210");
        String result=web3j.ethCall(transaction,DefaultBlockParameterName.LATEST).send().getResult();
        String rs="0x000000000000000000000000000000000000000000000000000000003B9ACA000000000000000000000000000000000000000000000000000000000005F5E10000000000000000000000000000000000000000000000000000000000055D4A800000000000000000000000000000000000000000000000000000000000000064000000000000000000000000000000000000000000000000000000000000001e0000000000000000000000000000000000000000000000000000000000000064000000000000000000000000000000000000000000000000000000000000000a";
        String r1 = rs.substring(2, 66);
        r1="0x"+r1;
        logger.info("r1"+r1);
        BigDecimal r11=convertToBigDecimal(r1);
        String r2=rs.substring(67,130);
        r2="0x"+r2;
        BigDecimal r21=convertToBigDecimal(r2);
        String r3=rs.substring(131,194);
        r3="0x"+r3;
        BigDecimal r31=convertToBigDecimal(r3);
        String r4=rs.substring(195,258);
        r4="0x"+r4;
        String r5=rs.substring(259,322);
        r5="0x"+r5;
        String r6 =rs.substring(323,386);
        r6="0x"+r6;
        String r7 =rs.substring(387,450);
        r7="0x"+r7;
        BigDecimal r41=convertToBigDecimal(r4);
        BigDecimal r51=convertToBigDecimal(r5);
        BigDecimal r61=convertToBigDecimal(r6);
        BigDecimal r71=convertToBigDecimal(r7);
        saveNodeExit(r11,r21,r31,r41,r51,r61,r71);
        return ResultMap.getSuccessfulResult(result);
    }

    private BigDecimal convertToBigDecimal(String value) {
        if (null != value)
            return new BigDecimal(Numeric.decodeQuantity(value));
        return null;
    }

    public Integer getLockParam() throws Exception {
        Web3j web3j = Web3jUtils.getWeb3j(ipaddress);
       // String tramlock=teamlock;
        org.web3j.protocol.core.methods.request.Transaction transaction = new org.web3j.protocol.core.methods.request.Transaction(null, null, null, null, teamlock, null, "0x05216523");
        String result = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send().getResult();
        String lockTime = result.substring(2, 66);
        String releseTime = result.substring(67, 130);
        Integer lock = Integer.parseInt(lockTime, 16);
        Integer relese = Integer.parseInt(releseTime, 16);
        Integer locks = lock + relese;
        return locks;
    }


    public Integer getMinerParam() throws Exception {
        Web3j web3j = Web3jUtils.getWeb3j(ipaddress);
        //String flowrevenues=flowrevenue;
        org.web3j.protocol.core.methods.request.Transaction transaction = new org.web3j.protocol.core.methods.request.Transaction(null, null, null, null, flowrevenue, null, "0x05216523");
        String result = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send().getResult();
        String lockTime = result.substring(2, 66);
        String releseTime = result.substring(67, 130);
        String apartTime = result.substring(131, 193);
        Integer lock = Integer.parseInt(lockTime, 16);
        Integer relese = Integer.parseInt(releseTime, 16);
        Integer locks = lock + relese;
        return locks;
    }

    public Long getPeriod() throws Exception {
        Web3j web3j = Web3jUtils.getWeb3j(ipaddress);
        int blockNumber = web3j.ethBlockNumber().send().getBlockNumber().intValue();
        AlienSnapshot.Snapshot snap = web3j.alientSnapshotByNumber(blockNumber).send().getSnapshot();
        Long period = snap.getPeriod().longValue();
        return period;
    }

    private void saveNodeExit(BigDecimal r11,BigDecimal r21,BigDecimal r31,BigDecimal r41,BigDecimal r51,BigDecimal r61,BigDecimal r71){
        NodeExit nodeExit = new NodeExit();
        nodeExit.setAddressName("");
        nodeExit.setTimeStamp(new Date());
        nodeExit.setPledgeAmount(r11);
        nodeExit.setDeductionAmount(r21);
        nodeExit.setTractAmount(r31);
        nodeExit.setLockStartNumber(r41.longValue());
        nodeExit.setLockNumber(r51.longValue());
        nodeExit.setReleaseNumber(r61.longValue());
        nodeExit.setReleaseInterval(r71.longValue());
        nodeExitMapper.insert(nodeExit);
    }

    @Override
    public ResultMap<Page<Transaction>> getAllTotalForReceive(String address) {
        BigDecimal totalValue= transferMinerMapper.getAllTotalForReceive(address);
        return ResultMap.getSuccessfulResult(totalValue);
    }

    @Override
    public ResultMap<Page<TransferMinerVo>> getLockNfcMinerInfoWallet(BlockQueryForm blockQueryForm) throws Exception{
        Page page = blockQueryForm.newFormPage();
        Long pageSize=page.getSize();
        Long pageCurrent =page.getCurrent();
        pageCurrent=(pageCurrent-1)*pageSize;
        String address=blockQueryForm.getAddress();
        List<TransferMinerVo> listInfos =null;
        blockQueryForm.setCurrent(pageCurrent);
        blockQueryForm.setSize(pageSize);
        Long total=0L;
        if(address==null ||address.equals("")){
           logger.info("");
        }else{
            listInfos =transferMinerMapper.getMinerInfoNfcSerachForWallet(blockQueryForm);
            total= transferMinerMapper.getTotalMinerSerachForWallet(blockQueryForm);
        }
        Long dayNumber=Long.valueOf(dayOneNumber);
        Long dayNumberThree =30*dayNumber;
        Long dayNumberSix =180*dayNumber;
        for(TransferMinerVo tx:listInfos){
            if(tx.getType()==5){
                Integer value=getMinerParam();
                tx.setLockNumber(value.longValue());
                tx.setAvgBlockTime(getPeriod());
                tx.setDayNumberThree(dayNumberThree);
                tx.setDayNumberSix(dayNumberSix);
            }
        }
        page.setRecords(listInfos);
        page.setTotal(total);
        return ResultMap.getSuccessfulResult(page);
    }


   /* public String getProfitVal(String txHash) {
        long timeStamp = System.currentTimeMillis();
        String url =profitUrl+ "random" + "=" + timeStamp;
        JSONObject json = new JSONObject();
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("drawhash",txHash);
        json.put("data", paramMap);
        String param = json.toString();
        String result = HttpUtils.HttpPostWithJson(url, param);
        if (result.equals("0.0")) {
            return "0.0";
        }
        JSONObject jsons = JSONObject.fromObject(result);
        String value;
        if (jsons.getString("result").equals("0")) {
            String data = jsons.getString("data");
            JSONObject json1 = JSONObject.fromObject(data);
            value = json1.getString("profitval");
            return value;
        } else {
            return "0.0";
        }

    }*/
}