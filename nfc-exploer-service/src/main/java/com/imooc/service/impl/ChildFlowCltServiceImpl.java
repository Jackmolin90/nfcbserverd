package com.imooc.service.impl;

import com.imooc.enums.ConstantEnum;
import com.imooc.mapper.NfcFlowMinerMapper;
import com.imooc.mapper.SyncChildBlocknumberMapper;
import com.imooc.pojo.NfcCltFlwdata;
import com.imooc.pojo.SyncChildBlocknumber;
import com.imooc.service.ChildFlowCltService;
import com.imooc.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhc 2021-12-08 10:31
 */
@Service
public class ChildFlowCltServiceImpl implements ChildFlowCltService {
    @Autowired
    private SyncChildBlocknumberMapper syncMapper;

    @Autowired
    private NfcFlowMinerMapper nfcFlowMinerMapper;
    
    @Transactional(rollbackFor = Exception.class)
    public void saveCltDB(List<NfcCltFlwdata> cltList,SyncChildBlocknumber syncHeight) throws Exception {
        try {
            if(cltList.size()>0){
                if(cltList.size()<= Constants.BATCHCOUNT){
                    nfcFlowMinerMapper.batchSaveFlwDataDetail(cltList);
                }else{
                    List<NfcCltFlwdata> list  = new ArrayList<>();
                    for (NfcCltFlwdata flw:cltList) {
                        list.add(flw);
                        if(list.size()>=Constants.BATCHCOUNT){
                            nfcFlowMinerMapper.batchSaveFlwDataDetail(list);
                            list  = new ArrayList<>();
                        }
                    }
                    if(list.size()>0){
                        nfcFlowMinerMapper.batchSaveFlwDataDetail(list);
                    }
                }
            }
            syncMapper.updateByPrimaryKey(syncHeight);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
