package com.imooc.mapper;

import com.imooc.pojo.Form.BlockQueryForm;
import com.imooc.pojo.TransToken;
import com.imooc.utils.MyMapper;
import com.imooc.utils.ResultMap;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TransferTokenMapper extends MyMapper<TransToken> {

    ResultMap getTokenSupply(@Param("contractAddress")String contractAddress);

    List<TransToken> getTransferList(@Param("coinType")long coinType);

    List<TransToken> getTransToken(@Param("blockQueryForm") BlockQueryForm blockQueryForm);

    void insertOrUptate(@Param("transaction") TransToken item);

    List<TransToken> getTransactionByTxHash(@Param("txHash")String txHash);

    long getContractCount(@Param("contractAddress")String contractAddress);
}
