package com.imooc.Utils;

import com.imooc.controller.job.AddressBalanceModifyJob;
import com.imooc.mapper.TransferMinerMapper;
import com.imooc.pojo.Addresses;
import com.imooc.pojo.TransferMiner;
import com.imooc.utils.Constants;

import java.util.*;

/**
 * @author zhc 2022-01-11 9:59
 */
public class TransferAddUtil {
    public void transferAdd(TransferMinerMapper transferMinerMapper,Long blockNumber, int type) {
        List<TransferMiner>  list = transferMinerMapper.getTransferAdd(blockNumber,type);
        List<TransferMiner> tfUpdate = new ArrayList<>();
        List<TransferMiner> tfInsert = new ArrayList<>();
        Map<String,Addresses> addressMap = new HashMap<>();
        for (TransferMiner trans:list) {
            if(trans.getReleaseHeigth()==0 || trans.getReleaseInterval()==0){
                TransferMiner vo = new TransferMiner();
                vo.setId(trans.getId());
                vo.setReleaseamount(trans.getTotalAmount());
                tfUpdate.add(vo);
                //release record
                TransferMiner transferMiner= new TransferMiner();
                transferMiner.setTxHash(UUID.randomUUID().toString());
                transferMiner.setRevenueaddress(trans.getRevenueaddress());
                transferMiner.setLogIndex(1);
                transferMiner.setAddress(trans.getAddress());
                transferMiner.setBlockNumber(trans.getBlockNumber());//
                transferMiner.setType(trans.getType()+1);
                transferMiner.setReleaseamount(trans.getTotalAmount());
                transferMiner.setCurtime(new Date());
                tfInsert.add(transferMiner);
                if(null!=transferMiner.getRevenueaddress()){
                    addressMap.put(transferMiner.getRevenueaddress(),new Addresses(transferMiner.getRevenueaddress(),blockNumber));
                }
            }else{
                Long times = trans.getReleaseHeigth()/trans.getReleaseInterval();
                TransferMiner vo = new TransferMiner();
                vo.setId(trans.getId());
                vo.setReleaseamount(trans.getTotalAmount());
                tfUpdate.add(vo);
                //release record
                TransferMiner transferMiner= new TransferMiner();
                transferMiner.setTxHash(UUID.randomUUID().toString());
                transferMiner.setRevenueaddress(trans.getRevenueaddress());
                transferMiner.setLogIndex(1);
                transferMiner.setAddress(trans.getAddress());
                transferMiner.setBlockNumber(trans.getBlockNumber());//
                transferMiner.setType(trans.getType()+1);
                transferMiner.setReleaseamount(trans.getTotalAmount().subtract(trans.getReleaseamount()));
                transferMiner.setCurtime(new Date());
                tfInsert.add(transferMiner);
                if(null!=transferMiner.getRevenueaddress()){
                    addressMap.put(transferMiner.getRevenueaddress(),new Addresses(transferMiner.getRevenueaddress(),blockNumber));
                }
            }
            if(tfUpdate.size()>0&&tfUpdate.size()/Constants.BATCHCOUNT>=1){
                transferMinerMapper.updateMinerTransfRelaseBatch(tfUpdate);
                tfUpdate = new ArrayList<>();
            }
            if(tfInsert.size()>0&&tfInsert.size()/Constants.BATCHCOUNT>=1){
                transferMinerMapper.insertBatch(tfInsert);
                tfInsert = new ArrayList<>();
            }
        }
        if(tfUpdate.size()>0){
            transferMinerMapper.updateMinerTransfRelaseBatch(tfUpdate);
        }
        if(tfInsert.size()>0){
            transferMinerMapper.insertBatch(tfInsert);
        }
        for (Map.Entry<String,Addresses> address : addressMap.entrySet()) {
            AddressBalanceModifyJob.getInstance().putq(address.getValue());
        }
    }
}
