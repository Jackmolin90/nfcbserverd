package com.imooc.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imooc.mapper.AccountMapper;
import com.imooc.mapper.NfcExchgListMapper;
import com.imooc.mapper.TransactionMapper;
import com.imooc.pojo.Form.QueryForm;
import com.imooc.pojo.Transaction;
import com.imooc.pojo.vo.AddressVo;
import com.imooc.pojo.vo.NfcExchgList;
import com.imooc.service.NfcExchgListService;
import com.imooc.utils.ResultMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NfcExchgListServiceImpl implements NfcExchgListService {

    @Autowired
    private NfcExchgListMapper mapper;

    @Autowired
    private TransactionMapper transactionMapper;

    @Override
    public ResultMap<Page<NfcExchgList>> getContractPageTokenList(QueryForm queryForm) {
        Page page = new Page();
        page.setCurrent(queryForm.getCurrent() == null?1:queryForm.getCurrent());  //当前页
        page.setSize(queryForm.getSize() == null?15:queryForm.getSize());         //每页条数
        queryForm.setIndex((page.getCurrent()-1)*page.getSize());
        List<NfcExchgList> listInfo =mapper.getContractPageTokenList(queryForm);
        long count =mapper.getContractTokenCount(queryForm);
        page.setRecords(listInfo);
        page.setTotal(count);
        return ResultMap.getSuccessfulResult(page);
    }

    //合约地址
    @Value("${contractAddress}")
    private String contractAddress;

    @Override
    public ResultMap statisLock(QueryForm queryForm) {
        NfcExchgList nfc = new NfcExchgList();
        nfc.setExchgIpaddr(queryForm.getQuery());
        Double num = mapper.statisLock(nfc);
        Transaction tran = new Transaction();
        tran.setFromAddr(queryForm.getQuery());
        tran.setToAddr(contractAddress);
        Double num2 = transactionMapper.statisLock(tran);
        Map<String,Object> result = new HashMap<>();
        result.put("locak",num);        //锁仓
        result.put("extract",num2);     //提取
        return ResultMap.getSuccessfulResult(result);
    }
}
