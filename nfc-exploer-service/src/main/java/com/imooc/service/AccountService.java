package com.imooc.service;



import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imooc.pojo.Addresses;
import com.imooc.pojo.Form.BlockQueryForm;
import com.imooc.pojo.vo.AddressVo;
import com.imooc.utils.ResultMap;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;
import org.web3j.abi.datatypes.Address;

public interface AccountService {
    ResultMap getSingleAddressBalance(String address);

    ResultMap getbalancemulti(String address);

    ResultMap<Page<AddressVo>> pageList(BlockQueryForm blockQueryForm);

   // ResultMap<Page<Addresses>> getContractPageList(BlockQueryForm blockQueryForm);

    ResultMap<Page<Addresses>> getContractPageTokenList(BlockQueryForm blockQueryForm);

    ResultMap  getAddressVoByaddress(String address);
    
    
    BigDecimal getAddressBalance(String addressHash); 
}
