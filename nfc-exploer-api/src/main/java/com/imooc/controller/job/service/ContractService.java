package com.imooc.controller.job.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import lombok.extern.slf4j.Slf4j;
import com.imooc.mapper.AccountMapper;
import com.imooc.mapper.ContractLockupMapper;
import com.imooc.mapper.ContractMapper;
import com.imooc.pojo.Contract;
import com.imooc.pojo.ContractLockup;
import com.imooc.pojo.Tokens;
import com.imooc.pojo.Transaction;
import com.imooc.utils.Constants;


/**
 * 合约服务
 *
 */
@Slf4j
@Service
public class ContractService {
		
	@Autowired
	private ContractMapper contractMapper;
	@Autowired
	private ContractLockupMapper contractLockupMapper;
	@Autowired
    AddressService addressService;
	@Autowired
    AccountMapper addressMapper;
	@Autowired
	private Web3jService web3jService;
	
	//Team Lockup Contract
	private static String transferAdmin1Topic = Constants.PRE+"858ac4d8dc6c854f604e18771d27d0066054fe88f5d6721149df79353081ee2c";
	private static String transferAdmin2Topic = Constants.PRE+"2dedd5a3b828d6e458eb1eb5a6a1bae4b64d76722b28af86c387f0b692cab3fb";

	private static String approveTopic = Constants.PRE+"77b92c0722e5bbe8d1413b7fbec6093bc4dc966a65832498dc8c2c67d9a937cc";
	private static String provideTopic = Constants.PRE+"76ac9883977ff904765119c5f2cf43d304e1929d6d8399fb697024e8df545e0e";
	private static String pickupV1Topic = Constants.PRE+"dd6eb07a5b1cff6970b7abdf62d8f670d00202f7404afd55741dfd27ea7a2d9b";
	private static String pickupV2Topic = Constants.PRE+"4a142a723704af60f8fd272a17a3f4b4c29ac84fb512c7038ccbfe91f9e1feaf";
	private static String lockupCancelTopic = Constants.PRE+"e94fb30214f4aa2d2dff6df243aebd3a3c0e8d86b1aea2ffb6b88fdc0129b0c4";

	//v1
	private static String admin1V1Key = "0x3c477f91";
	private static String admin2V1Key = "0x68a9f31c";	
	private static String lockupperiodKey = "0xddfeb214";
	//v2
	private static String admin1V2Key = "0x8da5cb5b";
	private static String admin2V2Key = "0xf851a440";
	private static String lockstartKey = "0xcf993172";
	//common	
	private static String releaseperiodKey = "0xb200ef93";
	private static String releaseintervalKey = "0x3d09e94d";
	
    
    public Contract getContract(String contractAddr){
    	return contractMapper.getContract(contractAddr);
    }
	
    /**
     * 构建团队锁仓合约
     * 如果获取参数失败,则不认为是锁仓合约
     * @param receipt
     * @param tx
     * @return
     */
	public Contract buildContract(TransactionReceipt receipt, Transaction tx) {
		String address = receipt.getContractAddress();
		if(address==null)
			return null;
		Long blockNumber = receipt.getBlockNumber().longValue();
		Contract contract = new Contract();				
		contract.setContract(address);
		contract.setTxhash(receipt.getTransactionHash());
		contract.setAuthor(receipt.getFrom());
		contract.setBlocknumber(blockNumber);
		contract.setIstoken(0);
		contract.setCreatetime(new Date());
		
		Tokens properties = web3jService.getERC20Properties(address);
		contract.setName(properties.getName());
		contract.setSymbol(properties.getSymbol());
				
		boolean isLockup = parseLockupContractV2(contract, address);
		if(!isLockup)
			isLockup = parseLockupContractV1(contract, address);
	//	if(!isLockup)
	//		log.info("Contract "+address+" is not a common contract.");
		contractMapper.saveOrUpdate(contract);
		addressService.saveOrUpdateAddress(address, blockNumber);
//		addressMapper.setAsContract(address, blockNumber);
		
		tx.setToAddr(receipt.getContractAddress());
		tx.setUfoprefix("CT");
		tx.setUfoversion("1");
		tx.setUfooperator("ContractNew");
		return contract;		
	}
	
