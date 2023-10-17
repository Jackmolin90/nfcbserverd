package com.imooc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imooc.pojo.Form.NfcFlowMinerQueryForm;
import com.imooc.pojo.Form.NfcNetStaticsForm;
import com.imooc.pojo.FlowReport;
import com.imooc.pojo.NfcCltFlwdata;
import com.imooc.pojo.NfcFlowMiner;
import com.imooc.pojo.vo.*;
import com.imooc.utils.ResultMap;
import net.sf.json.JSONObject;

import java.util.Map;

public interface NfcFlowMinerService {
    ResultMap<Page<NfcFlowMiner>> pageList(NfcFlowMinerQueryForm nfcFlowMinerQueryForm);
    ResultMap<Page<NfcFlowMiner>> minerRankPageList(NfcFlowMinerQueryForm nfcFlowMinerQueryForm);
    ResultMap<NfcFlowMiner> getNfcFlowMinerDetail(NfcFlowMinerQueryForm nfcFlowMinerQueryForm);

    ResultMap<Page<NfcFlowMinerDayVo>>  getMinerDayStatislist(NfcFlowMinerQueryForm nfcFlowMinerQueryForm);

    ResultMap<Map> outLine(NfcFlowMinerQueryForm nfcFlowMinerQueryForm);

    ResultMap<Page<NfcCltFlwdata>> pageNfcCltList(NfcFlowMinerQueryForm nfcFlowMinerQueryForm);

    ResultMap<Page<NfcCltFlwdata>> pageNfcCltDetailList(NfcFlowMinerQueryForm nfcFlowMinerQueryForm);

    ResultMap<NfcCltFlwdata> nfcFlowCltDetail(NfcFlowMinerQueryForm nfcFlowMinerQueryForm);

    AuthCfg getAuthCfgByAppid(String appid);

    ResultMap<Area> getArea();

    ResultMap<OperatorConfig> getOperatorConfig();

    ResultMap<Map> netBandWidth();

    ResultMap<Map> bandWidthToNfc(Long width);

    ResultMap  getMinnnerPledgeStatus(NfcFlowMinerQueryForm nfcFlowMinerQueryForm);

    ResultMap<Map> getNfcNetStatics(NfcNetStaticsForm net);

    ResultMap<Map> getNetServiceRank(NfcFlowMinerQueryForm nfcFlowMinerQueryForm);

    ResultMap<Page<FlowReport>> getFlowReportPageList(NfcFlowMinerQueryForm nfcFlowMinerQueryForm);


}
