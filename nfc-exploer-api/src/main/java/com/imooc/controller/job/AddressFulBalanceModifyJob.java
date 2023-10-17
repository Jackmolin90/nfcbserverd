package com.imooc.controller.job;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.AlienSnapshot;
import org.web3j.protocol.core.methods.response.AlienSnapshot.Snapshot;

import com.imooc.Utils.AgentSvcTask;
import com.imooc.mapper.AccountMapper;
import com.imooc.pojo.Addresses;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AddressFulBalanceModifyJob extends AgentSvcTask {

	private static Logger logger = LoggerFactory.getLogger(AddressFulBalanceModifyJob.class);
	private Long blockNumber;
	private Web3j web3j;
	private AccountMapper accountMapper;
    
    public AddressFulBalanceModifyJob(Web3j web3j, Long blockNumber, AccountMapper accountMapper) {
    	this.web3j = web3j;
        this.blockNumber = blockNumber;
        this.accountMapper = accountMapper; 
    }
    
	protected  void runTask() {
      //  long starttime = System.currentTimeMillis();
        logger.info("FulBalanceModifyJob start .....");
       
        FulBalanceModifyJob(web3j,blockNumber,accountMapper);
        
      //  long endtime = System.currentTimeMillis();
      //  logger.info("FulBalanceModifyJob end ,spends total second ="+(endtime-starttime)/1000);
    }
	
	public static void FulBalanceModifyJob(Web3j web3j, Long blockNumber, AccountMapper accountMapper){		
		try {		
			Snapshot snap = web3j.alienFulbalAtNumber(blockNumber.intValue()).send().getSnapshot();			
			if(snap!=null && snap.getFulbal()!=null){				
				Map<String, Object> fulbal=snap.getFulbal();
				Long fulBlock = accountMapper.getLeasetFulBlock();
				Long ful_nonce = 0L;
				int count=0;
				for(String address : fulbal.keySet()){
					Object value = fulbal.get(address);
					BigDecimal ful_balance = value==null ? BigDecimal.ZERO : new BigDecimal(value.toString());					
					Addresses account = accountMapper.getAddressInfo(address);
					if(account!=null && account.getFul_balance().compareTo(ful_balance)==0)
						continue;					
					Addresses accounts = new Addresses();
					accounts.setAddress(address);
					accounts.setContract(address);
					accounts.setBlockNumber(blockNumber);
					accounts.setInsertedTime(new Date());
					accounts.setFul_balance(ful_balance);
					accounts.setFul_nonce(ful_nonce);
					accounts.setFul_block(blockNumber);
					accountMapper.saveOrUpdateFul(accounts);
					count++;
				}
				if(fulBlock!=null){
					Snapshot leasetSnap = web3j.alienFulbalAtNumber(fulBlock.intValue()).send().getSnapshot();
					if(leasetSnap!=null && leasetSnap.getFulbal()!=null){
						Map<String, Object> leaseFulbal = leasetSnap.getFulbal();
						for(String address : leaseFulbal.keySet()){
							if(!fulbal.containsKey(address)){
								Addresses accounts = new Addresses();
								accounts.setAddress(address);
								accounts.setContract(address);
								accounts.setFul_balance(BigDecimal.ZERO);
								accounts.setFul_nonce(ful_nonce);
								accounts.setFul_block(blockNumber);
								accountMapper.saveOrUpdateFul(accounts);
								count++;
							}
						}
					}
				}
				log.info("Synchronize addresses ful balance thread end, total " + count + " addresses.");
			}
		} catch (IOException e) {
			log.error("FulBalanceModifyJob error,"+e.getMessage(),e);
		}
		
	}
	
	
	public static void updateAddressFulBalance(Web3j web3j, AccountMapper accountMapper, String address, Long blockNumber) {		
		try {
			AlienSnapshot response = web3j.alienFulBalance(address).send();
			if (response.hasError() || response.getSnapshot() == null) {
				logger.info("alienFulBalance " + address + " error:" + response.getError().getMessage());
				return;
			}
			BigInteger balance = response.getSnapshot().getAddrfulbal();
			if (balance == null)
				return;		
			BigDecimal ful_balance = new BigDecimal(balance);
			Long ful_nonce = 0L;
			Addresses addrFulBalances = new Addresses();
			addrFulBalances.setAddress(address);
			addrFulBalances.setContract(address);
			addrFulBalances.setBlockNumber(blockNumber);
			addrFulBalances.setInsertedTime(new Date());
			addrFulBalances.setFul_balance(ful_balance);
			addrFulBalances.setFul_nonce(ful_nonce);
			addrFulBalances.setFul_block(blockNumber);
			accountMapper.saveOrUpdateFul(addrFulBalances);
			log.info("Update address " + address + " ful balance to " + ful_nonce);
		} catch (Exception e) {
			log.warn("updateAddressFulBalance error," + e.getMessage(), e);
		}
	}
		
}
