package com.imooc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imooc.pojo.Form.BlockQueryForm;
import com.imooc.pojo.Form.TokenQueryForm;
import com.imooc.pojo.TokenContract;
import com.imooc.pojo.Tokens;
import com.imooc.pojo.TransToken;
import com.imooc.utils.ResultMap;
import org.apache.ibatis.annotations.Param;

public interface TokenService {
    ResultMap getTransferListByCoinType(int coinType);

    ResultMap getTotalSupply(String contractAddress);

    ResultMap<Page<Tokens>> getTokenList(BlockQueryForm blockQueryForm);

    ResultMap<Page<TransToken>> getTransTokenListForContract(@Param("blockQueryForm")BlockQueryForm blockQueryForm);

    ResultMap <Tokens>getContractMap(@Param("blockQueryForm")BlockQueryForm blockQueryForm);

    ResultMap getTokenIsExitCount(TokenQueryForm tokenQueryForm);

    ResultMap<Page<TokenContract>> getTokenContractList(@Param("blockQueryForm")BlockQueryForm blockQueryForm);
}
