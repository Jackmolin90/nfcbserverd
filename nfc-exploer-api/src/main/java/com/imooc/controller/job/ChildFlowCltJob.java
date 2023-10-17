package com.imooc.controller.job;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import com.imooc.Utils.AgentSvcTask;
import com.imooc.Utils.SpringContextUtils;
import com.imooc.controller.config.ExploerCfg;
import com.imooc.mapper.SyncChildBlocknumberMapper;
import com.imooc.pojo.NfcCltFlwdata;
import com.imooc.pojo.SyncChildBlocknumber;
import com.imooc.service.ChildFlowCltService;
import com.imooc.utils.Constants;
import com.imooc.utils.Web3jChildUtils;
import com.imooc.utils.Web3jUtils;
import org.apache.commons.codec.digest.DigestUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Numeric;

/**
 *
 */
@Slf4j
@Component
public class ChildFlowCltJob extends AgentSvcTask {

    private ExploerCfg  cfg;
	private ChildFlowCltService service;
	private SyncChildBlocknumberMapper syncMapper;
	private String syncid = "0";

	protected  void runTask() {
		service = SpringContextUtils.getBean(ChildFlowCltService.class);
		syncMapper = SpringContextUtils.getBean(SyncChildBlocknumberMapper.class);
		cfg = SpringContextUtils.getBean(ExploerCfg.class);
		while(!shutdown) {
			long stime=System.currentTimeMillis();
			Calendar ca = Calendar.getInstance();
			int minut = ca.get(Calendar.MINUTE);
			try {
				if(minut%cfg.getFlowcltminute()==0) {
					log.info("ChildFlowCltJob start!");
					Web3jChildUtils wb = new Web3jChildUtils();
					Web3j web3j = Web3jChildUtils.getWeb3j(cfg.getChildipaddress());
					Long currentBlockNumber = wb.getCurrentBlockNumber(web3j);
					Long lastBlockNumber = null;
					SyncChildBlocknumber syncHeight  = syncMapper.selectByPrimaryKey(syncid);
					if(syncHeight==null) {
						lastBlockNumber = 0l;
						syncMapper.insert( new SyncChildBlocknumber(syncid,lastBlockNumber,new Date()));
						syncHeight  = syncMapper.selectByPrimaryKey(syncid);
					}else {
						lastBlockNumber = syncHeight.getBlocknumber();
					}
					if(lastBlockNumber < currentBlockNumber) {
						traverseBlockNumber(web3j,currentBlockNumber,lastBlockNumber,syncHeight);
					}else {
						log.warn("ChildFlowCltJob lastBlockNumber >= currentBlockNumber");
					}
					log.info("ChildFlowCltJob end!");
				}

			}catch (Exception e) {
				log.error("ChildFlowCltJob error",e);
			}finally {
				try {
					long etime=System.currentTimeMillis();
					if(60000-(etime-stime)>0) {
						Thread.sleep(60000-(etime-stime));
					}
				} catch (InterruptedException e) {
				}
			}
		}
	
	}

	public synchronized void traverseBlockNumber(Web3j web3j, Long currentBlockNumber, Long lastBlockNumber, SyncChildBlocknumber syncHeight) {
		Date synctime = new Date();
		for (long i = lastBlockNumber; i < currentBlockNumber; i++) {
			Long blockNumber = i+1;
			BigInteger blockNumberPara = BigInteger.valueOf(blockNumber);
			EthBlock ethBlock;
			try {
				ethBlock = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(blockNumberPara), true).send();
			} catch (IOException e) {
				log.error("ethGetBlockByNumber error,"+e.getMessage(),e);
				break;
			}
			if (null == ethBlock) {
				break;
			}
			EthBlock.Block block = ethBlock.getBlock();
			if (null == block) {
				break;
			}
			try {
				List<NfcCltFlwdata> cltList = new ArrayList<NfcCltFlwdata>();
				List<Map> flowreports=block.getFlowreports();
				for (Map rp: flowreports) {
					List<Map> flowlist= (List<Map>) rp.get("flowList");
					for (Map flow:flowlist) {
						if(flow.get("flowrecord")!=null){
							Map flowrecord= (Map) flow.get("flowrecord");
							pckFlowVo(blockNumber,flowrecord, cltList);
						}
					}
				}
				syncHeight.setBlocknumber(blockNumber);
				syncHeight.setSynctime(synctime);
				service.saveCltDB(cltList,syncHeight);
			} catch (Exception e) {
				log.error("transaction error,"+e.getMessage(),e);
				break;
			}

		}
	}

	/*
	  pckFlow
	 */
	private void pckFlowVo(Long blockNumber,Map flowrecord,List<NfcCltFlwdata> cltList){
		NfcCltFlwdata vo = new NfcCltFlwdata();
		//ReportTime
		BigInteger ReportTime=new BigInteger(flowrecord.get("nonce").toString().substring(2),16);
		Date date=new Date();
		date.setTime(ReportTime.longValue()*1000l);
		vo.setReport_time(date);
		//service address
		String ToAddress= (String) flowrecord.get("to");
		vo.setEn_address(ToAddress);
		//flow
		BigInteger FlowValue= new BigInteger(flowrecord.get("value").toString().substring(2),16) ;
		vo.setFlow_value(FlowValue.longValue());
		//ful
		vo.setFulnum(new BigDecimal(vo.getFlow_value()).multiply(new BigDecimal(Constants.M_FUl))
				.multiply(new BigDecimal(Constants.decimals)).divide(new BigDecimal("1048576"),0,BigDecimal.ROUND_HALF_UP));
		//RouterAddress
		vo.setRouter_address((String)flowrecord.get("routeraddress"));
		//from deviceid
		//str="0x353633313236303437313539333139";
		String FromData= new String(Numeric.hexStringToByteArray((String)flowrecord.get("fromdesc"))) ;
		vo.setFrom_addr(FromData);
		//routerdesc
		vo.setRouter_ipaddr(new String(Numeric.hexStringToByteArray((String)flowrecord.get("routerdesc"))));
		//to
		//str="0x353633313030323737363432393932";
		String ToData= new String(Numeric.hexStringToByteArray((String)flowrecord.get("input"))) ;
		vo.setTo_addr(ToData);
		String hash= (String) flowrecord.get("hash");
		vo.setTrans_hash(hash);
		vo.setBlocknumber(blockNumber);
		vo.setInstime(new Date());
		cltList.add(vo);
	}
}
