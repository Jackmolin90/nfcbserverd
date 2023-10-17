package com.imooc.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imooc.mapper.AccountMapper;
import com.imooc.mapper.NfcFlowMinerMapper;
import com.imooc.mapper.TransferMinerMapper;
import com.imooc.pojo.Addresses;
import com.imooc.pojo.FlowReport;
import com.imooc.pojo.Form.BlockQueryForm;
import com.imooc.pojo.Form.NfcFlowMinerQueryForm;
import com.imooc.pojo.Form.NfcNetStaticsForm;
import com.imooc.pojo.NfcBandwidthConfig;
import com.imooc.pojo.NfcCltFlwdata;
import com.imooc.pojo.NfcFlowMiner;
import com.imooc.pojo.vo.*;
import com.imooc.service.NfcFlowMinerService;
import com.imooc.utils.DateUtil;
import com.imooc.utils.OrderUtils;
import com.imooc.utils.ResultMap;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@Service
public class NfcFlowMinerServiceImpl implements NfcFlowMinerService {

    @Autowired
    private NfcFlowMinerMapper nfcFlowMinerMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private TransferMinerMapper transferMinerMapper;

    public ResultMap<Page<NfcFlowMiner>> pageList(NfcFlowMinerQueryForm nfcFlowMinerQueryForm){
        Page page = nfcFlowMinerQueryForm.newFormPage();
        Long pageSize=page.getSize();
        Long pageCurrent =page.getCurrent();
        pageCurrent=(pageCurrent-1)*pageSize;
        nfcFlowMinerQueryForm.setCurrent(pageCurrent);
        nfcFlowMinerQueryForm.setSize(pageSize);
        String sortBy  = nfcFlowMinerQueryForm.getSortBy();
        if(sortBy!=null&&OrderUtils.nfcFlowMinerList.contains(sortBy)){
            nfcFlowMinerQueryForm.setSortSql(OrderUtils.sort(sortBy,nfcFlowMinerQueryForm.isDescending()));
        }
        List<NfcFlowMiner> listInfo =nfcFlowMinerMapper.getPageList(nfcFlowMinerQueryForm);
        long total=nfcFlowMinerMapper.getTotal(nfcFlowMinerQueryForm);
        page.setRecords(listInfo);
        page.setTotal(total);
        return ResultMap.getSuccessfulResult(page);
    }
    public ResultMap<Page<NfcFlowMiner>> minerRankPageList(NfcFlowMinerQueryForm nfcFlowMinerQueryForm){
        Page page = nfcFlowMinerQueryForm.newFormPage();
        Long pageSize=page.getSize();
        Long pageCurrent =page.getCurrent();
        pageCurrent=(pageCurrent-1)*pageSize;
        nfcFlowMinerQueryForm.setCurrent(pageCurrent);
        nfcFlowMinerQueryForm.setSize(pageSize);
        long total=nfcFlowMinerMapper.getMinerRankTotal(nfcFlowMinerQueryForm);
        NfcFlowMiner nf =  nfcFlowMinerMapper.getMinerSum();
        BigDecimal tBd = BigDecimal.ZERO;
        Long tFlow = 0l;
        BigDecimal tAmout = BigDecimal.ZERO;
        if(nf!=null){
            tBd = nf.getBandwidth();
            tFlow = nf.getMiner_flow();
            tAmout = nf.getRevenue_amount();
        }
        if(total>0){
            String sortBy  = nfcFlowMinerQueryForm.getSortBy();
            if(sortBy!=null&&OrderUtils.nfcFlowMinerRankList.contains(sortBy)){
                nfcFlowMinerQueryForm.setSortSql(OrderUtils.sort(sortBy,nfcFlowMinerQueryForm.isDescending()));
            }
            List<NfcFlowMiner> listInfo =nfcFlowMinerMapper.getMinerRankPageList(nfcFlowMinerQueryForm);
            for (NfcFlowMiner m: listInfo) {
                m.setTotalbandwidth(tBd);
                m.setTotalFlow(tFlow);
                m.setTotalRevenueamount(tAmout);
            }
            page.setRecords(listInfo);
            page.setTotal(total);
        }
        return ResultMap.getSuccessfulResult(page);
    }

