package com.imooc.controller.job;

import com.imooc.Utils.AgentSvcTask;
import com.imooc.Utils.TransferAddUtil;
import com.imooc.mapper.NfcFlowMinerMapper;
import com.imooc.mapper.TransferMinerMapper;
import com.imooc.pojo.Addresses;
import com.imooc.pojo.TransferMiner;
import com.imooc.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * There is one record for the release of data
 * @author zhc 2021-11-18 13:56
 */
public class TransferAddJob extends AgentSvcTask {
    private static Logger logger = LoggerFactory.getLogger(TransferAddJob.class);
    private NfcFlowMinerMapper nfcFlowMinerMapper;
    private TransferMinerMapper transferMinerMapper;
    private Long blockNumber;
    private Date yesterday;

    public TransferAddJob(Date yesterday,Long blockNumber,NfcFlowMinerMapper nfcFlowMinerMapper, TransferMinerMapper transferMinerMapper) {
        this.yesterday = yesterday;
        this.blockNumber = blockNumber;
        this.nfcFlowMinerMapper = nfcFlowMinerMapper;
        this.transferMinerMapper = transferMinerMapper;
    }

    protected  void runTask() {
        long starttime = System.currentTimeMillis();
        logger.info("TransferAddJob start .....");
        try {
            TransferAddUtil t = new TransferAddUtil();
            t.transferAdd(transferMinerMapper,blockNumber,0);
            new MinerModifyJob(yesterday,nfcFlowMinerMapper,transferMinerMapper).start();
        }catch (Exception e) {
            logger.error("TransferAddJob error,"+e.getMessage(),e);
        }
        long endtime = System.currentTimeMillis();
        logger.info("TransferAddJob end ,spends total second ="+(endtime-starttime)/1000);
    }


}
