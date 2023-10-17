package com.imooc.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;


@Component
public class Web3jChildUtils {
    private static final Logger logger =LoggerFactory.getLogger(Web3jChildUtils.class);

    private static  Web3j web3jChild =null;

    private static synchronized  Web3j init(String ipaddress){
        if(!ObjectUtils.isEmpty(web3jChild)){
            return web3jChild;
        }
        web3jChild =Web3j.build((new HttpService(ipaddress)));
        return web3jChild;
    }


    public static Web3j getWeb3j(String ipaddress){
        if(!ObjectUtils.isEmpty(web3jChild)){
            return web3jChild;
        }
        return  init(ipaddress);
    }


    /*获取地址余额*/  /*已测试ok   传参数区块高度最新*/
    public BigInteger getAddressBalance(Web3j web3j, String address) {
        EthGetBalance ethResult;
        try {
            ethResult = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        if (null == ethResult) {
            return null;
        }
        return ethResult.getBalance();
    }

    public BigInteger getAddressNonce(Web3j web3j, String address) {
        EthGetTransactionCount ethResult;
        try {
            ethResult = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).send();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return ethResult.getTransactionCount();
    }

    /**
     * @param web3j
     * @return
     */
    public  Long getCurrentBlockNumber(Web3j web3j) throws Exception{
        EthBlockNumber ethBlockNumber;
        try {
            ethBlockNumber = web3j.ethBlockNumber().send();
        } catch (IOException e) {
            throw new Exception("getCurrentBlockNumber error:"+e);
        }
        if (null == ethBlockNumber) {
            return 0l;
        }
        BigInteger number = ethBlockNumber.getBlockNumber();
        if (null == number) {
            return 0l;
        }
        return number.longValue();
    }

    /**
     * @param web3j
     * @param transHash
     * @return
     */
    public TransactionReceipt getTransactionReceipt(Web3j web3j, String transHash)throws Exception {
        EthGetTransactionReceipt ethReceipt;
        try {
            ethReceipt = web3j.ethGetTransactionReceipt(transHash).send();
        } catch (Exception e) {
            throw new Exception("getCurrentBlockNumber error:"+e);
        }
        if (null == ethReceipt) {
            return null;
        }
        TransactionReceipt receipt = ethReceipt.getResult();
        if (null == receipt) {
            return null;
        }
        return receipt;
    }




}
