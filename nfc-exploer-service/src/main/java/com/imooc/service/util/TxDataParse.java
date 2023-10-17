package com.imooc.service.util;

import com.imooc.enums.MinerStatusEnum;
import com.imooc.enums.NodeTypeEnum;
import com.imooc.mapper.NfcFlowMinerMapper;
import com.imooc.mapper.NodeExitMapper;
import com.imooc.mapper.TransactionMapper;
import com.imooc.pojo.FlowReport;
import com.imooc.pojo.Form.NfcNodeMinerQueryForm;
import com.imooc.pojo.NfcFlowMiner;
import com.imooc.pojo.NfcNodeMiner;
import com.imooc.pojo.Transaction;
import com.imooc.utils.Constants;
import com.imooc.utils.NfcOperatorEnum;
import com.imooc.utils.SscOperatorEnum;
import com.imooc.utils.UfoPrefixEnum;
import lombok.extern.slf4j.Slf4j;

import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * (NFC/SSC)
 * @author zhc 2021-09-09 18:00
 */
@Slf4j
public class TxDataParse {
    private final static   String zeroA = "0000000000000000000000000000000000000000";
//    private static String address1 = Constants.PRE+"7A4539Ed8A0b8B4583EAd1e5a3F604e83419f502";
//    private static String address2 = Constants.PRE+"7A4539Ed8A0b8B4583EAd1e5a3F604e83419f502";
//    private static String address3 = Constants.PRE+"7A4539Ed8A0b8B4583EAd1e5a3F604e83419f502";
    private static final String candReqTopic = Constants.PRE+"61edf63329be99ab5b931ab93890ea08164175f1bce7446645ba4c1c7bdae3a8"; //Node pledge
    private static final String flwReqTopic = Constants.PRE+"041e56787332f2495a47171278fa0f1ddb21961f702d0ba53c2bb2c079ccd418";//Data mining pledge, claimed bandwidth
    private static final String pledgeExitTopic=Constants.PRE+"9489b96ebcb056332b79de467a2645c56a999089b730c99fead37b20420d58e7"; //Node exit, traffic mining exit
    private static final String bindTopic = Constants.PRE+"f061654231b0035280bd8dd06084a38aa871445d0b7311be8cc2605c5672a6e3"; //Binding, unbinding, replacement
    private static final String exchTopic = Constants.PRE+"dd6398517e51250c7ea4c550bdbec4246ce3cd80eac986e8ebbbb0eda27dcf4c"; //ful exchange
    private static final String candPnshTopic = Constants.PRE+"d67fe14bb06aa8656e0e7c3230831d68e8ce49bb4a4f71448f98a998d2674621"; //Penalty make-up
    private static final String flwrptenTopic = Constants.PRE+"ea40f050c9c577748d5ddcdb6a19aab17cacb2fa5f63f3747c516b06b597afd1";

    /**
     * Transaction input
     * @param tx vo
     */
    public  static  void setTxData(Map<String, Boolean> ethAddress,String address1, String address2, String address3, Transaction tx, 
    		TransactionMapper transactionMapper, NfcFlowMinerMapper nfcFlowMinerMapper, NodeExitMapper nodeExitMapper, List<Log> logs, Long fulRate){
        if(tx.getStatus()!=1|| tx.getUfoprefix()==null||tx.getInput()==null)
                return;
        if(tx.getUfoprefix().equalsIgnoreCase(UfoPrefixEnum.NFC.getCode())){
                        nfc(ethAddress,tx,transactionMapper,nfcFlowMinerMapper,nodeExitMapper,logs,fulRate);
        }else if(tx.getUfoprefix().equalsIgnoreCase(UfoPrefixEnum.SSC.getCode())){
                        ssc(address1,address2,address3,tx,transactionMapper,nfcFlowMinerMapper,logs);
        }
    }

