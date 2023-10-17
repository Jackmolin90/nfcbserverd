package com.imooc.controller;

import com.imooc.pojo.Form.BlockQueryForm;
import com.imooc.pojo.Form.NfcFlowMinerQueryForm;
import com.imooc.pojo.Form.TransactionQueryForm;
import com.imooc.pojo.Form.TxQueryForm;
import com.imooc.pojo.Param.TransactionQueryParam;
import com.imooc.pojo.vo.AddressVo;
import com.imooc.pojo.vo.AuthCfg;
import com.imooc.service.AccountService;
import com.imooc.service.NfcFlowMinerService;
import com.imooc.service.TransactionService;
import com.imooc.utils.Constants;
import com.imooc.utils.HmacSha256Util;
import com.imooc.utils.Result;
import com.imooc.utils.ResultMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author zhc 2021-10-05 16:12
 */
/*api*/
@CrossOrigin
@RestController
@RequestMapping("/api")
@Api(tags="outside interface")
@Slf4j
public class ApiController {
    @Autowired
    private NfcFlowMinerService nfcFlowMinerService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @ApiOperation(value = "Miner query", notes = "Miner query")
    @ResponseBody
    @PostMapping("/getflowMinerlist")
    public ResultMap getflowMinerlist(@Valid @RequestBody NfcFlowMinerQueryForm nfcFlowMinerQueryForm)throws  Exception {
        nfcFlowMinerQueryForm.setMiner_addr(Constants.pre0XtoNX(nfcFlowMinerQueryForm.getMiner_addr()));
        nfcFlowMinerQueryForm.setRevenue_address(Constants.pre0XtoNX(nfcFlowMinerQueryForm.getRevenue_address()));
        if(StringUtils.isEmpty(nfcFlowMinerQueryForm.getAppid())){
            return ResultMap.getApiFailureResult("param appid is empty!");
        }
        if(StringUtils.isEmpty(nfcFlowMinerQueryForm.getSign())){
            return ResultMap.getApiFailureResult("param sign is empty!");
        }
        AuthCfg authCfg = nfcFlowMinerService.getAuthCfgByAppid(nfcFlowMinerQueryForm.getAppid());
        if(authCfg==null){
            return ResultMap.getApiFailureResult("can not find appkey ,when appid is "+nfcFlowMinerQueryForm.getAppid());
        }
        //sha256(md5(appid=$appid&random=$random&blocknumber=$blocknumber&current=$current&method=getflowMinerlist),key)
        String sign = HmacSha256Util.getSign("appid="+nfcFlowMinerQueryForm.getAppid()
                +"&random="+nfcFlowMinerQueryForm.getRandom()+"&blocknumber="+nfcFlowMinerQueryForm.getBlocknumber()+"&current="+nfcFlowMinerQueryForm.getCurrent()
                +"&method=getflowMinerlist",authCfg.getAppkey());
        if(!nfcFlowMinerQueryForm.getSign().equalsIgnoreCase(sign)){
            log.info("sign error,sign="+sign);
            return ResultMap.getApiFailureResult("sign validate error");
        }
        return nfcFlowMinerService.pageList(nfcFlowMinerQueryForm);
    }

    @ApiOperation(value = "Miner mining day statistics query", notes = "Miner mining day statistics query")
    @ResponseBody
    @PostMapping("/getMinerDayStatislist")
    public ResultMap getMinerDayStatislist(@Valid @RequestBody NfcFlowMinerQueryForm nfcFlowMinerQueryForm)throws  Exception {
        nfcFlowMinerQueryForm.setMiner_addr(Constants.pre0XtoNX(nfcFlowMinerQueryForm.getMiner_addr()));
        if(StringUtils.isEmpty(nfcFlowMinerQueryForm.getAppid())){
            return ResultMap.getApiFailureResult("param appid is empty!");
        }
        if(StringUtils.isEmpty(nfcFlowMinerQueryForm.getSign())){
            return ResultMap.getApiFailureResult("param sign is empty!");
        }
        AuthCfg authCfg = nfcFlowMinerService.getAuthCfgByAppid(nfcFlowMinerQueryForm.getAppid());
        if(authCfg==null){
            return ResultMap.getApiFailureResult("can not find appkey ,when appid is "+nfcFlowMinerQueryForm.getAppid());
        }
        //sha256(md5(appid=$appid&random=$random&current=$current&method=getMinerDayStatislist),key)
        String sign = HmacSha256Util.getSign("appid="+nfcFlowMinerQueryForm.getAppid()
                +"&random="+nfcFlowMinerQueryForm.getRandom()+"&current="+nfcFlowMinerQueryForm.getCurrent()
                +"&method=getMinerDayStatislist",authCfg.getAppkey());
        if(!nfcFlowMinerQueryForm.getSign().equalsIgnoreCase(sign)){
            log.info("sign error,sign="+sign);
            return ResultMap.getApiFailureResult("sign validate error");
        }
        return nfcFlowMinerService.getMinerDayStatislist(nfcFlowMinerQueryForm);
    }

