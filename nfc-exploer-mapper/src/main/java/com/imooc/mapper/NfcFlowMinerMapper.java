package com.imooc.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imooc.pojo.Form.NfcFlowMinerQueryForm;
import com.imooc.pojo.FlowReport;
import com.imooc.pojo.NfcBandwidthConfig;
import com.imooc.pojo.NfcCltFlwdata;
import com.imooc.pojo.NfcFlowMiner;
import com.imooc.pojo.NfcNetStatics;
import com.imooc.pojo.vo.*;
import com.imooc.utils.MyMapper;
import com.imooc.utils.ResultMap;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface NfcFlowMinerMapper extends MyMapper<NfcFlowMiner> {
    List<NfcFlowMiner> getPageList(@Param("nfcFlowMinerQueryForm") NfcFlowMinerQueryForm nfcFlowMinerQueryForm);

    long getTotal(@Param("nfcFlowMinerQueryForm")NfcFlowMinerQueryForm nfcFlowMinerQueryForm);

    List<NfcFlowMiner> getMinerRankPageList(@Param("nfcFlowMinerQueryForm") NfcFlowMinerQueryForm nfcFlowMinerQueryForm);

    long getMinerRankTotal(@Param("nfcFlowMinerQueryForm")NfcFlowMinerQueryForm nfcFlowMinerQueryForm);

    NfcFlowMiner getNfcFlowMinerDetail(@Param("nfcFlowMinerQueryForm") NfcFlowMinerQueryForm nfcFlowMinerQueryForm);

    long getMinerDayStatisCount(@Param("nfcFlowMinerQueryForm")NfcFlowMinerQueryForm nfcFlowMinerQueryForm);

    List<NfcFlowMinerDayVo> getMinerDayStatislist(@Param("nfcFlowMinerQueryForm") NfcFlowMinerQueryForm nfcFlowMinerQueryForm);

    long getNodeCount(@Param("nfcFlowMinerQueryForm")NfcFlowMinerQueryForm nfcFlowMinerQueryForm);

    NfcFlowMiner getStatisByReAddress(@Param("nfcFlowMinerQueryForm")NfcFlowMinerQueryForm nfcFlowMinerQueryForm);

    List<NfcCltFlwdata> getNfcCltPageList(@Param("nfcFlowMinerQueryForm") NfcFlowMinerQueryForm nfcFlowMinerQueryForm);

    List<NfcCltFlwdata> getNfcCltDetailPageList(@Param("nfcFlowMinerQueryForm") NfcFlowMinerQueryForm nfcFlowMinerQueryForm);

    NfcCltFlwdata nfcFlowCltDetail(@Param("nfcFlowMinerQueryForm")NfcFlowMinerQueryForm nfcFlowMinerQueryForm);

    long getNfcCltTotal(@Param("nfcFlowMinerQueryForm")NfcFlowMinerQueryForm nfcFlowMinerQueryForm);

    long getNfcCltDetailTotal(@Param("nfcFlowMinerQueryForm")NfcFlowMinerQueryForm nfcFlowMinerQueryForm);

    void saveOrUpdata(@Param("nfcFlowMiner")NfcFlowMiner nfcFlowMiner);

    void updateFlowMiner(@Param("nfcFlowMiner")NfcFlowMiner nfcFlowMiner);

    NfcFlowMiner getSingleMiner(@Param("miner_addr") String miner_addr);

    List<NfcBandwidthConfig> getBandwidthConfig();

    NfcFlowMiner getMinerSum();

    AuthCfg getAuthCfgByAppid(@Param("appid") String appid);

    List<Area> getArea();

    List<OperatorConfig> getOperatorConfig();

//    void saveOrUpdataFlwData(@Param("nfcCltFlwdata")NfcCltFlwdata nfcCltFlwdata);

//    void saveOrUpdataFlwDataDay(@Param("nfcCltFlwdata")NfcCltFlwdata nfcCltFlwdata);

    void batchSaveFlwDataDay(@Param("list")List<NfcCltFlwdata> list);

    void batchUpdateFlwDataDay(@Param("list")List<NfcCltFlwdata> list);

    List<NfcCltFlwdata> getFlwDataDayListByReportTime(@Param("ctime") String ctime);

    void batchSaveFlwDataDetail(@Param("list")List<NfcCltFlwdata> list);

    void insertNfcNetStatics(@Param("nns")NfcNetStatics nns);

    NfcNetStatics queryNfcNetStaticsByCtime(@Param("ctime") String ctime);

    NfcCltFlwdata queryNfcCltFlwdataDayByTime(@Param("ctime") String ctime);

    NfcCltFlwdata queryNfcCltFlwdataDayByAddress(@Param("address") String address);

    List<NfcCltFlwdata> queryNfcCltFlwdataDayGroupBYAddress();

    List<NfcFlowMiner> getAllMinerAddress();

    List<NfcNetStatics> queryNfcNetStaticsBetwentime(@Param("startTime") String startTime,@Param("endTime") String endTime);

    long getNetServiceRankTotal(@Param("nfcFlowMinerQueryForm")NfcFlowMinerQueryForm nfcFlowMinerQueryForm);

    List<NetRankVo>  getNetServiceRankList(@Param("nfcFlowMinerQueryForm")NfcFlowMinerQueryForm nfcFlowMinerQueryForm);

    void updateFlowMinerBatch(@Param("list")List<NfcFlowMiner> list);

    
    
    List<FlowReport> getFlowReportPageList(@Param("nfcFlowMinerQueryForm") NfcFlowMinerQueryForm nfcFlowMinerQueryForm);

    long getFlowReportTotal(@Param("nfcFlowMinerQueryForm") NfcFlowMinerQueryForm nfcFlowMinerQueryForm);
	
	void saveOrUpdateFlowReport(@Param("flowReport")FlowReport flowReport);
}
