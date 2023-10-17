package com.imooc.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imooc.mapper.*;
import com.imooc.pojo.*;
import com.imooc.pojo.Form.BlockQueryForm;
import com.imooc.pojo.Form.DposVoterQueryForm;
import com.imooc.pojo.vo.*;
import com.imooc.service.BlockService;
import com.imooc.service.util.TxDataParse;
import com.imooc.utils.*;
//import jnr.ffi.annotations.In;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthBlockNumber;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BlockServiceImpl implements BlockService {

    @Autowired
    private BlockMapper blockMapper;

    @Autowired
    private TransactionMapper transactionMapper;


    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private TransferMinerMapper transferMinerMapper;

    @Autowired
    private ApiConfigMapper apiConfigMapper;

    @Autowired
    private DposVotesMapper dposVotesMapper;
    @Autowired
    private OverViewMapper  overMapper;
    @Value("${ipaddress}")
    private String ipaddress;

    @Value("${FROM_ADDRESS_HASH}")
    private String fromaddressHash;

    @Value("${requestUrl}")
    private String requestUrl;

    @Value("${accessid}")
    private String accessid;

    @Value("${accessjson}")
    private String accessjson;

    @Value("${key}")
    private String key;

    @Value("${contractaddress1}")
    private String contractaddress1;

    @Value("${contractaddress2}")
    private String contractaddress2;

    @Value("${contractaddress3}")
    private String contractaddress3;

    @Value("${addresspool}")
    private String addresspool;

    @Value("${mineraddress1}")
    private String mineraddress1;

    @Value("${mineraddress2}")
    private String mineraddress2;

    @Value("${mineraddress3}")
    private String mineraddress3;

    @Value("${blocktotal}")
    private String blocktotal;

    @Value("${year}")
    private String year;

    @Value("${allrewards}")
    private String allrewards;

    @Value("${round}")
    private long round;
    @Value("${epoch}")
    private long epoch;
    private static final String userPrikey = "pppp";
    @Value("${yeartotal}")
    private   Long yeartotal;//The total number of seconds in a year
    @Value("${perblocksecond}")
    private   int perblocksecond;//How many seconds is a block
    @Value("${blockTotalNfc}")
    private   Long blockTotalNfc;//Traffic mining rewards
    @Value("${supplyTotalNfc}")
    private   Long supplyTotalNfc;//supplyTotalNfc

    @Value("${dayOneNumber}")
    private   Long dayOneNumber;
    
    private static final Logger logger = LoggerFactory.getLogger(BlockServiceImpl.class);

    @Override
    public void save(Blocks block) {

    }

    public    ResultMap getBlockNumber(){
        Map<String, Object> map = new HashMap<String, Object>();
        long totalBlockNumber = blockMapper.getLeatestBlockNumber();
        map.put("totalBlockNumber", totalBlockNumber);//
        return ResultMap.getSuccessfulResult(map);
    }

    @Override
    public ResultMap getDatas() {
        Web3j web3j = Web3jUtils.getWeb3j(ipaddress);
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            Request<?, EthBlockNumber> request = web3j.ethBlockNumber();
            long totalBlockNumber = blockMapper.getLeatestBlockNumber();
            String fromHash = fromaddressHash.trim();
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("txType","0");
            long transactionCount = transactionMapper.getTotalTransactionCount(paramsMap);
           // BigInteger balance1 = web3j.ethGetBalance(contractaddress1, DefaultBlockParameterName.LATEST).send().getBalance();//1
//            BigInteger balance2 = web3j.ethGetBalance(contractaddress2, DefaultBlockParameterName.LATEST).send().getBalance();//2
//            BigInteger balance3 = web3j.ethGetBalance(contractaddress3, DefaultBlockParameterName.LATEST).send().getBalance();//3
//            BigInteger addresspools = web3j.ethGetBalance(addresspool, DefaultBlockParameterName.LATEST).send().getBalance();//
//            String addresspool = addresspools.toString();
            long nowNumber = web3j.ethBlockNumber().send().getBlockNumber().longValue();
            long avgTime=20L;
            //long avgTime= web3j.alientSnapshotByNumber((int)nowNumber).send().getSnapshot().getPeriod().longValue();
//     //*
//            map.put("minerRewardTotal", getSendReward());//
//            map.put("TotalPledgeQuantity", balance2);//
//            map.put("TotalLockUpQuantity", balance3);//0
//            map.put("addresspools", addresspool);//
            map.put("totalBlockNumber", totalBlockNumber);//
            map.put("destrNum", getAddr0Balance());//
            long blockYesetoday = totalBlockNumber - dayOneNumber;    
            BlockOverView  viewing=getOverView(blockYesetoday);
            map.put("bandWidthSize", viewing.getBandwidth());//
            map.put("totalFlow", viewing.getTotalflow());// MB
            map.put("pledgeNum", viewing.getPladgenum());//  + MB
            map.put("lockNum", viewing.getLocknum());//   MB
            map.put("nextElectTime", calNextElectTime(totalBlockNumber));//
            map.put("nfc24", viewing.getNfc24());//24
            map.put("nfcToGb",viewing.getNfctogb()==null?"0":viewing.getNfctogb().doubleValue());//
//            map.put("transactionCount", transactionCount);//
//            map.put("blockTime", avgTime);//
//            map.put("TotalCirculationQuantity", getAllAddressBalance());
//            map.put("yesterNewAccount", getNewAccount());
//            map.put("yesterTransactionValue", getYesterDayValue());
//            map.put("totalTransactionValue", getTotalValue());
//            map.put("voterTotal",getVotesInfo());
//            map.put("voterSize",getRoundSize());
//            map.put("desnfc",getAddr0Balance());

            map.put("blockRewardTotal", getMinerNumber(totalBlockNumber).multiply(new BigDecimal(Constants.decimals)));
            map.put("supplyTotalNfc", supplyTotalNfc);
            map.put("blockTime", perblocksecond);
            List<AddressVo> accountList = accountMapper.getAllAddress();
            BigDecimal circulatingSupply = BigDecimal.ZERO;
            for (AddressVo vo: accountList) {
                circulatingSupply = TxDataParse.add(circulatingSupply,vo.getBalance());
            }
            map.put("totalAcount", accountList.size());
            map.put("transaction24h", transactionMapper.getTotalTxValue24H());
            map.put("circulatingSupply", circulatingSupply);
            map.put("circulatingRate", circulatingSupply.divide(new BigDecimal(Constants.decimals),4,BigDecimal.ROUND_HALF_UP).divide(new BigDecimal(supplyTotalNfc),4,BigDecimal.ROUND_HALF_UP));
            return ResultMap.getSuccessfulResult(map);
        } catch (Exception e) {
            logger.error("error",e);
        }
        return ResultMap.getFailureResult("");
    }

    private  BigDecimal getMinerNumber(Long nowNumber){
        try {
            long blocknum = yeartotal/perblocksecond;
            long blockreward=blockTotalNfc;
            int n = (int) (nowNumber/blocknum + (nowNumber%blocknum==0?0:1));
            BigDecimal  tbn=new BigDecimal(blockreward*(1-Math.pow(0.5,n/6.0)));
            BigDecimal  tbn_1=new BigDecimal(blockreward*(1-Math.pow(0.5,(n-1)/6.0)));
            BigDecimal  total=tbn.subtract(tbn_1).divide(new BigDecimal(blocknum),20,BigDecimal.ROUND_HALF_UP);
            return total;
        } catch (Exception e) {
            e.printStackTrace();
            return new BigDecimal("0");
        }
    }
    private BlockOverView  getOverView(long blockYesetoday){
        BlockOverView  info=null;
        try{
            info= overMapper.getFwMinerData();//、
            BlockOverView  nodeinfo=  overMapper.getNodeMinerData();//
            if(nodeinfo!=null){
                info.setPladgenum(nodeinfo.getNodepledgenum().add(info.getFwpledgenum()));
            }
            BlockOverView  lockinfo=  overMapper.getCurrLockData();//
            if(lockinfo!=null){
                info.setLocknum(lockinfo.getLocknum());
            }
            BlockOverView  dfninfo=overMapper.getnfctogbData();
            if(dfninfo!=null){
                info.setNfctogb(dfninfo.getNfctogb());
                info.setTotalflow(dfninfo.getTotalflow());
            }
            BlockOverView  nfc24=overMapper.getLastMintage(blockYesetoday);
            if(nfc24!=null){
            	info.setNfc24(nfc24.getTotalnfc());            	
            }
             return  info;
        }catch (Exception e){
            e.printStackTrace();
               logger.error("get overview error",e);
        }
        return new BlockOverView();

    }


    private String calNextElectTime(long totalBlockNumber){
        StringBuffer selectTime = new StringBuffer();
        try {
            if(totalBlockNumber<=epoch){
                selectTime.append(epoch);
            }else{
                selectTime.append((totalBlockNumber+(epoch-totalBlockNumber%epoch)));
            }
//
//            long day = between / (24 * 3600);
//            long hour = between % (24 * 3600) / 3600;
//            long minute = between % 3600 / 60;
//            long second = between % 60;
//
//            if (day > 0) {
//                selectTime.append(day + " days ");
//                selectTime.append(hour + " hours ");
//                selectTime.append(minute + " minutes ");
//                if (second > 0) {
//                    selectTime.append(second + " seconds");
//                }
//            } else {
//                if (hour > 0) {
//                    selectTime.append(hour + " hours ");
//                    selectTime.append(minute + " minutes ");
//                    if (second > 0) {
//                        selectTime.append(second + " seconds");
//                    }
//                } else {
//                    if (minute > 0) {
//                        selectTime.append(minute + " minutes ");
//                        if (second > 0) {
//                            selectTime.append(second + " seconds");
//                        }
//                    } else {
//                        selectTime.append(second + " seconds");
//                    }
//
//                }
//            }
            return selectTime.toString();
        }catch (Exception e){
            logger.error("Error calculating election time",e);
        }
        return selectTime.toString();
    }
    private  Date getNextWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getThisWeekMonday(date));
        cal.add(Calendar.DATE, 7);
        return cal.getTime();
    }
    private  Date getThisWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // ，
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // ，
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        return cal.getTime();
    }

    private BigDecimal getAddr0Balance() {    	
        BigDecimal bal0 =transactionMapper.getAddr0Balance(Constants.addressZero);
        if (null == bal0) {
            bal0 = new BigDecimal("0");
        }
        return bal0;
    }

    //
    public BigDecimal getTotalValue() {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        BigDecimal totalValue = transactionMapper.getTotalValues(paramMap);
        return totalValue;
    }

    //
    public BigDecimal getYesterDayValue() {
        String startTime = DateUtil.getFormatDateTime(DateUtil.getBeginDayOfYesterday(), "yyyy-MM-dd HH:mm:ss");
        String endTime = DateUtil.getFormatDateTime(DateUtil.getEndDayOfYesterDay(), "yyyy-MM-dd HH:mm:ss");
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        BigDecimal yesterTotal = transactionMapper.getTotalValues(paramMap);
        return yesterTotal;
    }

    //
    public Long getNewAccount() {
        String startTime = DateUtil.getFormatDateTime(DateUtil.getBeginDayOfYesterday(), "yyyy-MM-dd HH:mm:ss");
        String endTime = DateUtil.getFormatDateTime(DateUtil.getEndDayOfYesterDay(), "yyyy-MM-dd HH:mm:ss");
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        Long count = accountMapper.getNewAddress(paramMap);
        return count;
    }

    //
    public BigDecimal getSendReward() {
        Integer[] type = {5};
        BigDecimal reard = transferMinerMapper.getReward(type);
        if (null == reard) {
            reard = new BigDecimal("0.0");
        }
        return reard;
    }

    @Override
    public ResultMap<Page<BlocksVo>> pageList(BlockQueryForm blockQueryForm) {
        BigDecimal b = new BigDecimal("0");
        Page page = blockQueryForm.newFormPage();
        Long pageSize = page.getSize();
        Long pageCurrent = page.getCurrent();
        long total = blockMapper.getTotalCount(blockQueryForm.getAddress());
        long startNumber;
        if (pageCurrent == 1) {
            startNumber = total - pageSize;
        } else {
            startNumber = total - (pageCurrent * pageSize);
        }
        pageCurrent = (pageCurrent - 1) * pageSize;
        List<BlocksVo> list = blockMapper.pageList(pageCurrent, pageSize, startNumber,blockQueryForm.getAddress());
        if (list.size() == 0) {
            return ResultMap.getSuccessfulResult(null);
        }
        if (list.size() > 0 || list != null) {
            for (BlocksVo bkk : list) {
                long number = bkk.getBlockNumber();//00
                String fromHash = fromaddressHash.trim();
                long count = transactionMapper.getTotalByBlockNumber(number, fromHash);
                if (count == 0) {
                    bkk.setAvgGasPrice(0L);
                } else {
                    long acgGasPrice = transactionMapper.getAvgPrice(number);
                    bkk.setAvgGasPrice(acgGasPrice);
                }
                if (bkk.getReward() == null) {
                    bkk.setReward(b);
                }
            }
            page.setRecords(list);
            page.setTotal(total);
            return ResultMap.getSuccessfulResult(page);
        } else {
            return ResultMap.getSuccessfulResult(null);
        }
    }

    @Override
    public ResultMap getBlockInfoByBlockNumber(Long blockNumber) {
        List<BlocksVo> listInfo = blockMapper.getBlockInfoByNumber(blockNumber);
        if (listInfo.size() == 0) {
            return ResultMap.getSuccessfulResult(null);
        }
        //
        for (BlocksVo bkk : listInfo) {
            long number = bkk.getBlockNumber();//00
            if (bkk.getBlockTransactionCount() == null) {
                bkk.setBlockTransactionCount(0);
            } else {
                bkk.setBlockTransactionCount(bkk.getTxsCount());
            }
            String fromHash = fromaddressHash.trim();
            long count = transactionMapper.getTotalByBlockNumber(number, fromHash);
            if (count == 0) {
                bkk.setAvgGasPrice(0L);
            } else {
                long acgGasPrice = transactionMapper.getAvgPrice(number);
                bkk.setAvgGasPrice(acgGasPrice);
            }
            BlockQueryForm blockQueryForm = new BlockQueryForm();
            blockQueryForm.setType(1);
            blockQueryForm.setBlockNumber(blockNumber);
            TransferMinerVo tm = transferMinerMapper.getMinerSum(blockQueryForm);
            if(tm!=null){
                bkk.setLockamount(tm.getTotalAmount());
                bkk.setReleaseamount(tm.getReleaseamount());
            }
        }
        return ResultMap.getSuccessfulResult(listInfo);
    }

    @Override
    public ResultMap getDataForNfc() {
        Map<String, Object> map = new HashMap<String, Object>();
        String flow = getAllBill();//
        String nfc = getAllFlowNfc();//
        String miner = getAllFlowMiner();//
        map.put("ActiveMiner", miner);//
        map.put("ComputingPower", nfc);//
        map.put("TotalNetWorkTraffic", flow);//
        return ResultMap.getSuccessfulResult(map);
    }


    /**/
    public String getAllBill() {
//        long timeStamp = System.currentTimeMillis();
//        String url = requestUrl + "accessid" + "=" + accessid + "&" + "random" + "=" + timeStamp;
//        String md5Val = MD5.encodeByMD5(accessjson + timeStamp);
//        String hash = HmacsHa256.sha256_HMAC(md5Val, key);
//        JSONObject json = new JSONObject();
//        json.put("type", "flow");
//        json.put("sig", hash);
//        String param = json.toString();
//        String result = HttpUtils.HttpPostWithJson(url, param);
//        if (result.equals("0.0")) {  //
//            return "0.0";
//        }
//        JSONObject jsons = JSONObject.fromObject(result);
//        String value;
//        String unit;
//        if (jsons.getString("result").equals("0")) {
//            String data = jsons.getString("data");
//            JSONObject json1 = JSONObject.fromObject(data);
//            value = json1.getString("billflow");
//            unit = json1.getString("unit");
//            value = value + " ";
//            value = value + unit;
//            return value;
//        } else {
            return "0.0";
//        }

    }

    /**/
    public String getAllFlowNfc() {
//        long timeStamp = System.currentTimeMillis();
//        String url = requestUrl + "accessid" + "=" + accessid + "&" + "random" + "=" + timeStamp;
//        String md5Val = MD5.encodeByMD5(accessjson + timeStamp);
//        String hash = HmacsHa256.sha256_HMAC(md5Val, key);
//        JSONObject json = new JSONObject();
//        json.put("type", "flow-nfc");
//        json.put("sig", hash);
//        String param = json.toString();
//        String result = HttpUtils.HttpPostWithJson(url, param);
//        if (result.equals("0.0")) {  //
//            return "0.0";
//        }
//        JSONObject jsons = JSONObject.fromObject(result);
//        String value;
//        if (jsons.getString("result").equals("0")) {
//            String data = jsons.getString("data");
//            JSONObject json1 = JSONObject.fromObject(data);
//            value = json1.getString("flow-nfc");
//            return value;
//        } else {
            return "0.0";
//        }

    }


    /**/
    public String getAllFlowMiner() {
//        long timeStamp = System.currentTimeMillis();
//        String url = requestUrl + "accessid" + "=" + accessid + "&" + "random" + "=" + timeStamp;
//        String md5Val = MD5.encodeByMD5(accessjson + timeStamp);
//        String hash = HmacsHa256.sha256_HMAC(md5Val, key);
//        JSONObject json = new JSONObject();
//        json.put("type", "miner");
//        json.put("sig", hash);
//        String param = json.toString();
//        String result = HttpUtils.HttpPostWithJson(url, param);
//        if (result.equals("0.0")) {  //
//            return "0.0";
//        }
//        JSONObject jsons = JSONObject.fromObject(result);
//        String value;
//        if (jsons.getString("result").equals("0")) {
//            String data = jsons.getString("data");
//            JSONObject json1 = JSONObject.fromObject(data);
//            value = json1.getString("minernum");
//            return value;
//        } else {
            return "0.0";
//        }

    }

    /* */
    public BigDecimal getRewardByYear(Double nFee, Long nowStamp, BigDecimal bigNumber) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //
        String dateTime = format.format(nowStamp);
        if (nFee <= 1) {//
            BigDecimal rewardFirst = getRewards(nFee);
            BigDecimal yearBlockTotal = new BigDecimal(blocktotal);
            BigDecimal reward = rewardFirst.divide(yearBlockTotal, 20, BigDecimal.ROUND_HALF_UP);
            reward=reward.multiply(bigNumber);
            return reward;
        } else {//，n
            BigDecimal rewardYear = getRewards(nFee);
            Double fee = nFee - 1;
            BigDecimal lastYear = getRewards(fee);
            BigDecimal yearReward = rewardYear.subtract(lastYear);
            BigDecimal yearBlockTotal = new BigDecimal(blocktotal);
            BigDecimal reward = yearReward.divide(yearBlockTotal, 20, BigDecimal.ROUND_HALF_UP);
            //
            Long totals=Long.valueOf(blocktotal);//
            Long l =new Double(fee).longValue();//
            Long count=l*totals; //
            Long nowNumber= bigNumber.longValue();//
            Long left=nowNumber-count;//n*/
            BigDecimal a= new BigDecimal(left);
            reward= reward.multiply(a);
            return reward;
        }
    }


    /*Tf*/
    public BigDecimal getRewards(Double nFee) {
        double allReward = Double.parseDouble(allrewards);//
        int years = Integer.valueOf(year).intValue();
        double bb = (double) nFee / years;//
        double hh = 1 - (Math.pow(0.5, bb));//
        double cc = allReward * hh;
        BigDecimal total = BigDecimal.valueOf(cc);
        return total;
    }

    /**/
    public BigDecimal getAllAddressBalance() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("contractaddress1", contractaddress1);
        map.put("contractaddress2", contractaddress2);
        map.put("contractaddress3", contractaddress3);
        map.put("mineraddress1", mineraddress1);
        map.put("mineraddress2", mineraddress2);
        /*     map.put("mineraddress3",mineraddress3);*/
        map.put("addresspool", addresspool);
        List<Addresses> listAll = accountMapper.getAllAddressFoBalance(map);
        BigDecimal value = new BigDecimal("0.0");
        if (listAll.size() == 0) {
            value = new BigDecimal("0.0");
        } else {
            for (Addresses account : listAll) {
                BigDecimal al = account.getBalance();
                value = value.add(al);
            }
        }
        return value;
    }

    public ResultMap getAllParamters(long types,String language) {
        List<ApiConfigView> list = apiConfigMapper.getAllConfig(types);
        if(language.equals("zh")||language=="zh"){
            list= apiConfigMapper.getAllConfig(types);
        }else{
            list= apiConfigMapper.getAllConfigForEn(types);
        }
        return ResultMap.getSuccessfulResult(list);
    }

    @Override
    //nfc
    public ResultMap<Page<BlockAndMinerVo>> getMinerAndFeeNfc(BlockQueryForm blockQueryForm) {
        Page page = blockQueryForm.newFormPage();
        Long pageSize = page.getSize();
        Long pageCurrent = page.getCurrent();
        pageCurrent=(pageCurrent-1)*pageSize;
        List<BlockAndMinerVo> listInfo = transferMinerMapper.getBlockAndMinerInfo(pageCurrent, pageSize);
        long total = transferMinerMapper.getTotalMinerInfo();
        page.setRecords(listInfo);
        page.setTotal(total);
        return ResultMap.getSuccessfulResult(page);
    }



    public BigDecimal getVotesInfo()throws Exception {
        try {
          /*  Object [] values=null;
            Web3j web3j = Web3jUtils.getWeb3j(ipaddress);
            int blockNumber = web3j.ethBlockNumber().send().getBlockNumber().intValue();//
            AlienSnapshot.Snapshot snap=web3j.alientSnapshotByNumber(blockNumber).send().getSnapshot();//
            Map<String, Object[]> map = snap.getVotes();//
            BigDecimal totalStake= new BigDecimal("0");
            if(map.size()>0 &&map!=null){
                for(String key:map.keySet()){
                    values= map.get(key);
                    Integer rod=getRound(new Long(blockNumber).intValue());
                    for(int j=0;j<values.length;j++){
                        JSONObject jsonObject=JSONObject.fromObject(values[j].toString());
                        BigDecimal stake =new BigDecimal(jsonObject.get("Stake").toString());
                        totalStake=totalStake.add(stake); //
                    }
                }
            }
            return totalStake;*/
            return  null;
        } catch (Exception e) {
            return new BigDecimal("0.0");
        }
    }

    public long getVotesSize()throws Exception {
        try {
            Map<String,Object> returnMap= new HashMap<String,Object>();
            Web3j web3j = Web3jUtils.getWeb3j(ipaddress);
            int blockNumber = web3j.ethBlockNumber().send().getBlockNumber().intValue();
            int round=getRound(blockNumber);//
            DposVoterVo dposVoterVo=dposVotesMapper.getVoterInfo(round);
            long votersize=dposVoterVo.getVoterCount();
            returnMap.put("votersize",dposVoterVo.getVoterCount());
            return votersize;
        } catch (Exception e) {
            return 0L;
        }
    }

    //
    public int getRoundSize()throws Exception {
       /* try {
            int voterSize=0;
            Object [] values=null;
            Web3j web3j = Web3jUtils.getWeb3j(ipaddress);
            int blockNumber = web3j.ethBlockNumber().send().getBlockNumber().intValue();//
            AlienSnapshot.Snapshot snap=web3j.alientSnapshotByNumber(blockNumber).send().getSnapshot();//
            Map<String, Object[]> map = snap.getVotes();//
            if(map.size()>0 &&map!=null){
                voterSize=map.size();
            }
            return voterSize;
        } catch (Exception e) {
            return 0;
        }*/
        return 0;
    }
    //
    private int getRound(int blockNumber){
        int rouds=0;
        if(blockNumber<=round){
           rouds=1;
           return rouds;
       }else{
            float cell=blockNumber/round;
            double nYear=(double)blockNumber/round;
            double nFee= Math.ceil(nYear);
            int round = new Double(nFee).intValue();
            return round;
       }
    }

    @Override
    public ResultMap<Page<Blocks>> getRewardVoterPageList(DposVoterQueryForm dposVoterQueryForm) {
        List<BlockRewardVo> listInfo = blockMapper.getAllRewardForaddress(dposVoterQueryForm);
        if(listInfo.size()>0 ||!listInfo.isEmpty()){
            return ResultMap.getSuccessfulResult(listInfo);
        }else{
            return ResultMap.getSuccessfulResult(null);
        }
    }

    /**
     * Obtain the pledge quantity per 1Mbps of the current flow mining pledge 4 intervals
     * @return
     */
    @Override
    public ResultMap getBwPlgeRange() {
        Map<String, Object> map = new HashMap<String, Object>();
        try{
            map.put("bw0_300",overMapper.getBwPdgRnge300()) ;
            map.put("bw301_800",overMapper.getBwPdgRnge380()) ;
            map.put("bw801_1500",overMapper.getBwPdgRnge815()) ;
            map.put("bw1500",overMapper.getBwPdgRnge1500()) ;
            return ResultMap.getSuccessfulResult(map);
        }catch (Exception e){
            logger.error("Obtain the pledge quantity per 1Mbps of the current flow mining pledge 4 intervals error ",e);
        }

        return ResultMap.getFailureResult("");
    }




}