package com.imooc.mapper;

import com.imooc.pojo.Form.BlockQueryForm;
import com.imooc.pojo.Form.QueryForm;
import com.imooc.pojo.vo.AddressVo;
import com.imooc.pojo.vo.NfcExchgList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NfcExchgListMapper {

    List<NfcExchgList> getContractPageTokenList(@Param("queryForm") QueryForm queryForm);

    long getContractTokenCount(@Param("queryForm")QueryForm queryForm);

    double statisLock(NfcExchgList nfc);
}
