package com.imooc.service;

import com.imooc.pojo.NfcCltFlwdata;
import com.imooc.pojo.SyncChildBlocknumber;

import java.util.List;

/**
 * @author zhc 2021-12-08 10:31
 */
public interface ChildFlowCltService {

    public void saveCltDB(List<NfcCltFlwdata> cltList,SyncChildBlocknumber syncHeight)throws Exception;
}