    @Override
    public ResultMap<NfcFlowMiner> getNfcFlowMinerDetail(NfcFlowMinerQueryForm nfcFlowMinerQueryForm) {
        NfcFlowMiner miner = nfcFlowMinerMapper.getNfcFlowMinerDetail(nfcFlowMinerQueryForm);
        if(miner==null){
            miner = new NfcFlowMiner();
        }
        BlockQueryForm blockQueryForm = new BlockQueryForm();
        blockQueryForm.setAddress(nfcFlowMinerQueryForm.getMiner_addr());
        blockQueryForm.setType(5);
        TransferMinerVo flvo = transferMinerMapper.getLockSummaryByRevAddress(blockQueryForm);
        if(flvo!=null){
            miner.setFlwtotalamount(flvo.getTotalAmount());
            miner.setFlwreleaseamount(flvo.getReleaseamount());
        }
        blockQueryForm.setType(9);
        TransferMinerVo bdvo = transferMinerMapper.getLockSummaryByRevAddress(blockQueryForm);
        if(bdvo!=null){
            miner.setBdtotalamount(bdvo.getTotalAmount());
            miner.setBdreleaseamount(bdvo.getReleaseamount());
        }
        return ResultMap.getSuccessfulResult(miner);
    }

    @Override
    public ResultMap<Page<NfcFlowMinerDayVo>> getMinerDayStatislist(NfcFlowMinerQueryForm nfcFlowMinerQueryForm) {
        Page page = nfcFlowMinerQueryForm.newFormPage();
        Long pageSize=page.getSize();
        Long pageCurrent =page.getCurrent();
        pageCurrent=(pageCurrent-1)*pageSize;
        nfcFlowMinerQueryForm.setCurrent(pageCurrent);
        nfcFlowMinerQueryForm.setSize(pageSize);
        List<NfcFlowMinerDayVo> listInfo =nfcFlowMinerMapper.getMinerDayStatislist(nfcFlowMinerQueryForm);
        long total=nfcFlowMinerMapper.getMinerDayStatisCount(nfcFlowMinerQueryForm);
        page.setRecords(listInfo);
        page.setTotal(total);
        return ResultMap.getSuccessfulResult(page);
    }

    public ResultMap<Page<NfcCltFlwdata>> pageNfcCltList(NfcFlowMinerQueryForm nfcFlowMinerQueryForm){
        Page page = nfcFlowMinerQueryForm.newFormPage();
        Long pageSize=page.getSize();
        Long pageCurrent =page.getCurrent();
        pageCurrent=(pageCurrent-1)*pageSize;
        nfcFlowMinerQueryForm.setCurrent(pageCurrent);
        nfcFlowMinerQueryForm.setSize(pageSize);
        List<NfcCltFlwdata> listInfo =nfcFlowMinerMapper.getNfcCltPageList(nfcFlowMinerQueryForm);
        long total=nfcFlowMinerMapper.getNfcCltTotal(nfcFlowMinerQueryForm);
        page.setRecords(listInfo);
        page.setTotal(total);
        return ResultMap.getSuccessfulResult(page);
    }
    public ResultMap<Page<NfcCltFlwdata>> pageNfcCltDetailList(NfcFlowMinerQueryForm nfcFlowMinerQueryForm){
        Page page = nfcFlowMinerQueryForm.newFormPage();
        Long pageSize=page.getSize();
        Long pageCurrent =page.getCurrent();
        pageCurrent=(pageCurrent-1)*pageSize;
        nfcFlowMinerQueryForm.setCurrent(pageCurrent);
        nfcFlowMinerQueryForm.setSize(pageSize);
        List<NfcCltFlwdata> listInfo =nfcFlowMinerMapper.getNfcCltDetailPageList(nfcFlowMinerQueryForm);
        long total=nfcFlowMinerMapper.getNfcCltDetailTotal(nfcFlowMinerQueryForm);
        page.setRecords(listInfo);
        page.setTotal(total);
        return ResultMap.getSuccessfulResult(page);
    }

    @Override
    public ResultMap<NfcCltFlwdata> nfcFlowCltDetail(NfcFlowMinerQueryForm nfcFlowMinerQueryForm) {
        return ResultMap.getSuccessfulResult(nfcFlowMinerMapper.nfcFlowCltDetail(nfcFlowMinerQueryForm));
    }


