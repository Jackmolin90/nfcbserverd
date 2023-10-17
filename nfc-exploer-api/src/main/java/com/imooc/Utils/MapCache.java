package com.imooc.Utils;

import com.imooc.pojo.vo.TransactionVo;
import com.imooc.utils.Constants;
import com.imooc.utils.NfcOperatorEnum;
import com.imooc.utils.SscOperatorEnum;
import com.imooc.utils.UfoPrefixEnum;

import java.util.HashMap;
import java.util.Map;

/**
 *  mapcache
 * @author zhc 2021-09-08 15:31
 */
public class MapCache {

    public  static Map<String,TransactionVo> ufoPreMap = new HashMap<>();
    static {
        loadMap();
    }

    /**
     * NFC SSC
     */
    public  static  void loadMap(){
        //NFC
        ufoPreMap.put(NfcOperatorEnum.Exch.getEncode(),new TransactionVo(UfoPrefixEnum.NFC.getCode(), Constants.ufoVersion,NfcOperatorEnum.Exch.getCode()));
        ufoPreMap.put(NfcOperatorEnum.Bind.getEncode(),new TransactionVo(UfoPrefixEnum.NFC.getCode(),Constants.ufoVersion,NfcOperatorEnum.Bind.getCode()));
        ufoPreMap.put(NfcOperatorEnum.Unbind.getEncode(),new TransactionVo(UfoPrefixEnum.NFC.getCode(),Constants.ufoVersion,NfcOperatorEnum.Unbind.getCode()));
        ufoPreMap.put(NfcOperatorEnum.Rebind.getEncode(),new TransactionVo(UfoPrefixEnum.NFC.getCode(),Constants.ufoVersion,NfcOperatorEnum.Rebind.getCode()));
        ufoPreMap.put(NfcOperatorEnum.CandReq.getEncode(),new TransactionVo(UfoPrefixEnum.NFC.getCode(),Constants.ufoVersion,NfcOperatorEnum.CandReq.getCode()));
        ufoPreMap.put(NfcOperatorEnum.CandExit.getEncode(),new TransactionVo(UfoPrefixEnum.NFC.getCode(),Constants.ufoVersion,NfcOperatorEnum.CandExit.getCode()));
        ufoPreMap.put(NfcOperatorEnum.CandPnsh.getEncode(),new TransactionVo(UfoPrefixEnum.NFC.getCode(),Constants.ufoVersion,NfcOperatorEnum.CandPnsh.getCode()));
        ufoPreMap.put(NfcOperatorEnum.FlwReq.getEncode(),new TransactionVo(UfoPrefixEnum.NFC.getCode(),Constants.ufoVersion,NfcOperatorEnum.FlwReq.getCode()));
        ufoPreMap.put(NfcOperatorEnum.FlwExit.getEncode(),new TransactionVo(UfoPrefixEnum.NFC.getCode(),Constants.ufoVersion,NfcOperatorEnum.FlwExit.getCode()));
        ufoPreMap.put(NfcOperatorEnum.flwrpten.getEncode(),new TransactionVo(UfoPrefixEnum.NFC.getCode(),Constants.ufoVersion,NfcOperatorEnum.flwrpten.getCode()));
        //SSC
        ufoPreMap.put(SscOperatorEnum.ExchRate.getEncode(),new TransactionVo(UfoPrefixEnum.SSC.getCode(),Constants.ufoVersion,SscOperatorEnum.ExchRate.getCode()));
        ufoPreMap.put(SscOperatorEnum.Deposit.getEncode(),new TransactionVo(UfoPrefixEnum.SSC.getCode(),Constants.ufoVersion,SscOperatorEnum.Deposit.getCode()));
        ufoPreMap.put(SscOperatorEnum.CndLock.getEncode(),new TransactionVo(UfoPrefixEnum.SSC.getCode(),Constants.ufoVersion,SscOperatorEnum.CndLock.getCode()));
        ufoPreMap.put(SscOperatorEnum.FlwLock.getEncode(),new TransactionVo(UfoPrefixEnum.SSC.getCode(),Constants.ufoVersion,SscOperatorEnum.FlwLock.getCode()));
        ufoPreMap.put(SscOperatorEnum.RwdLock.getEncode(),new TransactionVo(UfoPrefixEnum.SSC.getCode(),Constants.ufoVersion,SscOperatorEnum.RwdLock.getCode()));
        ufoPreMap.put(SscOperatorEnum.OffLine.getEncode(),new TransactionVo(UfoPrefixEnum.SSC.getCode(),Constants.ufoVersion,SscOperatorEnum.OffLine.getCode()));
        ufoPreMap.put(SscOperatorEnum.QOS.getEncode(),new TransactionVo(UfoPrefixEnum.SSC.getCode(),Constants.ufoVersion,SscOperatorEnum.QOS.getCode()));
        ufoPreMap.put(SscOperatorEnum.WdthPnsh.getEncode(),new TransactionVo(UfoPrefixEnum.SSC.getCode(),Constants.ufoVersion,SscOperatorEnum.WdthPnsh.getCode()));
    }

    public static  TransactionVo getValue(String data){
        if(null == data){
            return null;
        }
        for (Map.Entry<String,TransactionVo> entry : ufoPreMap.entrySet()) {
            if(data.startsWith(entry.getKey())){
                return entry.getValue();
            }
        }
       return null;
    }
}
