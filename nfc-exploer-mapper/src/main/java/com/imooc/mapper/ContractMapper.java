package com.imooc.mapper;

import com.imooc.pojo.Contract;
import com.imooc.pojo.Contractverified;
import com.imooc.pojo.Form.BlockQueryForm;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ContractMapper {
	
	Contract getContract(@Param("contract")String contract);

    void saveOrUpdate(@Param("contract")Contract contract);
    
    void updateVerify(@Param("contract")Contract contract);

    List<Contract> getPageList(@Param("blockQueryForm")BlockQueryForm blockQueryForm);

    long getTotal(@Param("blockQueryForm")BlockQueryForm blockQueryForm);
    
    List<Contract> getAuthorizedContracts(@Param("address") String address);
    
    Contract getAuthorizedContract(@Param("contract")String contract,@Param("address") String address);
    
    
    
    
    List<Contractverified> getContractInfo(@Param("contractAddress")String address);
}