    /**
     * my/outLine
     * @param nfcFlowMinerQueryForm
     * @return
     */
    @Override
    public ResultMap<Map> outLine(NfcFlowMinerQueryForm nfcFlowMinerQueryForm) {
        Map<String,Object> outLine = new HashMap<>();
        outLine.put("minerCount",nfcFlowMinerMapper.getTotal(nfcFlowMinerQueryForm));
        List<Addresses> addressList = accountMapper.selectSingleAddress(nfcFlowMinerQueryForm.getRevenue_address());
        if(addressList.size()>0){
            Addresses address = addressList.get(0);
            outLine.put("address",address.getAddress());
            outLine.put("balance",address.getBalance());
            outLine.put("ful_balance",address.getFul_balance());
            outLine.put("ful_owe",address.getFul_owe());
        }
        NfcFlowMiner n =  nfcFlowMinerMapper.getStatisByReAddress(nfcFlowMinerQueryForm);
        outLine.put("payful",n.getPayful());
        outLine.put("revenue_amount",n.getRevenue_amount());
        outLine.put("nodeCount",nfcFlowMinerMapper.getNodeCount(nfcFlowMinerQueryForm));
        BlockQueryForm blockQueryForm = new BlockQueryForm();
        blockQueryForm.setRevenueAddress(nfcFlowMinerQueryForm.getRevenue_address());
        blockQueryForm.setType(5);
        TransferMinerVo flvo = transferMinerMapper.getLockSummaryByRevAddress(blockQueryForm);
        if(flvo!=null){
            outLine.put("flwtotalamount",flvo.getTotalAmount());
            outLine.put("flwreleaseamount",flvo.getReleaseamount());
        }else{
            outLine.put("flwtotalamount",0);
            outLine.put("flwreleaseamount",0);
        }
        blockQueryForm.setType(9);
        TransferMinerVo bdvo = transferMinerMapper.getLockSummaryByRevAddress(blockQueryForm);
        if(bdvo!=null){
            outLine.put("bdtotalamount",bdvo.getTotalAmount());
            outLine.put("bdreleaseamount",bdvo.getReleaseamount());
        }else{
            outLine.put("bdtotalamount",0);
            outLine.put("bdreleaseamount",0);
        }
        blockQueryForm.setType(1);
        TransferMinerVo blockvo = transferMinerMapper.getLockSummaryByRevAddress(blockQueryForm);
        if(blockvo!=null){
            outLine.put("blocktotalamount",blockvo.getTotalAmount());
            outLine.put("blockreleaseamount",blockvo.getReleaseamount());
        }else{
            outLine.put("blocktotalamount",0);
            outLine.put("blockreleaseamount",0);
        }
        return ResultMap.getSuccessfulResult(outLine);
    }


    @Override
    public AuthCfg getAuthCfgByAppid(String appid) {
        return nfcFlowMinerMapper.getAuthCfgByAppid(appid);
    }

    @Override
    public ResultMap<Area> getArea() {
        return ResultMap.getSuccessfulResult(nfcFlowMinerMapper.getArea());
    }

    @Override
    public ResultMap<OperatorConfig> getOperatorConfig() {
        return ResultMap.getSuccessfulResult(nfcFlowMinerMapper.getOperatorConfig());
    }

    @Override
    public ResultMap<Map> netBandWidth() {
        Map<String,Object> result = new HashMap<>();
        result.put("poolBandWidth",nfcFlowMinerMapper.getMinerSum().getBandwidth());
        return ResultMap.getSuccessfulResult(result);
    }

    @Override
    public ResultMap<Map> bandWidthToNfc(Long bandwidth) {
        Map<String,Object> result = new HashMap<>();
        List<NfcBandwidthConfig>  bandwidthConfig =  nfcFlowMinerMapper.getBandwidthConfig();
        int scale = 3;
        for (int i = 0; i < bandwidthConfig.size(); i++) {
            if (null != bandwidthConfig.get(i).getMax()) {
                if (bandwidth >= bandwidthConfig.get(i).getMin() && bandwidth <= bandwidthConfig.get(i).getMax()) {
                    scale = bandwidthConfig.get(i).getVal().multiply(new BigDecimal("100")).intValue();
                    break;
                }
            } else {
                if (bandwidth > bandwidthConfig.get(i).getMin()) {
                    scale = bandwidthConfig.get(i).getVal().multiply(new BigDecimal("100")).intValue();
                    break;
                }
            }
        }
        BigInteger Amount=null;
        NfcFlowMiner nm = nfcFlowMinerMapper.getMinerSum();
        BigInteger FlowTotal=nm.getBandwidth()==null?new BigInteger("0"):nm.getBandwidth().toBigInteger();
        BigInteger total=FlowTotal;
        //1EB flow
        BigInteger eb1=new BigInteger("1073741824").multiply(new BigInteger("1024"));//1EB 1073741824 * 1024
        BigInteger FlowHarvest= nm.getRevenue_amount()==null?new BigInteger("0"):nm.getRevenue_amount().toBigInteger();
        if (0<FlowTotal.compareTo(eb1)) {
            Amount=(new BigInteger(bandwidth+"").multiply(FlowHarvest).multiply(new BigInteger(scale+""))).divide(total).divide(new BigInteger("100"));
        } else {
            Amount=(new BigInteger(bandwidth+"").multiply(new BigInteger("3660208594").multiply(new BigInteger("10").pow(16))).multiply(new BigInteger(scale+""))).divide(new BigInteger("570480")).divide(new BigInteger("100"));

        }
        result.put("nfc",Amount.toString());
        return ResultMap.getSuccessfulResult(result);
    }

