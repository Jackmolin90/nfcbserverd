package com.imooc.mapper;

import com.imooc.pojo.Form.BlockQueryForm;
import com.imooc.pojo.Form.NfcNodeMinerQueryForm;
import com.imooc.pojo.NfcFlowMiner;
import com.imooc.pojo.NfcNodeMiner;
import com.imooc.pojo.NodeExit;
import com.imooc.pojo.Punished;
import com.imooc.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface NodeExitMapper extends MyMapper<NodeExit> {

    List<Punished> getPageList(@Param("blockQueryForm") BlockQueryForm blockQueryForm);

    long getTotal();

    List<NfcNodeMiner> getNodePageList(@Param("nfcNodeMinerQueryForm") NfcNodeMinerQueryForm nfcNodeMinerQueryForm);

    long getNodeTotal(@Param("nfcNodeMinerQueryForm") NfcNodeMinerQueryForm nfcNodeMinerQueryForm);

    NfcNodeMiner getNode(@Param("nfcNodeMinerQueryForm") NfcNodeMinerQueryForm nfcNodeMinerQueryForm);

    long getNodeByRev(@Param("nfcNodeMinerQueryForm") NfcNodeMinerQueryForm nfcNodeMinerQueryForm);

    long getNodeExistRealse(@Param("address") String address);

    void saveOrUpdateNodeMiner(@Param("nfcNodeMiner") NfcNodeMiner nfcNodeMiner);

    void updateNodeMiner(@Param("nfcNodeMiner") NfcNodeMiner nfcNodeMiner);

    void updateNodeStatus(@Param("nfcNodeMiner") NfcNodeMiner nfcNodeMiner);

    List<NfcNodeMiner> getNodeListNotExit();

    void updateNodeBatch(@Param("list") List<String> list);

    BigDecimal getLeftamount(@Param("address") String  address,@Param("type")int type);
}
