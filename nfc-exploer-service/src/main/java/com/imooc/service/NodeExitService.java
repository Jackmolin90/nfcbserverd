package com.imooc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imooc.pojo.Form.BlockQueryForm;
import com.imooc.pojo.Form.NfcNodeMinerQueryForm;
import com.imooc.pojo.NfcNodeMiner;
import com.imooc.pojo.NodeExit;
import com.imooc.utils.ResultMap;

public interface NodeExitService {
    ResultMap<NodeExit> getNodeExit(BlockQueryForm blockQueryForm);

    ResultMap<Page<NfcNodeMiner>> getNodeList(NfcNodeMinerQueryForm nfcNodeMinerVo);

    ResultMap getNode( NfcNodeMinerQueryForm nfcNodeMinerVo);

    ResultMap getNodeByRev( NfcNodeMinerQueryForm nfcNodeMinerVo);


    ResultMap getNodePledgeAmount( NfcNodeMinerQueryForm nfcNodeMinerVo) throws  Exception;

    ResultMap getNodeExistRealse( NfcNodeMinerQueryForm nfcNodeMinerVo);
}
