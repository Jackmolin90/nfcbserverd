package com.imooc.service;


import com.imooc.utils.ResultMap;

public interface NfcFaucetService {

    ResultMap getFaucetByAddress(String address);


    ResultMap sendCoinToAddress(String address);
}