    /**
     *
     * NFC
     * @param tx
     */
    public  static  void nfc(Map<String, Boolean> ethAddress,Transaction tx, TransactionMapper transactionMapper, NfcFlowMinerMapper nfcFlowMinerMapper,NodeExitMapper nodeExitMapper,List<Log> logs,Long fulRate){
         if(logs==null||logs.size()==0){
            tx.setStatus(0);
            return;
         }
         List<String> topics = logs.get(0).getTopics();
         String logData = logs.get(0).getData();
         String data = tx.getInput();
         String ufoParameter = null;
         String srcData =null;
         if(data.startsWith(NfcOperatorEnum.Exch.getEncode())){
             BigInteger nfc = null;
             BigInteger ful = null;      //FUL
             if(topics!=null&&topics.size()>0&&topics.get(0).equalsIgnoreCase(exchTopic)){
                 nfc = bigNumberConvert(logData.substring(2,66));
                 ful = convertToBigDecimal("0x"+logData.substring(67,130)).toBigInteger();
             }else{
                 tx.setStatus(0);
                 return;
             }
             srcData = new String(Numeric.hexStringToByteArray(data.substring(NfcOperatorEnum.Exch.getEncode().length())));
             ufoParameter = srcData.substring(1);
             String [] split = ufoParameter.split(":");
             tx.dataBuild(addressParse(split[0]),null,
                     nfc.toString(),
                     ful==null?null:ful.toString(),null,null);
             ethAddress.put(addressParse(split[0]),true);
         }else if(data.startsWith(NfcOperatorEnum.Bind.getEncode())){
              String type = null;
             if(topics!=null&&topics.size()>0&&topics.get(0).equalsIgnoreCase(bindTopic)){
                 type = convertToBigDecimal(topics.get(2)).toString();
             }else{
                 tx.setStatus(0);
                 return;
             }
             srcData = new String(Numeric.hexStringToByteArray(data.substring(NfcOperatorEnum.Bind.getEncode().length())));
             ufoParameter = srcData.substring(1);
             String [] split = ufoParameter.split(":");
             String miner_addr = addressParse(split[0]);
             String revenue_address = tx.getFromAddr();
             tx.dataBuild(miner_addr,revenue_address,type,null,addressParse(split[2]),addressParse(split[3]));
             //Income address binding income type 0 node reward 1 traffic mining
             if(Integer.parseInt(type)==0){
                 //Node binding
                 NfcNodeMiner node = new NfcNodeMiner();
                 node.setNode_address(miner_addr);
                 node.setRevenue_address(revenue_address);
                 node.setSync_time(new Date());
                 nodeExitMapper.updateNodeMiner(node);
             }else if(Integer.parseInt(type)==1){
                 NfcFlowMiner pre_miner = nfcFlowMinerMapper.getSingleMiner(miner_addr);
                 if(pre_miner==null){
                     NfcFlowMiner minerInsert = new NfcFlowMiner(miner_addr,revenue_address,MinerStatusEnum.toBeAddPool.getCode(),tx.getBlockNumber(),new Date());
                     nfcFlowMinerMapper.saveOrUpdata(minerInsert);
                 }else{
                     NfcFlowMiner minerUpdate = new NfcFlowMiner(miner_addr,revenue_address,pre_miner.getMiner_status(),tx.getBlockNumber(),new Date());
                     nfcFlowMinerMapper.updateFlowMiner(minerUpdate);
                 }
             }
         }else if(data.startsWith(NfcOperatorEnum.Unbind.getEncode())){
             String type = null;
             if(topics!=null&&topics.size()>0&&topics.get(0).equalsIgnoreCase(bindTopic)){
                 type = convertToBigDecimal(topics.get(2)).toString();
             }else{
                 tx.setStatus(0);
                 return;
             }
             srcData = new String(Numeric.hexStringToByteArray(data.substring(NfcOperatorEnum.Unbind.getEncode().length())));
             ufoParameter = srcData.substring(1);
             String [] split = ufoParameter.split(":");
             String miner_addr = addressParse(split[0]);
             String revenue_address = tx.getFromAddr();
             tx.dataBuild(miner_addr,null,type,null,null,null);
             if(Integer.parseInt(type)==0){
                 //Node revenue address unbinding
                 NfcNodeMiner node = new NfcNodeMiner();
                 node.setNode_address(miner_addr);
                 node.setRevenue_address("");
                 node.setSync_time(new Date());
                 nodeExitMapper.updateNodeMiner(node);
             }else if(Integer.parseInt(type)==1){
                 //Unbind income address
                 NfcFlowMiner miner = new NfcFlowMiner();
                 miner.setMiner_addr(miner_addr);
                 miner.setRevenue_address("");
                 miner.setBlocknumber(tx.getBlockNumber());
                 miner.setSync_time(new Date());
                 nfcFlowMinerMapper.updateFlowMiner(miner);
             }
         }else if(data.startsWith(NfcOperatorEnum.Rebind.getEncode())){
             String type = null;
             if(topics!=null&&topics.size()>0&&topics.get(0).equalsIgnoreCase(bindTopic)){
                 type = convertToBigDecimal(topics.get(2)).toString();
             }else{
                 tx.setStatus(0);
                 return;
             }
             srcData = new String(Numeric.hexStringToByteArray(data.substring(NfcOperatorEnum.Rebind.getEncode().length())));
             ufoParameter = srcData.substring(1);
             String [] split = ufoParameter.split(":");
             String miner_addr = addressParse(split[0]);
             String revenue_address = addressParse(split[4]);
             tx.dataBuild(miner_addr,revenue_address ,type,null,addressParse(split[2]),addressParse(split[3]));
             if(Integer.parseInt(type)==0){
                 //Node revenue address replacement
                 NfcNodeMiner node = new NfcNodeMiner();
                 node.setNode_address(miner_addr);
                 node.setRevenue_address(revenue_address);
                 node.setSync_time(new Date());
                 nodeExitMapper.updateNodeMiner(node);
             }else if(Integer.parseInt(type)==1){
                 //Change of income address
                 NfcFlowMiner miner = new NfcFlowMiner();
                 miner.setMiner_addr(miner_addr);
                 miner.setRevenue_address(revenue_address);
                 miner.setBlocknumber(tx.getBlockNumber());
                 miner.setSync_time(new Date());
                 if(!zeroA.equalsIgnoreCase(split[3])){
                     miner.setAddpool(1);
                 }
                 nfcFlowMinerMapper.updateFlowMiner(miner);
             }
         }else if(data.startsWith(NfcOperatorEnum.CandReq.getEncode())){
             srcData = new String(Numeric.hexStringToByteArray(data.substring(NfcOperatorEnum.CandReq.getEncode().length())));
             ufoParameter = srcData.substring(1);
             String address = addressParse(ufoParameter.substring(0));
             String type  = null;
             BigDecimal pledgeAmout = null;
             if(topics!=null&&topics.size()>0&&topics.get(0).equalsIgnoreCase(candReqTopic)){
                 type = convertToBigDecimal(topics.get(2)).toString();
                 pledgeAmout = convertToBigDecimal("0x"+logData.substring(2,66));
             }else{
                 tx.setStatus(0);
                 return;
             }
             tx.dataBuild(address,null,type,pledgeAmout.toString(),null,null);

             NfcNodeMinerQueryForm queryForm = new  NfcNodeMinerQueryForm();
             queryForm.setNode_address(address);
             NfcNodeMiner pre_node = nodeExitMapper.getNode(queryForm);
             NfcNodeMiner node = new NfcNodeMiner() ;
             node.setNode_address(address);
             node.setNode_type(NodeTypeEnum.wait.getCode());
             node.setBlocknumber(tx.getBlockNumber());
             if(pre_node!=null) {
                 node.setPledge_amount(add(pre_node.getPledge_amount(), pledgeAmout));
             }else{
                 node.setPledge_amount(pledgeAmout);
             }
             node.setJoin_time(tx.getTimeStamp());
             node.setSync_time(new Date());
             nodeExitMapper.saveOrUpdateNodeMiner(node);
         }else if(data.startsWith(NfcOperatorEnum.CandExit.getEncode())){
             srcData = new String(Numeric.hexStringToByteArray(data.substring(NfcOperatorEnum.CandExit.getEncode().length())));
             ufoParameter = srcData.substring(1);
             String address = addressParse(ufoParameter.substring(0));
             String type  = null;
             if(topics!=null&&topics.size()>0&&topics.get(0).equalsIgnoreCase(pledgeExitTopic)){
                 type = convertToBigDecimal(topics.get(2)).toString();
             }else{
                 tx.setStatus(0);
                 return;
             }
             tx.dataBuild(address,null,type,null,null,null);
         }else if(data.startsWith(NfcOperatorEnum.CandPnsh.getEncode())){
             srcData = new String(Numeric.hexStringToByteArray(data.substring(NfcOperatorEnum.CandPnsh.getEncode().length())));
             ufoParameter = srcData.substring(1);
             String address = addressParse(ufoParameter.substring(0));
             Integer mbpoint = 0;
             BigInteger fee = null;
             if(topics!=null&&topics.size()>0&&topics.get(0).equalsIgnoreCase(candPnshTopic)){
                 mbpoint = convertToBigDecimal("0x"+logData.substring(2,66)).intValue();
                 fee = convertToBigDecimal("0x"+logData.substring(67,130)).toBigInteger();
             }else{
                 tx.setStatus(0);
                 return;
             }
             tx.dataBuild(address,null,mbpoint==null?null:mbpoint.toString(),
                     fee==null?null:fee.toString(),null,null);
             //Block out penalties to make up
             NfcNodeMinerQueryForm queryForm = new  NfcNodeMinerQueryForm();
             queryForm.setNode_address(address);
             NfcNodeMiner pre_node = nodeExitMapper.getNode(queryForm);
             if(pre_node!=null){
                 NfcNodeMiner node = new NfcNodeMiner();
                 node.setNode_address(address);
                 node.setFractions(0);
                 node.setPledge_amount(add(pre_node.getPledge_amount(),new BigDecimal(fee)));
                 node.setSync_time(new Date());
                 nodeExitMapper.updateNodeMiner(node);
             }
         }else if(data.startsWith(NfcOperatorEnum.FlwReq.getEncode())){
             srcData = new String(Numeric.hexStringToByteArray(data.substring(NfcOperatorEnum.FlwReq.getEncode().length())));
             ufoParameter = srcData.substring(1);
             String [] split = ufoParameter.split(":");
             String miner_address = addressParse(split[0]);
             String line_type = bigNumberConvert(split[1]).toString();
             String bandwidth = bigNumberConvert(split[2]).toString();
             String type  = null;
             BigDecimal pledgeAmout = null;
             if(topics!=null&&topics.size()>0&&topics.get(0).equalsIgnoreCase(flwReqTopic)){
                 type = convertToBigDecimal(topics.get(2)).toString();
                 pledgeAmout =  convertToBigDecimal("0x"+logs.get(1).getData().substring(67,130));
             }else{
                 tx.setStatus(0);
                 return;
             }
             tx.dataBuild(miner_address, null, bandwidth,pledgeAmout==null?null:pledgeAmout.toString(),
                     line_type,type);
             //Data mining pledge
             NfcFlowMiner pre_miner = nfcFlowMinerMapper.getSingleMiner(miner_address);
             if(pre_miner!=null){
                 NfcFlowMiner miner = new NfcFlowMiner();
                 miner.setMiner_addr(miner_address);
                 miner.setSync_time(new Date());
                 miner.setMiner_status(MinerStatusEnum.mining.getCode());
                 miner.setLine_type(line_type);
                 miner.setBandwidth(new BigDecimal(bandwidth));
                 miner.setPledge_amount(add(pre_miner.getPledge_amount(),pledgeAmout));
                 miner.setBlocknumber(tx.getBlockNumber());
                 miner.setJoin_time(tx.getTimeStamp());
                 nfcFlowMinerMapper.updateFlowMiner(miner);
             }
         }else if(data.startsWith(NfcOperatorEnum.FlwExit.getEncode())){
             srcData = new String(Numeric.hexStringToByteArray(data.substring(NfcOperatorEnum.FlwExit.getEncode().length())));
             ufoParameter = srcData.substring(1);
             String miner_address = addressParse(ufoParameter.substring(0));
             String type  = null;
             if(topics!=null&&topics.size()>0&&topics.get(0).equalsIgnoreCase(pledgeExitTopic)){
                 type = convertToBigDecimal(topics.get(2)).toString();
             }else{
                 tx.setStatus(0);
                 return;
             }
             tx.dataBuild(miner_address,null,type,null,null,null);
             
		} else if (data.startsWith(NfcOperatorEnum.flwrpten.getEncode())) {	//TODO
			if (topics == null || topics.size() == 0 || !topics.get(0).equalsIgnoreCase(flwrptenTopic)) {
				tx.setStatus(0);
				return;
			}
			String dataString = new String(Numeric.hexStringToByteArray(logData.substring(2)));
			List<String> succIndex = Arrays.asList(dataString.split(","));

			srcData = new String(Numeric.hexStringToByteArray(data.substring(NfcOperatorEnum.flwrpten.getEncode().length())));
			ufoParameter = srcData.substring(1);
			String[] split = ufoParameter.split(":");
			String enid = split[0];
			String[] flows = split[1].split("\\|");
			String miner_addr = addressParse(tx.getFromAddr().substring(2));
			for (int i = 0; i < flows.length; i++) {
				String[] f = flows[i].split(",");
				if(f.length!=4){
					log.warn("get flow report data error: length!=4");
					continue;
				}
				Long report_number = Long.parseLong(f[0]);
				Long deviceid = Long.parseLong(f[1]);
				BigDecimal flow_value = new BigDecimal(f[2]);
				BigDecimal ful = flow_value.multiply(new BigDecimal(fulRate));								
				int status = succIndex.contains(i + "") ? 1 : 0;
				String client_addr = null;
				try{
					if(status==1){
						String rsv = f[3];
						//签名 v r s
						String toAddress = tx.getFromAddr().substring(2);
						String s = toAddress.toLowerCase(Locale.ROOT) + report_number + deviceid + flow_value;
	                    byte[] sig= Numeric.hexStringToByteArray(rsv);
	                    byte[] sr = new byte[32];
	                    System.arraycopy(sig,0,sr,0,32);
	                    byte[] ss = new byte[32];
	                    System.arraycopy(sig,32,ss,0,32);
	                    byte[] sv =new byte[1];
	                    System.arraycopy(sig,64,sv,0,1);
	                    BigInteger v=new BigInteger(sv);
	                    v=v.add(new BigInteger("27"));
	                    Sign.SignatureData signatureData = new Sign.SignatureData(v.toByteArray(), sr, ss);
	                    BigInteger publickey = Sign.signedMessageToKey(s.getBytes(StandardCharsets.UTF_8), signatureData);
	                    client_addr = addressParse(Keys.getAddress(publickey));
					}
				}catch(Exception e){
					status = 0;
					log.warn("parse flwrpten transaction data error",e);
				}
				
				FlowReport flowReport = new FlowReport();
				flowReport.setEnid(Long.parseLong(enid));
				flowReport.setMiner_addr(miner_addr);
				flowReport.setDeviceid(deviceid);
				flowReport.setClient_addr(client_addr);
				flowReport.setReport_number(report_number);
				flowReport.setFlow_value(flow_value);
				flowReport.setFul(ful);
				flowReport.setStatus(status);
				flowReport.setBlocknumber(tx.getBlockNumber());
				flowReport.setBlockhash(tx.getBlockHash());
				
				nfcFlowMinerMapper.saveOrUpdateFlowReport(flowReport);
			}
			tx.dataBuild(enid, null, null, null, null, null);
		}         
    }


