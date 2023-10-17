package com.imooc.mapper;

import com.imooc.pojo.AddrBalances;
import com.imooc.pojo.BlockOverView;

import java.math.BigDecimal;

public interface OverViewMapper {

    BlockOverView getFwMinerData();
    BlockOverView getNodeMinerData();
    BlockOverView getCurrLockData();
    BlockOverView getnfctogbData();
    BlockOverView getLastMintage(long blocknumber);
    BigDecimal getBwPdgRnge300();
    BigDecimal getBwPdgRnge380();
    BigDecimal getBwPdgRnge815();
    BigDecimal getBwPdgRnge1500();
}