    @ApiOperation(value = "Recharge record query", notes = "Recharge record query")
    @ResponseBody
    @PostMapping("/getExchRecord")
    public ResultMap getExchRecord(@Valid @RequestBody TxQueryForm tx) throws  Exception{
        tx.setPay_address(Constants.pre0XtoNX(tx.getPay_address()));
        tx.setCharge_address(Constants.pre0XtoNX(tx.getCharge_address()));
        if(StringUtils.isEmpty(tx.getAppid())){
            return ResultMap.getApiFailureResult("param appid is empty!");
        }
        if(StringUtils.isEmpty(tx.getSign())){
            return ResultMap.getApiFailureResult("param sign is empty!");
        }
        AuthCfg authCfg = nfcFlowMinerService.getAuthCfgByAppid(tx.getAppid());
        if(authCfg==null){
            return ResultMap.getApiFailureResult("can not find appkey ,when appid is "+tx.getAppid());
        }
        //sha256(md5(appid=$appid&random=$random&blocknumber=$blocknumber&current=current&txType=$txType),key)
        String sign = HmacSha256Util.getSign("appid="+tx.getAppid()
                +"&random="+tx.getRandom()+"&blocknumber="+tx.getBlocknumber()+"&current="+tx.getCurrent()+"&txType="+tx.getTxType(),authCfg.getAppkey());
        if(!tx.getSign().equalsIgnoreCase(sign)){
            log.info("sign error,sign="+sign);
            return ResultMap.getApiFailureResult("sign validate error");
        }
        return transactionService.txPageList(tx);
    }

    @ApiOperation(value = "getTransactionByTxHash", notes = "getTransactionByTxHash")
    @ResponseBody
    @PostMapping("/getTransactionByTxHash")
    public ResultMap getTransactionByTxHash(@Valid @RequestBody TransactionQueryForm tx)throws  Exception {
        tx.toParam(TransactionQueryParam.class);
        tx.setTxHash(tx.getTxHash());
        if(StringUtils.isEmpty(tx.getAppid())){
            return ResultMap.getApiFailureResult("param appid is empty!");
        }
        if(StringUtils.isEmpty(tx.getSign())){
            return ResultMap.getApiFailureResult("param sign is empty!");
        }
        AuthCfg authCfg = nfcFlowMinerService.getAuthCfgByAppid(tx.getAppid());
        if(authCfg==null){
            return ResultMap.getApiFailureResult("can not find appkey ,when appid is "+tx.getAppid());
        }
        //sha256(md5(appid=$appid&random=$random&txHash=$txHash,key)
        String sign = HmacSha256Util.getSign("appid="+tx.getAppid()
                +"&random="+tx.getRandom()+"&txHash="+tx.getTxHash(),authCfg.getAppkey());
        if(!tx.getSign().equalsIgnoreCase(sign)){
            log.info("sign error,sign="+sign);
            return ResultMap.getApiFailureResult("sign validate error");
        }
        return transactionService.getTransactionInfoByTxHash(tx);
    }