    /**
     * SSC
     * @param tx
     */
    public  static  void ssc(String address1,String address2,String address3,Transaction tx,TransactionMapper transactionMapper, NfcFlowMinerMapper nfcFlowMinerMapper,List<Log> logs){
        String data = tx.getInput();
        String ufoParameter = null;
        String srcData = null;
        if(data.startsWith(SscOperatorEnum.ExchRate.getEncode())){
            if(!address1.equalsIgnoreCase(tx.getFromAddr())){
                tx.setStatus(0);
                return;
            }
            srcData = new String(Numeric.hexStringToByteArray(data.substring(SscOperatorEnum.ExchRate.getEncode().length())));
            ufoParameter = srcData.substring(1);
            tx.dataBuild(ufoParameter,null,null,null,null,null);
        }else if(data.startsWith(SscOperatorEnum.Deposit.getEncode())){
            if(!address2.equalsIgnoreCase(tx.getFromAddr())){
                tx.setStatus(0);
                return;
            }
            srcData = new String(Numeric.hexStringToByteArray(data.substring(SscOperatorEnum.Deposit.getEncode().length())));
            ufoParameter = srcData.substring(1);
            String [] split = ufoParameter.split(":");
            tx.dataBuild(bigNumberConvert(split[0]).toString(),split[1],null,null,null,null);
        }else if(data.startsWith(SscOperatorEnum.CndLock.getEncode())){
            if(!address2.equalsIgnoreCase(tx.getFromAddr())){
                tx.setStatus(0);
                return;
            }
            srcData = new String(Numeric.hexStringToByteArray(data.substring(SscOperatorEnum.CndLock.getEncode().length())));
            ufoParameter = srcData.substring(1);
            String [] split = ufoParameter.split(":");
            tx.dataBuild(bigNumberConvert(split[0]).toString(),
                    bigNumberConvert(split[1]).toString(),
                    bigNumberConvert(split[2]).toString(),null,null,null);
        }else if(data.startsWith(SscOperatorEnum.FlwLock.getEncode())){
            if(!address2.equalsIgnoreCase(tx.getFromAddr())){
                tx.setStatus(0);
                return;
            }
            srcData = new String(Numeric.hexStringToByteArray(data.substring(SscOperatorEnum.FlwLock.getEncode().length())));
            ufoParameter = srcData.substring(1);
            String [] split = ufoParameter.split(":");
            tx.dataBuild(bigNumberConvert(split[0]).toString(),
                    bigNumberConvert(split[1]).toString(),
                    bigNumberConvert(split[2]).toString(),null,null,null);
        }else if(data.startsWith(SscOperatorEnum.RwdLock.getEncode())){
            if(!address2.equalsIgnoreCase(tx.getFromAddr())){
                tx.setStatus(0);
                return;
            }
            srcData = new String(Numeric.hexStringToByteArray(data.substring(SscOperatorEnum.RwdLock.getEncode().length())));
            ufoParameter = srcData.substring(1);
            String [] split = ufoParameter.split(":");
            tx.dataBuild(bigNumberConvert(split[0]).toString(),
                    bigNumberConvert(split[1]).toString(),
                    bigNumberConvert(split[2]).toString(),null,null,null);
        }else if(data.startsWith(SscOperatorEnum.WdthPnsh.getEncode())){
            if(!address3.equalsIgnoreCase(tx.getFromAddr())){
                tx.setStatus(0);
                return;
            }
            srcData = new String(Numeric.hexStringToByteArray(data.substring(SscOperatorEnum.WdthPnsh.getEncode().length())));
            ufoParameter = srcData.substring(1);
            String [] split = ufoParameter.split(":");
            tx.dataBuild(addressParse(split[0]),null,bigNumberConvert(split[1]).toString(),null,null,null);
        }
    }

