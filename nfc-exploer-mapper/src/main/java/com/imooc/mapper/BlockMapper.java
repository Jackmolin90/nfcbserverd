package com.imooc.mapper;

import com.imooc.pojo.Blocks;
import com.imooc.pojo.Form.DposVoterQueryForm;
import com.imooc.pojo.vo.BlockRewardVo;
import com.imooc.pojo.vo.BlocksVo;
import com.imooc.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface BlockMapper  extends MyMapper<Blocks> {
    List<Blocks> getBeforeBlocks(@Param("timeStp")String timeStp);

    List<Blocks> getAfterBlocks(@Param("timeStp")String timeStp);

    long getLeatestBlockNumber();

    List<BlocksVo> pageList(@Param("pageCurrent")Long pageCurrent, @Param("pageSize") Long pageSize, @Param("startNumber")Long startNumber,
                            @Param("address")String address);

    long getTotalCount(@Param("address")String address);

    List<BlocksVo> getBlockInfoByNumber(@Param("blockNumber")Long blockNumber);

    Blocks getminBlock(@Param("blockNumber")long blockNumber);

    long getCountByThisYear(@Param("dateTime")String dateTime);

    void insertOrUpdate(@Param("accounts")Blocks item);

    List<BlockRewardVo> getAllRewardForaddress(@Param("dposVoterQueryForm")DposVoterQueryForm dposVoterQueryForm);

    long getTotalRewardCount(@Param("dposVoterQueryForm")DposVoterQueryForm dposVoterQueryForm);
}