	/**
	 * 根据TopTopic,解析团队锁仓交易
	 * @param transactionReceipt
	 * @param token
	 * @return
	 */
	public boolean parseTransaction(Contract contract, Transaction tx, Log txLog) {
	//	if (transactionReceipt == null || transactionReceipt.getLogs().size() == 0)
	//		return false;
		boolean isLockupContract = false;
		String contractAddr = contract.getContract();
		String txhash = tx.getHash();
	//	for(Log txLog : transactionReceipt.getLogs()){
		//	Log log = transactionReceipt.getLogs().get(0);
			if (txLog == null || txLog.getTopics() == null || txLog.getTopics().size() == 0)
				return false;
			String topic = txLog.getTopics().get(0);
		//	log.info("topic:" + topic);
	    	if(transferAdmin1Topic.equals(topic)){		//一级管理员权限转移
	    		isLockupContract = true;
	    		String newAdmin1 = Constants.PRE+txLog.getTopics().get(1).substring(26);		//新一级管理员地址
	    		contract.setAdmin1(newAdmin1);
	    		contract.setUpdatetime(new Date());
	    		contractMapper.saveOrUpdate(contract);
	    		tx.setUfooperator("TlcChAdmin1");
	    		tx.setParam1(newAdmin1);	    		
	    	}else if(transferAdmin2Topic.equals(topic)){//二级管理员权限转移
	    		isLockupContract = true;
	    		String newAdmin2 = Constants.PRE+txLog.getTopics().get(1).substring(26);		//新二级管理员地址 
	    		contract.setAdmin2(newAdmin2);
	    		contract.setUpdatetime(new Date());
	    		contractMapper.saveOrUpdate(contract);
	    		tx.setUfooperator("TlcChAdmin2");
	    		tx.setParam1(newAdmin2);
	    	}else if(approveTopic.equals(topic)){		//一级管理员授权xx金额	 
	    		isLockupContract = true;
	//    		String data = txLog.getData();
	//    		String data1 = data.substring(0,66);
	//    		BigInteger approveAmount = new BigInteger(data1.substring(2),16);	//授权金额	    		
	    	}else if(provideTopic.equals(topic)){	//二级管理员给用户发放xx金额
	    		isLockupContract = true;
	    		String topic1 = txLog.getTopics().get(1);
	    		String topic2 = txLog.getTopics().get(2);
	    		String data = txLog.getData();
	    		String data1 = data.substring(0,66);
	    		String address = Constants.PRE+topic1.substring(26);							//用户地址 
	    		BigInteger lockstart = new BigInteger(topic2.substring(2),16);		//锁仓开始高度		    	
	    		BigInteger lockupAmount = new BigInteger(data1.substring(2),16);	//锁仓(发放)金额
	    		Long lockupperiod = contract.getLockupperiod();							//锁仓期
				Long releaseperiod = contract.getReleaseperiod();						//释放周期
				Long releaseinterval = contract.getReleaseinterval();					//释放间隔
	    		ContractLockup contractLockup = new ContractLockup();
				contractLockup.setContract(contractAddr);
				contractLockup.setAddress(address);
				contractLockup.setTxhash(txhash);
				contractLockup.setType(1);
				contractLockup.setLockupnumber(lockstart.longValue());
				contractLockup.setLockupamount(new BigDecimal(lockupAmount));
				contractLockup.setLockupperiod(lockupperiod);
				contractLockup.setReleaseperiod(releaseperiod);
				contractLockup.setReleaseinterval(releaseinterval);
				contractLockup.setCreatetime(new Date());
				contractLockupMapper.saveOrUpdate(contractLockup);
				tx.setUfooperator("TlcProvide");
	    		tx.setParam1(address);
	    		tx.setParam3(new BigDecimal(lockstart));
	    		tx.setParam4(new BigDecimal(lockupAmount));
	    	}else if(pickupV1Topic.equals(topic)){	//用户提取xx金额	V1	一个交易中可能有多条提取记录
	    		isLockupContract = true;
	    		String topic1= txLog.getTopics().get(1);
	    		String topic2 = txLog.getTopics().get(2); 
	    		String data = txLog.getData();
	            String data1 = data.substring(0,66);  
				String data2 = data.substring(66, 66 + 64);
				String data3 = data.substring(66 + 64, 66 + 64 + 64);
	    		String address = Constants.PRE+topic1.substring(26);							//用户地址 
	    		BigInteger lockstart = new BigInteger(topic2.substring(2),16);		//锁仓开始高度	    		          
	            BigInteger pickupAmount = new BigInteger(data1.substring(2),16);	//提取的金额数	            
	            BigInteger lockupAmount = new BigInteger(data2.substring(2),16);	//锁仓(发放)金额	            
	            BigInteger remainAmount = new BigInteger(data3.substring(2),16);	//剩余锁仓金额	            
	            Long lockupperiod = contract.getLockupperiod();							//锁仓期
				Long releaseperiod = contract.getReleaseperiod();						//释放周期
				Long releaseinterval = contract.getReleaseinterval();					//释放间隔				
				ContractLockup contractLockup = new ContractLockup();
				contractLockup.setContract(contractAddr);
				contractLockup.setAddress(address);
				contractLockup.setTxhash(txhash);				
				contractLockup.setType(2);
				contractLockup.setLockupnumber(lockstart.longValue());
				contractLockup.setPickupamount(new BigDecimal(pickupAmount));
				contractLockup.setLockupamount(new BigDecimal(lockupAmount));
				contractLockup.setRemainamount(new BigDecimal(remainAmount));
				contractLockup.setLockupperiod(lockupperiod);
				contractLockup.setReleaseperiod(releaseperiod);
				contractLockup.setReleaseinterval(releaseinterval);
				contractLockup.setCreatetime(new Date());				
				contractLockupMapper.saveOrUpdate(contractLockup);
				tx.setUfooperator("TlcPickup");
	    		tx.setParam1(address);
	    		tx.setParam2(lockstart.toString());
	    		tx.setParam3(new BigDecimal(pickupAmount));
	    		tx.setParam4(new BigDecimal(lockupAmount));	    		
	    	}else if(lockupCancelTopic.equals(topic)){	//二级管理员收回用户某条锁仓
				isLockupContract = true;
				String data = txLog.getData();
				String data1 = data.substring(0, 66);
				String data2 = data.substring(66, 66 + 64);
				String data3 = data.substring(66 + 64, 66 + 64 + 64);
				String data4 = data.substring(66 + 64 + 64, 66 + 64 + 64 + 64);
	    		String address = Constants.PRE+data1.substring(26);							//用户地址
	            BigInteger lockstart = new BigInteger(data2.substring(2),16);		//锁仓开始高度
	            BigInteger lockupcancel = new BigInteger(data3.substring(2),16);	//锁仓收回高度
	            BigInteger cancelAmount = new BigInteger(data4.substring(2),16);	//收回时该条锁仓已释放的金额	            
	            ContractLockup contractLockup = new ContractLockup();
	            contractLockup.setContract(contractAddr);
				contractLockup.setAddress(address);
				contractLockup.setType(1);
				contractLockup.setLockupnumber(lockstart.longValue());				
				contractLockup.setCancelnumber(lockupcancel.longValue());
	            contractLockup.setCancelamount(new BigDecimal(cancelAmount));
	            contractLockupMapper.updateCancel(contractLockup);	            
	            tx.setUfooperator("TlcRecycle");
	    		tx.setParam1(address);
	    		tx.setParam2(lockstart.toString());
	    		tx.setParam3(new BigDecimal(lockupcancel));
	    		tx.setParam4(new BigDecimal(cancelAmount));
	          //  	log.warn("can not find contract lockup data for contract:"+contract+",address:"+address.toLowerCase()+",blocknumber:"+lockstart);
	    	}else if(pickupV2Topic.equals(topic)){	//用户提取xx金额	V2
	    		isLockupContract = true;	    		
	    		String topic1= txLog.getTopics().get(1);
	    	//	String topic2 = txLog.getTopics().get(2);  
	    		String data = txLog.getData();
	            String data1 = data.substring(0,66); 
	            String data2=data.substring(66,66+64);
	        //  String data3= data.substring(66+64,66+64+64);    
	    		String address = Constants.PRE+topic1.substring(26);							//用户地址 
	    	//	BigInteger lockstart = new BigInteger(topic2.substring(2),16);		//锁仓开始高度	    		           
	            BigInteger pickupAmount = new BigInteger(data1.substring(2),16);	//提取的金额数	           
	            BigInteger lockupAmount = new BigInteger(data2.substring(2),16);	//锁仓(发放)金额
	        //  BigInteger remainAmount = new BigInteger(data3.substring(2),16);	//剩余锁仓金额
	            Long lockupstart = contract.getLockupstart();							//锁仓开始高度
	            Long lockupperiod = contract.getLockupperiod();							//锁仓期
				Long releaseperiod = contract.getReleaseperiod();						//释放周期
				Long releaseinterval = contract.getReleaseinterval();					//释放间隔				
				ContractLockup contractLockup = new ContractLockup();
				contractLockup.setContract(contractAddr);
				contractLockup.setAddress(address);
				contractLockup.setTxhash(txhash);				
				contractLockup.setType(2);
				contractLockup.setLockupnumber(lockupstart);				
				contractLockup.setPickupamount(new BigDecimal(pickupAmount));
				contractLockup.setLockupamount(new BigDecimal(lockupAmount));
			//	contractLockup.setRemainamount(new BigDecimal(remainAmount));
				contractLockup.setLockupperiod(lockupperiod);
				contractLockup.setReleaseperiod(releaseperiod);
				contractLockup.setReleaseinterval(releaseinterval);
				contractLockup.setCreatetime(new Date());				
				contractLockupMapper.saveOrUpdate(contractLockup);
				
				tx.setUfooperator("TlcPickup");
	    		tx.setParam1(address);
	    		tx.setParam3(new BigDecimal(pickupAmount));
	    		tx.setParam4(new BigDecimal(lockupAmount));
	    		tx.setParam5(pickupAmount.toString());
	    	}
	//	}		
		if(tx.getUfooperator()!=null && !"".equals(tx.getUfooperator())){
			tx.setUfoprefix("CT");
			tx.setUfoversion("1");
		}
		tx.setContract(contractAddr);
		return isLockupContract;
    }
	
		
	private boolean parseLockupContractV1(Contract contract, String address){
		try{
			String admin1 = getAddressValue(address, admin1V1Key);						//一级管理员
			String admin2 = getAddressValue(address, admin2V1Key);						//二级管理员
			BigInteger lockupperiod = getNumericValue(address, lockupperiodKey);		//锁仓期
			BigInteger releaseperiod = getNumericValue(address, releaseperiodKey);		//释放周期
			BigInteger releaseinterval = getNumericValue(address,releaseintervalKey);	//释放间隔
			
			contract.setType(1);			
			contract.setAdmin1(admin1);
			contract.setAdmin2(admin2);
			contract.setLockupperiod(lockupperiod.longValue());
			contract.setReleaseinterval(releaseinterval.longValue());
			contract.setReleaseperiod(releaseperiod.longValue());			
			return true;
		} catch (Exception e) {
			log.info("Contract "+address+" is not a team lockup contract v1 for :"+e.getMessage());
			return false;
		}
	}
		