    /**
     * 40-byte address plus prefix
     */
    public static String addressParse(String address){
      //        return Constants.PRE + address;
    	return Constants.pre0XtoNX(address);
    }

    /**
     * 16 to 10
     * @param num
     * @return
     */
    public static BigInteger bigNumberConvert(String num){
        return  new BigInteger(num,16);
    }


    private static BigDecimal convertToBigDecimal(String value) {
        if (null != value)
            return new BigDecimal(Numeric.decodeQuantity(value));
        return null;
    }

    public static BigDecimal add(BigDecimal a,BigDecimal b ){
        if(a == null)
            return b;
        if(b == null)
            return a;
        return a.add(b);
    }

    public static void main(String[] args) {
        long secords=31536000;
        long blockintal=10;
        long blockreward=105000000;
        BigDecimal blocknum=new BigDecimal(secords/blockintal);
        for(int i=1;i<5;i++) {
            BigDecimal  tbn=new BigDecimal(blockreward*(1-Math.pow(0.5,i/6.0)));
            BigDecimal  tbn_1=new BigDecimal(blockreward*(1-Math.pow(0.5,(i-1)/6.0)));
            BigDecimal  b=tbn.subtract(tbn_1).divide(blocknum,20,BigDecimal.ROUND_HALF_UP);

        }
    }


}
