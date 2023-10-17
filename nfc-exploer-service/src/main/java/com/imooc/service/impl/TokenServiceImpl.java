package com.imooc.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imooc.mapper.*;
import com.imooc.pojo.Form.BlockQueryForm;
import com.imooc.pojo.Form.TokenQueryForm;
import com.imooc.pojo.TokenContract;
import com.imooc.pojo.Tokens;
import com.imooc.pojo.TransToken;
import com.imooc.service.TokenService;
import com.imooc.utils.ResultMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TokenServiceImpl implements TokenService {
   @Autowired
   private TransferTokenMapper transferTokenMapper;

   @Autowired
   private TokenMapper tokenMapper;

   @Autowired
   private AccountMapper accountMapper;

   @Autowired
   private ContractTokenMapper contractTokenMapper;

    private static final Logger logger= LoggerFactory.getLogger(TokenServiceImpl.class);

    public ResultMap getTransferListByCoinType(int coinType) {
        List<TransToken> listInfo =transferTokenMapper.getTransferList(coinType);
        return ResultMap.getSuccessfulResult(listInfo);
    }

    public ResultMap getTotalSupply(String contractAddress) {
       List<Tokens> listInfo =tokenMapper.getTokenSupply(contractAddress);
       return ResultMap.getSuccessfulResult(listInfo);
    }

    @Override
    public ResultMap<Page<Tokens>> getTokenList(BlockQueryForm blockQueryForm) {
        Page page = blockQueryForm.newFormPage();
        Long pageSize=page.getSize();
        Long pageCurrent =page.getCurrent();
        pageCurrent=(pageCurrent-1)*pageSize;
        blockQueryForm.setCurrent(pageCurrent);
        blockQueryForm.setSize(pageSize);
        List<Tokens> list = tokenMapper.getTokenListInfo(blockQueryForm);
        for(Tokens tokens:list){
            String contractAddress =tokens.getContract();
            long count =accountMapper.getContractTokenCount(contractAddress);
            tokens.setAccountTotal(count);
        }
        long total=tokenMapper.getTotalTokens(blockQueryForm);
        page.setRecords(list);
        page.setTotal(total);
        return ResultMap.getSuccessfulResult(page);
    }

    @Override
    public ResultMap<Page<TransToken>> getTransTokenListForContract(BlockQueryForm blockQueryForm) {
        Page page = blockQueryForm.newFormPage();
        Long pageSize=page.getSize();
        Long pageCurrent =page.getCurrent();
        pageCurrent=(pageCurrent-1)*pageSize;
        blockQueryForm.setCurrent(pageCurrent);
        blockQueryForm.setSize(pageSize);
        List<TransToken> listInfo =transferTokenMapper.getTransToken(blockQueryForm);
        page.setRecords(listInfo);
        page.setTotal(1);
        return ResultMap.getSuccessfulResult(page);
    }

    @Override
    public ResultMap <Tokens>getContractMap(BlockQueryForm blockQueryForm) {
        String contract=blockQueryForm.getAddress();
        Tokens token = tokenMapper.getTokens(contract);
        long count =accountMapper.getContractCount(contract);
        if(token !=null){
            token.setAccountTotal(count);
        }
        return ResultMap.getSuccessfulResult(token);
    }

    @Override
    public ResultMap getTokenIsExitCount(TokenQueryForm tokenQueryForm) {
        long count =tokenMapper.getCountForExit(tokenQueryForm);
        return ResultMap.getSuccessfulResult(count);
    }

    @Override
    public ResultMap<Page<TokenContract>> getTokenContractList(BlockQueryForm blockQueryForm) {
        Page page = blockQueryForm.newFormPage();
        Long pageSize=page.getSize();
        Long pageCurrent =page.getCurrent();
        pageCurrent=(pageCurrent-1)*pageSize;
        blockQueryForm.setCurrent(pageCurrent);
        blockQueryForm.setSize(pageSize);
        List<TokenContract>list =contractTokenMapper.getTokenContract(blockQueryForm);
        for(TokenContract tokens:list){
            String contractAddress =tokens.getContractAddress();
            long count =transferTokenMapper.getContractCount(contractAddress);
          //  long count =accountMapper.getContractCount(contractAddress);
            tokens.setAccountTotal(count);
        }
        long total =contractTokenMapper.getTotal(blockQueryForm);
        page.setRecords(list);
        page.setTotal(total);
        return ResultMap.getSuccessfulResult(page);
    }
}