    @ApiOperation(value = "Network-wide computing power query", notes = "Network-wide computing power query")
    @ResponseBody
    @PostMapping("/netBandWidth")
    public ResultMap netBandWidth(@Valid @RequestBody NfcFlowMinerQueryForm nfcFlowMinerQueryForm)throws  Exception {
        if(StringUtils.isEmpty(nfcFlowMinerQueryForm.getAppid())){
            return ResultMap.getApiFailureResult("param appid is empty!");
        }
        if(StringUtils.isEmpty(nfcFlowMinerQueryForm.getSign())){
            return ResultMap.getApiFailureResult("param sign is empty!");
        }
        AuthCfg authCfg = nfcFlowMinerService.getAuthCfgByAppid(nfcFlowMinerQueryForm.getAppid());
        if(authCfg==null){
            return ResultMap.getApiFailureResult("can not find appkey ,when appid is "+nfcFlowMinerQueryForm.getAppid());
        }
        //sha256(md5(appid=$appid&random=$random,key)
        String sign = HmacSha256Util.getSign("appid="+nfcFlowMinerQueryForm.getAppid()
                +"&random="+nfcFlowMinerQueryForm.getRandom(),authCfg.getAppkey());
        if(!nfcFlowMinerQueryForm.getSign().equalsIgnoreCase(sign)){
            log.info("sign error,sign="+sign);
            return ResultMap.getApiFailureResult("sign validate error");
        }
        return nfcFlowMinerService.netBandWidth();
    }

    @ApiOperation(value = "getAddressVoByAddress", notes = "getAddressVoByAddress")
    @ResponseBody
    @PostMapping("/getAddressVoByAddress")
    public ResultMap getAddressVoByAddress(@Valid @RequestBody AddressVo addressVo)throws  Exception {
        if(StringUtils.isEmpty(addressVo.getAppid())){
            return ResultMap.getApiFailureResult("param appid is empty!");
        }
        if(StringUtils.isEmpty(addressVo.getSign())){
            return ResultMap.getApiFailureResult("param sign is empty!");
        }
        AuthCfg authCfg = nfcFlowMinerService.getAuthCfgByAppid(addressVo.getAppid());
        if(authCfg==null){
            return ResultMap.getApiFailureResult("can not find appkey ,when appid is "+addressVo.getAppid());
        }
        //sha256(md5(appid=$appid&random=$random&address=$address,key)
        String sign = HmacSha256Util.getSign("appid="+addressVo.getAppid()
                +"&random="+addressVo.getRandom()+"&address="+addressVo.getAddress(),authCfg.getAppkey());
        if(!addressVo.getSign().equalsIgnoreCase(sign)){
            log.info("sign error,sign="+sign);
        //    return ResultMap.getApiFailureResult("sign validate error");
        }
        return accountService.getAddressVoByaddress(addressVo.getAddress());
    }


    @ApiOperation(value = "getMinnnerPledgeStatus", notes = "getMinnnerPledgeStatus")
    @ResponseBody
    @PostMapping("/getMinnnerPledgeStatus")
    public ResultMap getMinnnerPledgeStatus(@Valid @RequestBody NfcFlowMinerQueryForm nfcFlowMinerQueryForm)throws  Exception {
        if(StringUtils.isEmpty(nfcFlowMinerQueryForm.getAppid())){
            return ResultMap.getApiFailureResult("param appid is empty!");
        }
        if(StringUtils.isEmpty(nfcFlowMinerQueryForm.getSign())){
            return ResultMap.getApiFailureResult("param sign is empty!");
        }
        AuthCfg authCfg = nfcFlowMinerService.getAuthCfgByAppid(nfcFlowMinerQueryForm.getAppid());
        if(authCfg==null){
            return ResultMap.getApiFailureResult("can not find appkey ,when appid is "+nfcFlowMinerQueryForm.getAppid());
        }
        //sha256(md5(appid=$appid&random=$random&miner_addr=$miner_addr,key)
        String sign = HmacSha256Util.getSign("appid="+nfcFlowMinerQueryForm.getAppid()
                +"&random="+nfcFlowMinerQueryForm.getRandom()
                +"&miner_addr="+nfcFlowMinerQueryForm.getMiner_addr(),authCfg.getAppkey());
        if(!nfcFlowMinerQueryForm.getSign().equalsIgnoreCase(sign)){
            log.info("sign error,sign="+sign);
       //     return ResultMap.getApiFailureResult("sign validate error");
        }
        return nfcFlowMinerService.getMinnnerPledgeStatus(nfcFlowMinerQueryForm);
    }
}
