package com.imooc.mapper;

import com.imooc.pojo.NfcFaucet;
import com.imooc.pojo.vo.AddressVo;
import com.imooc.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

public interface NfcFaucetMapper extends MyMapper<NfcFaucet> {

    NfcFaucet getFaucetByAddress(@Param("address")String address);
}
