package com.imooc.mapper;

import com.baomidou.mybatisplus.core.MybatisMapperAnnotationBuilder;
import com.imooc.pojo.Form.BlockQueryForm;
import com.imooc.pojo.Form.LockUpTransferForm;
import com.imooc.pojo.Form.PledgeQueryForm;
import com.imooc.pojo.Form.TransferMinerForm;
import com.imooc.pojo.NfcFlowMiner;
import com.imooc.pojo.TransToken;
import com.imooc.pojo.TransferMiner;
import com.imooc.pojo.vo.BlockAndMinerVo;
import com.imooc.pojo.vo.TransferMinerVo;
import com.imooc.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface TransferMinerMapper extends MyMapper<TransferMiner> {

    List<TransferMiner> getAllMinerInfo(@Param("address")String address,@Param("types") Integer[] types);

    List<TransferMiner> getAllMinerForInfo(@Param("id")Long id, @Param("types")Integer[] types);

    BigDecimal getReward( @Param("types")Integer[] type);

    List<TransferMiner> getPledgeInfo(@Param("lockUpTransferForm")LockUpTransferForm lockUpTransferForm);

    List<TransferMiner> getPledgeInfos(@Param("paramMap")Map<String, Object> paramMap);

    List<BlockAndMinerVo> getBlockAndMinerInfo(@Param("current")Long pageCurrent,@Param("size") Long pageSize);

    long getTotalMinerInfo();

    List<TransferMinerVo> getMinerInfo(LockUpTransferForm lockUpTransferForm);

    List<TransferMinerVo> getMinerInfoNfc(@Param("current")long pageCurrent,@Param("size")long pageSize);

    long getTotalMiner();

    List<TransferMinerVo> getMinerInfoNfcMap(@Param("paramMap")Map<String, Object> map);

    long getTotalMinerMap(@Param("paramMap")Map<String, Object> map);

    List<TransferMinerVo> getMinerInfoNfcSerach(@Param("address")String address,@Param("current")long pageCurrent,@Param("size")long pageSize);

    long getTotalMinerSerach(@Param("address")String address);

    Long getPledgeInfoCount(@Param("lockUpTransferForm")LockUpTransferForm lockUpTransferForm);

    List<TransferMiner> getOverData(@Param("blockQueryForm")BlockQueryForm blockQueryForm);

    Long getTotalInfo(@Param("blockQueryForm")BlockQueryForm blockQueryForm);

    List<TransferMinerVo> getMinerInfoForNfc(@Param("blockQueryForm")BlockQueryForm blockQueryForm);

    Long getTotalMinerForNfc(@Param("blockQueryForm")BlockQueryForm blockQueryForm);

    TransferMinerVo getMinerSum(@Param("blockQueryForm")BlockQueryForm blockQueryForm);

    List<TransferMinerVo> getLockNfcMinersByRevAddress(@Param("blockQueryForm")BlockQueryForm blockQueryForm);

    Long getTotalLockNfcMinersByRevAddress(@Param("blockQueryForm")BlockQueryForm blockQueryForm);

    TransferMinerVo getLockSummaryByRevAddress(@Param("blockQueryForm")BlockQueryForm blockQueryForm);

    Long getTotalRealease(@Param("blockQueryForm")BlockQueryForm blockQueryForm);

    List<TransferMinerVo> getMinerInfoNfcSerachForNfc(@Param("blockQueryForm")BlockQueryForm blockQueryForm);

    Long getTotalMinerSerachForNfc(@Param("blockQueryForm")BlockQueryForm blockQueryForm);

    BigDecimal getAllTotalForReceive(@Param("address")String address);

    List<TransferMinerVo> getMinerInfoNfcSerachForWallet(@Param("blockQueryForm")BlockQueryForm blockQueryForm);

    Long getTotalMinerSerachForWallet(@Param("blockQueryForm")BlockQueryForm blockQueryForm);

    void insertOrUptate(@Param("item")TransToken item);

    void insertOrUpdateMiner(@Param("transaction")TransferMiner transferMiner);
    void insertBatch(@Param("list")List<TransferMiner> list);

    BigDecimal getTotalValueForTxHash(@Param("txHash")String txHash);

    List<TransferMinerVo> getMinerInfoForNfcTotal(@Param("blockQueryForm")BlockQueryForm blockQueryForm);

    List<TransferMiner> pagePledgeInfoList(@Param("address")String address,@Param("types") Integer[] types,@Param("current") long pageCurrent, @Param("size")long pageSize);

    TransferMiner getTransferMinerInfo(@Param("pledgeQueryForm")PledgeQueryForm pledgeQueryForm);

    Long getMaxBlockNumberTransfer(@Param("address")String address);

    List<TransferMiner> getMaxBlockNumberTransferGroupByAddress();

    List<TransferMiner> getTransferAdd(@Param("blocknumber")Long blocknumber,@Param("type")int type);

    void updateMiner(@Param("transferMiner")TransferMiner transferMiner);

    void updateMinerTransfRelaseBatch(@Param("list")List<TransferMiner> list);

    TransferMiner getMinerInfoAddress(@Param("transferMiner")TransferMiner transferMiner);

    List<TransferMiner> getTransferMinerList(@Param("transferMiner")TransferMiner transferMiner);

    TransferMiner getReleaseamountByAddress(@Param("address") String address);

    List<TransferMiner> getReleaseamountGroupByAddress();

    List<TransferMiner> getReleaseamountGroupByAddressAndType(@Param("type") int type);

    List<TransferMiner> getLockByType(@Param("transferMiner")TransferMiner transferMiner);

    long getDayFLowMaxBlockNumber();

}
