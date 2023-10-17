package com.imooc.controller.job.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imooc.mapper.AccountMapper;
import com.imooc.mapper.AddrBalancesMapper;
import com.imooc.pojo.AddrBalances;
import com.imooc.pojo.Addresses;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class AddressService {	
	@Autowired
	AccountMapper accountMapper;
	@Autowired
	AddrBalancesMapper addrBalancesMapper;
	@Autowired
	Web3jService web3jService;
		
	/**
	 * 同步多个地址余额线程
	 * @param addressMap
	 */
	/*@Async
	public void syncAddressesBalance(Map<String, Long> addressMap) {
		try {
			TimeSpend timeSpend = new TimeSpend();
			log.info("Synchronize addresses balance thread start ...");
			for (String address : addressMap.keySet()) {
				Long blockNumber = addressMap.get(address);
				if (address == null || blockNumber == null)
					continue;
				BigInteger balance = web3jService.getAddressBalance(address);
				BigInteger nonce = web3jService.getAddressNonce(address);
				if (balance != null && nonce != null) {
					if (saveAddressBalance(address, null, blockNumber, balance, nonce, new Date()))
						saveWithDraw(address, null, blockNumber, balance, nonce);
				}
			}
			log.info("Synchronize addresses balance thread end, total " + addressMap.size() + " addresses, spend " + timeSpend.getSpendTime() + "\n");
		} catch (Exception e) {
			log.warn("syncAddressesBalance error," + e.getMessage(), e);
		}
	}*/
	/**
     * 保存/修改地址
     * @param address
     * @param blockNumber
     */
	public boolean saveOrUpdateAddress(String address, Long blockNumber) {
		try {
			BigInteger balance = web3jService.getAddressBalance(address);
			BigInteger nonce = web3jService.getAddressNonce(address);
			if (balance != null && nonce != null) {
				if (saveAddressBalance(address, null, blockNumber, balance, nonce, new Date())){
					saveWithDraw(address, null, blockNumber, balance, nonce);
					log.info("Update address " + address + " balance to " + balance);
					return true;
				}
			}			
		} catch (Exception e) {
			log.warn("saveOrupdateAddress error," + e.getMessage(), e);
		}
		return false;
	}
	
  
    /**
     * 修改代币相关地址余额
     * @param contract
     * @param addresses
     * @param blockNumber
     * @param blockDate
     * @return
     */
	/*public void handleTokenBalance(String contract, Collection<String> addresses, Long blockNumber) {
		try {
			TimeSpend timeSpend = new TimeSpend();
			log.info("Synchronize token "+contract+" addresses balance start...");
			int count = 0;
			for (String address : addresses) {
				BigInteger balance = web3jService.getAddressTokenBalance(contract, address);
				if (null == balance)
					continue;
				BigInteger nonce = web3jService.getAddressNonce(address);
				if (null == nonce)
					continue;
				if(saveAddressTokenBalance(address, contract, blockNumber, balance, nonce, new Date()))
					count ++;
			}
			log.info("Synchronize token "+contract+" addresses balance thread end, total " + count + " addresses, spend " + timeSpend.getSpendTime());
		} catch (Exception e) {
			log.warn("handleTokenBalance error," + e.getMessage(), e);
		}
	}*/
	
	/**
	 * 设置地址锁仓
	 * @param address
	 * @param blockNumber
	 */
	/*public void setAddressLocked(String address,Long blockNumber) {
		Addresses addrParam = new Addresses();
		addrParam.setAddress(address);
		Addresses addrs = accountMapper.selectOne(addrParam);
		if (addrs == null) {
			addrs = new Addresses();
			addrs.setAddress(address);
			addrs.setContract(address);
			addrs.setBlockNumber(blockNumber);
			addrs.setBalance(new BigDecimal("0"));
			addrs.setNonce(0l);
			addrs.setHaslock(1);
			addrs.setInsertedTime(new Date());
			accountMapper.saveOrUpdataHaslock(addrs);
		} else {
			if (null == addrs.getHaslock() || 0 == addrs.getHaslock()) {
				accountMapper.updateHaslock(address);
			}
		}		
    }*/
		
	private boolean saveAddressBalance(String address, String contract, Long blockNumber, BigInteger balance, BigInteger nonce, Date insert) {
		if (contract == null)
			contract = address;
		Addresses addresses = new Addresses();
		addresses.setAddress(address);
		addresses.setContract(contract);
		addresses.setBlockNumber(blockNumber);
		addresses.setBalance(new BigDecimal(balance));
		addresses.setNonce(nonce.longValue());
		addresses.setInsertedTime(insert);
	//	addresses.setBalance_block(blockNumber);
		int result = accountMapper.saveOrUpdata(addresses);
		return result ==1;
	}

	/*private boolean saveAddressTokenBalance(String address, String contract, Long blockNumber, BigInteger balance, BigInteger nonce, Date insert) {
		if (contract == null)
			contract = address;
		Addresses addrToken = new Addresses();
		addrToken.setAddress(address);
		addrToken.setContract(contract);
		addrToken.setBlockNumber(blockNumber);
		addrToken.setBalance(new BigDecimal(balance));
		addrToken.setNonce(nonce.longValue());
		addrToken.setInsertedTime(insert);
		int result = accountMapper.saveOrUpdataToken(addrToken);
		return result ==1;
	}*/
	
	private AddrBalances saveWithDraw(String address, String contract, Long blockNumber, BigInteger balance, BigInteger nonce) {
		if (contract == null)
			contract = address;
		AddrBalances addrBalances = new AddrBalances();
		addrBalances.setAddress(address);
		addrBalances.setContract(contract);
		addrBalances.setBlockNumber(blockNumber);
		addrBalances.setBalance(new BigDecimal(balance));
		addrBalances.setNonce(nonce.longValue());
		addrBalancesMapper.saveOrUpdate(addrBalances);
		return addrBalances;
	}
	
	
}
