package com.imooc.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;

public class Constants {

    public static final Result STATUS_ACCOUNT_HASHERROR =Result.getResult("empty", 1001);

    public static final Result STATUS_PARAMSERROR =Result.getResult("Illegal non-existent parameter", 1002);

    public static final Result STATUS_BLOCK_RESULT =Result.getResult("Return result is empty", 1003);

    public static final Result STATUS_BLOCK_NUMBER_NOT_EXETS =Result.getResult("This block number does not exist", 1004);

    public static final Result STATUS_BLOCK_NUMBER =Result.getResult("Block height cannot exceed 20", 1005);

    public static long TimestampFactor = 1000;

    public static String LatestBlockNumberKey = "latest";

    public static BigInteger GWeiFactor = BigInteger.valueOf(10).pow(9);

    public static String URL="http://api-ropsten.etherscan.io/api";

    public static String APIKEY="802601ffe1a741088783f6a508990b40";

    public static final long minTime=59;

    public static final long maxTime=61;

    public static final long mill=1000;

    public static final String  PRE_0X = "0x";//0x
    
    public static final String  PRE_NX = "NX";//NX    

    public static final String  PRE = PRE_0X;

    //ufoVersion
    public static final String ufoVersion = "1";

    public static final Integer MUL_W = 10000;

    //NODE_PLEDGE_AMOUNT
//    public static final BigInteger NODE_PLEDGE_AMOUNT = new BigInteger("36000000000000000000");

    public static final int TOTALMBPOINT = 100;
    //batch size
    public static final int  BATCHCOUNT = 100;
    //1M ->ful 0.0000751953125
    public static final String M_FUl = "0.000013671875";
    //decimals
    public static final String decimals = "1000000000000000000";
    
    public static final String addressZero = PRE + "0000000000000000000000000000000000000000";
    /*
    0x->NX
     */
    public  static String pre0XtoNX(String str){
        if(StringUtils.isEmpty(str)){
            return str;
        }
        if(str.substring(0,2).equalsIgnoreCase(Constants.PRE_NX) || str.substring(0,2).equalsIgnoreCase(Constants.PRE_0X)){
            return PRE + str.substring(2).toLowerCase();
        }else {
            return PRE + str.toLowerCase();
        }
    }
}
