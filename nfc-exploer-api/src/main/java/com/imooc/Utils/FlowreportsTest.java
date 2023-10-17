package com.imooc.Utils;

import lombok.extern.slf4j.Slf4j;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
public class FlowreportsTest {
    public void testFlowreports() throws Exception {

        Web3j web3 = Web3j.build(new HttpService("http://192.168.9.101:8510/"));
        EthBlock ethBlock = web3.ethGetBlockByNumber(DefaultBlockParameter.valueOf(new BigInteger("6005")), true).send();
        EthBlock.Block block = ethBlock.getBlock();

        List<Map> flowreports=block.getFlowreports();
        List<Map> flowlist= (List<Map>) flowreports.get(0).get("flowList");
        Map flowrecord= (Map) flowlist.get(0).get("flowrecord");

        //ReportTime
        BigInteger ReportTime=new BigInteger(flowrecord.get("nonce").toString().substring(2),16);
        Date date=new Date();
        date.setTime(ReportTime.longValue()*1000l);

        String str="";
        //服务设备地址
        String ToAddress= (String) flowrecord.get("to");
        //流量值
        BigInteger FlowValue= new BigInteger(flowrecord.get("value").toString().substring(2),16) ;
        //流量地址
        String RouterAddress= (String)flowrecord.get("routeraddress") ;
        //from 设备id
        str=(String)flowrecord.get("fromdesc");
        //str="0x353633313236303437313539333139";
        String FromData= new String(Numeric.hexStringToByteArray(str)) ;
        //路由id
        str=(String)flowrecord.get("routerdesc");
        //str=0x31303035
        String RouterDesc= new String(Numeric.hexStringToByteArray(str)) ;
        //to 设备id
        str=(String)flowrecord.get("input");
        //str="0x353633313030323737363432393932";
        String ToData= new String(Numeric.hexStringToByteArray(str)) ;

        String hash= (String) flowrecord.get("hash");


        log.info("date:" +date);
        log.info("ToAddress:" +ToAddress);
        log.info("FlowValue:" +FlowValue);
        log.info("RouterAddress:" +RouterAddress);
        log.info("FromData:" +FromData);
        log.info("RouterDesc:" +RouterDesc);
        log.info("ToData:" +ToData);
        log.info("hash:" +hash);
    }

    public static void main(String[] args)throws Exception  {
        FlowreportsTest f = new FlowreportsTest();
        f.testFlowreports();

    }
}