	private boolean parseLockupContractV2(Contract contract, String address){		
		try{
			String admin1 = getAddressValue(address, admin1V2Key);						//一级管理员
			String admin2 = null;														//二级管理员
			try{
				admin2 = getAddressValue(address, admin2V2Key);
			}catch(Exception e){				
			}
			BigInteger lockupstart = getNumericValue(address, lockstartKey);			//锁仓开始高度
			BigInteger releaseperiod = getNumericValue(address, releaseperiodKey);		//释放周期
			BigInteger releaseinterval = getNumericValue(address,releaseintervalKey);	//释放间隔
			contract.setType(2);
			contract.setAdmin1(admin1);
			contract.setAdmin2(admin2);
		//	contract.setLockupperiod(lockupperiod.longValue());
			contract.setLockupstart(lockupstart.longValue());
			contract.setReleaseinterval(releaseinterval.longValue());
			contract.setReleaseperiod(releaseperiod.longValue());
			return true;
		} catch (Exception e) {
			log.info("Contract "+address+" is not a team lockup contract v2 for :"+e.getMessage());
			return false;
		}
	}

	
	/**
	 * 获取地址值,加上前缀
	 */
	private String getAddressValue(String contract, String key) throws Exception {
		String value = web3jService.getContractParam(contract, key);
		return Constants.PRE + value.substring(26);
	}
	/**
	 * 获取数字值
	 */
	private BigInteger getNumericValue(String contract, String key) throws Exception {
		String value = web3jService.getContractParam(contract, key);
		return new BigInteger(value.substring(2), 16);
	}
    
	
	
    
	
    
	/**
	 * 合约锁仓
	 * @param contract			合约地址		
	 * @param address			用户地址
	 * @param lockupnumber		锁仓高度
	 * @param lockupamount		锁仓金额
	 * @param pickupamount		提取金额
	 * @param remainamount 		剩余金额
	 * @param lockupperiod 		锁仓周期
	 * @param releaseperiod		释放周期
	 * @param releaseinterval	释放间隔
	 * @return
	 */
	protected ContractLockup createContractLockup(String contract, String address, Long lockupnumber, BigDecimal lockupamount, BigDecimal pickupamount, BigDecimal remainamount, 
			Long lockupperiod, Long releaseperiod, Long releaseinterval) {
		ContractLockup contractLockup = new ContractLockup();
		contractLockup.setContract(contract);
		contractLockup.setAddress(address);
		contractLockup.setLockupnumber(lockupnumber);
		contractLockup.setPickupamount(pickupamount);
		contractLockup.setLockupamount(lockupamount);
		contractLockup.setRemainamount(remainamount);
		contractLockup.setLockupperiod(lockupperiod);
		contractLockup.setReleaseperiod(releaseperiod);
		contractLockup.setReleaseinterval(releaseinterval);
		contractLockup.setCreatetime(new Date());		
        return contractLockup;        
	}
	
}
