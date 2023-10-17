package com.imooc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imooc.pojo.Addresses;
import com.imooc.pojo.Form.BlockQueryForm;
import com.imooc.pojo.Form.QueryForm;
import com.imooc.pojo.vo.NfcExchgList;
import com.imooc.utils.ResultMap;

public interface NfcExchgListService {

    ResultMap<Page<NfcExchgList>> getContractPageTokenList(QueryForm queryForm);

    ResultMap statisLock(QueryForm queryForm);
}
