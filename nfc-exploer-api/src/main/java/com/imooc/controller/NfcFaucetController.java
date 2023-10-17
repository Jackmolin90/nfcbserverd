package com.imooc.controller;

import com.imooc.service.NfcFaucetService;
import com.imooc.service.TokenService;
import com.imooc.utils.ResultMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.http.HttpService;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;


@CrossOrigin
@RestController
@Api(value = "NfcFaucetController", tags = "NfcFaucetController")
@RequestMapping("/faucet")
public class NfcFaucetController {
    private Logger logger = LoggerFactory.getLogger(NfcFaucetController.class);
    @Value("${ipaddress}")
    private  String ipaddress;

    @Autowired
    private NfcFaucetService nfcFaucetService;

    @ApiOperation(value = "getFaucetByAddress", notes = "getFaucetByAddress")
    @ApiImplicitParams({})
    @PostMapping("/getFaucetByAddress")
    public ResultMap getFaucetByAddress(@RequestParam(value="address")String address) throws Exception {
        return  nfcFaucetService.getFaucetByAddress(address);
    }


    @ApiOperation(value = "sendCoinToAddress", notes = "sendCoinToAddress")
    @ApiImplicitParams({})
    @PostMapping("/sendCoinToAddress")
    public ResultMap sendCoinToAddress(@RequestParam(value="address")String address) throws Exception {
        return  nfcFaucetService.sendCoinToAddress(address);
    }

}
