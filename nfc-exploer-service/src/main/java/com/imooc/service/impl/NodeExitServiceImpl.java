package com.imooc.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imooc.enums.NodeTypeEnum;
import com.imooc.mapper.NodeExitMapper;
import com.imooc.mapper.TransactionMapper;
import com.imooc.mapper.TransferMinerMapper;
import com.imooc.pojo.Form.BlockQueryForm;
import com.imooc.pojo.Form.NfcNodeMinerQueryForm;
import com.imooc.pojo.NfcNodeMiner;
import com.imooc.pojo.NodeExit;
import com.imooc.pojo.Punished;
import com.imooc.pojo.Transaction;
import com.imooc.pojo.vo.TransferMinerVo;
import com.imooc.service.NodeExitService;
import com.imooc.utils.Constants;
import com.imooc.utils.ResultMap;
import com.imooc.utils.SscOperatorEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NodeExitServiceImpl implements NodeExitService {
    @Autowired
    private NodeExitMapper nodeExitMapper;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private TransferMinerMapper transferMinerMapper;
    @Override
    public ResultMap<NodeExit> getNodeExit(BlockQueryForm blockQueryForm) {
        Page page = blockQueryForm.newFormPage();
        Long pageSize=page.getSize();
        Long pageCurrent =page.getCurrent();
        pageCurrent=(pageCurrent-1)*pageSize;
        blockQueryForm.setCurrent(pageCurrent);
        blockQueryForm.setSize(pageSize);
        List<Punished> listInfo =nodeExitMapper.getPageList(blockQueryForm);
        long total=nodeExitMapper.getTotal();
        page.setRecords(listInfo);
        page.setTotal(total);
        return ResultMap.getSuccessfulResult(page);
    }

    @Override
    public ResultMap<Page<NfcNodeMiner>> getNodeList(NfcNodeMinerQueryForm nfcNodeMinerVo) {
        Page page = nfcNodeMinerVo.newFormPage();
        Long pageSize=page.getSize();
        Long pageCurrent =page.getCurrent();
        pageCurrent=(pageCurrent-1)*pageSize;
        nfcNodeMinerVo.setCurrent(pageCurrent);
        nfcNodeMinerVo.setSize(pageSize);
        List<NfcNodeMiner> listInfo =nodeExitMapper.getNodePageList(nfcNodeMinerVo);
        long total=nodeExitMapper.getNodeTotal(nfcNodeMinerVo);
        if(total>0){
            if(nfcNodeMinerVo.getNode_type()==3){  //Exit node
                for (NfcNodeMiner node: listInfo) {
                    node.setLeftamount(nodeExitMapper.getLeftamount(node.getNode_address(),3));
                }
            }
            page.setRecords(listInfo);
            page.setTotal(total);
            return ResultMap.getSuccessfulResult(page);
        }else{
            return ResultMap.getSuccessfulResult(null);
        }

    }

    @Override
    public ResultMap getNode(NfcNodeMinerQueryForm nfcNodeMinerVo) {
        NfcNodeMiner node = nodeExitMapper.getNode(nfcNodeMinerVo);
        if(node==null){
            node = new NfcNodeMiner();
        }
        BlockQueryForm blockQueryForm = new BlockQueryForm();
        blockQueryForm.setAddress(nfcNodeMinerVo.getNode_address());
        blockQueryForm.setType(1);
        TransferMinerVo tm = transferMinerMapper.getMinerSum(blockQueryForm);
        if(tm!=null){
            node.setLockamount(tm.getTotalAmount());
            node.setReleaseamount(tm.getReleaseamount());
        }
        blockQueryForm.setType(3);
        TransferMinerVo tm2 = transferMinerMapper.getMinerSum(blockQueryForm);
        if(tm2!=null){
            node.setExitlockamount(tm2.getTotalAmount());
            node.setExitreleaseamount(tm2.getReleaseamount());
        }
        return  ResultMap.getSuccessfulResult(node);
    }

    public ResultMap getNodeByRev( NfcNodeMinerQueryForm nfcNodeMinerVo){
        return  ResultMap.getSuccessfulResult(nodeExitMapper.getNodeByRev(nfcNodeMinerVo));
    }

    public   ResultMap getNodeExistRealse( NfcNodeMinerQueryForm nfcNodeMinerVo){
        return  ResultMap.getSuccessfulResult(nodeExitMapper.getNodeExistRealse(nfcNodeMinerVo.getAddress()));
    }

    @Override
    public ResultMap getNodePledgeAmount(NfcNodeMinerQueryForm nfcNodeMinerVo) throws  Exception {
        List<Transaction> deposit = transactionMapper.getTransactionByTxType(SscOperatorEnum.Deposit.getCode());
        String pledge = null;
        if(deposit.size()>0){
            pledge =  deposit.get(0).getParam1();
        }else{
            throw  new Exception("no Deposit config set");
        }
        Map<String ,Object> m = new HashMap<>();
        m.put("TOTALMBPOINT",Constants.TOTALMBPOINT);
        m.put("NODE_PLEDGE_AMOUNT",pledge);
        return ResultMap.getSuccessfulResult(m);
//        if(StringUtils.isEmpty(nfcNodeMinerVo.getNode_address())){
//            return ResultMap.getSuccessfulResult(m);
//        }
//        NfcNodeMiner node = nodeExitMapper.getNode(nfcNodeMinerVo);
//        if(node!=null&&node.getNode_type()!=NodeTypeEnum.exit.getCode()){
//            m.put("TOTALMBPOINT",Constants.TOTALMBPOINT);
//            m.put("NODE_PLEDGE_AMOUNT",pledge);
//            return ResultMap.getSuccessfulResult(m);
//        }else {
//            return ResultMap.getSuccessfulResult(m);
//        }
    }


}