    public  ResultMap  getMinnnerPledgeStatus(NfcFlowMinerQueryForm nfcFlowMinerQueryForm){
        NfcFlowMiner vo = nfcFlowMinerMapper.getSingleMiner(nfcFlowMinerQueryForm.getMiner_addr());
        if(vo!=null){
            return ResultMap.getSuccessfulResult(vo.getMiner_status());
        }else{
            return ResultMap.getFailureResult("query no data");
        }
    }

    @Override
    public ResultMap<Map> getNfcNetStatics(NfcNetStaticsForm net) {
        String endTime = net.getEndTime();
        Date end = null;
        if(StringUtils.isEmpty(endTime)){
            end = new Date();
            endTime = DateUtil.getFormatDateTime(end, "yyyy-MM-dd");
        }else{
            endTime = endTime.substring(0,10);
            end = DateUtil.parseDate(endTime, "yyyy-MM-dd");
        }
        String startTime = net.getStartTime();
        if(StringUtils.isEmpty(startTime)){
            startTime = DateUtil.getFormatDateTime(DateUtil.addDaysToDate(end,-30), "yyyy-MM-dd");
        }else{
            startTime = startTime.substring(0,10);
        }
        return ResultMap.getSuccessfulResult(nfcFlowMinerMapper.queryNfcNetStaticsBetwentime(startTime,endTime));
    }

    @Override
    public ResultMap<Map> getNetServiceRank(NfcFlowMinerQueryForm nfcFlowMinerQueryForm) {
        if(StringUtils.isEmpty(nfcFlowMinerQueryForm.getTime())){
            String day =  DateUtil.getFormatDateTime( DateUtil.getBeginDayOfYesterday(), "yyyy-MM-dd");
            nfcFlowMinerQueryForm.setTime(day);
        }
        BigDecimal totalBw = nfcFlowMinerMapper.getMinerSum().getBandwidth();
        nfcFlowMinerQueryForm.setBandwidth(totalBw);
        Page page = nfcFlowMinerQueryForm.newFormPage();
        Long pageSize=page.getSize();
        Long pageCurrent =page.getCurrent();
        pageCurrent=(pageCurrent-1)*pageSize;
        nfcFlowMinerQueryForm.setCurrent(pageCurrent);
        nfcFlowMinerQueryForm.setSize(pageSize);
        long total=nfcFlowMinerMapper.getNetServiceRankTotal(nfcFlowMinerQueryForm);
        page.setTotal(total);
        if(total>0){
            List<NetRankVo> listInfo =nfcFlowMinerMapper.getNetServiceRankList(nfcFlowMinerQueryForm);
            page.setRecords(listInfo);
        }
        return ResultMap.getSuccessfulResult(page);
    }
	@Override
	public ResultMap<Page<FlowReport>> getFlowReportPageList(NfcFlowMinerQueryForm nfcFlowMinerQueryForm) {
		Page page = nfcFlowMinerQueryForm.newFormPage();
        Long pageSize=page.getSize();
        Long pageCurrent =page.getCurrent();
        pageCurrent=(pageCurrent-1)*pageSize;
        nfcFlowMinerQueryForm.setCurrent(pageCurrent);
        nfcFlowMinerQueryForm.setSize(pageSize);
        String sortBy = nfcFlowMinerQueryForm.getSortBy();
        if(sortBy!=null&&OrderUtils.nfcFlowMinerList.contains(sortBy)){
            nfcFlowMinerQueryForm.setSortSql(OrderUtils.sort(sortBy,nfcFlowMinerQueryForm.isDescending()));
        }
        List<FlowReport> listInfo = nfcFlowMinerMapper.getFlowReportPageList(nfcFlowMinerQueryForm);
        long total=nfcFlowMinerMapper.getFlowReportTotal(nfcFlowMinerQueryForm);
        page.setRecords(listInfo);
        page.setTotal(total);
        return ResultMap.getSuccessfulResult(page);
	}
}
