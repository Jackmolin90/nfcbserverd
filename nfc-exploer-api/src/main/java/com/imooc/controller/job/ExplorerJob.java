package com.imooc.controller.job;

import com.imooc.Utils.MapCache;
import com.imooc.controller.job.service.ContractService;
import com.imooc.enums.MinerStatusEnum;
import com.imooc.enums.NodeTypeEnum;
import com.imooc.enums.VideoStatusEnum;
import com.imooc.mapper.*;
import com.imooc.pojo.*;
import com.imooc.pojo.Form.NfcNodeMinerQueryForm;
import com.imooc.pojo.Transaction;
import com.imooc.pojo.vo.TransactionVo;
import com.imooc.service.util.TxDataParse;
import com.imooc.utils.*;
import io.swagger.annotations.Api;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.abi.FunctionEncoder;
import org.web3j.contracts.eip20.generated.ERC20;
import org.web3j.contracts.eip721.generated.ERC721Enumerable;
import org.web3j.contracts.eip721.generated.ERC721Metadata;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.utils.Numeric;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.TypeReference;

@RestController
@Api(value = "Sync block transaction split token information", tags = "Sync block transaction split token information")
public class ExplorerJob {
    private static Logger logger = LoggerFactory.getLogger(ExplorerJob.class);

    @Value("${ipaddress}")
    private String ipaddress;   //url

    @Value("${dayOneNumber}")
    private   Long dayOneNumber;//1 day block height, 10 seconds to generate a block

    @Value("${lockreleaseinterval}")
    private   Long lockreleaseinterval;//lockreleaseinterval

    @Value("${blockTotalNfc}")
    private   Long blockTotalNfc;//Traffic mining rewards


    @Value("${addresspool}")
    private String addresspool;//Interest pool address

    @Value("${yeartotal}")
    private   Long yeartotal;//The total number of seconds in a year

    @Value("${perblocksecond}")
    private   int perblocksecond;//How many seconds is a block

    @Value("${year}")
    private  String year;//Life cycle is calculated as 43 years

    @Value("${allrewards}")
    private  String allrewards;  //Total block reward

    @Value("${blocktotal}")
    private   String blocktotal;//Estimated total number of blocks in a year

    @Value("${teamlock}")
    private String teamlock;

    @Value("${nodepledge}")
    private String nodepledge;

    @Value("${flowrevenue}")
    private String flowrevenue;

    @Value("${mineraddress1}")
    private String mineraddress1;

    @Value("${mineraddress2}")
    private String mineraddress2;

    @Value("${mineraddress3}")
    private String mineraddress3;

    @Value("${signaddress}")
    private  String signaddress;

    @Value("${teammanager}")
    private String teammanager;

    @Value("${feemanager}")
    private  String feemanager;

    @Value("${epoch}")
    private long round;//Number of blocks in a cycle

    @Value("${epoch}")
    private long epoch;//Number of blocks between each round of election

    @Value("${nodeblock}")
    private long nodeblock;//Number of synchronization witness node blocks

    @Value("${sendData}")
    private String dendData;//

    @Value("${contractMethod}")
    private String contractMethod;//

    @Value("${rnMinValue}")
    private String rnMinValue;

    @Value("${cnMinValue}")
    private String cnMinValue;

    @Value("${enMinValue}")
    private String enMinValue;

    @Value("${voterInput}")
    private  String voterInput;

    @Value("${exploerUrl}")
    private  String exploerUrl;

    @Value("${profitUrl}")
    private  String profitUrl;

    @Value("${pledgeAddress}")
    private  String pledgeAddress;

    @Value("${nodeStartRn}")
    private  String nodeStartRn;

    @Value("${nodeStartCn}")
    private  String nodeStratCn;

    @Value("${address1}")
    private  String address1;

    @Value("${address2}")
    private  String address2;

    @Value("${address3}")
    private  String address3;

    @Value("${taskblock}")
    private  int taskblock;

    @Value("${blocklock}")
    private   Long blocklock;//blocklock
    
    @Value("${fulRate:13671875000000}")
    private Long fulRate;

    private String erc20Start="0x70a08231";//

    private String erc1155Start="0x00fdd58e";//

    private String erc1155Transaction="0xf242432a";//

    private static final String pledgeFilter=Constants.PRE+"9489b96ebcb056332b79de467a2645c56a999089b730c99fead37b20420d58e7";

    private static final String LogFilter = "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef";

    private String decimals = "1000000000000000000";
    @Autowired
    private BlockMapper blockMapper;

    @Autowired
    private BlockForkMapper blockForkMapper;

    @Autowired
    private BlockRewardsMapper blockRewardsMapper;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private TransForkMapper transForkMapper;

    @Autowired
    private ContractsMapper contractsMapper;

    @Autowired
    private LogMapper logMapper;

    @Autowired
    private TokenMapper tokenMapper;

    @Autowired
    private TransferTokenMapper transferTokenMapper;

    @Autowired
    private TokenInstancesMapper tokenInstancesMapper;

    @Autowired
    private AddrBalancesMapper addrBalancesMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private TransferMinerMapper transferMinerMapper;

    @Autowired
    private DposVotesMapper dposVotesMapper;

    @Autowired
    private DposNodeMapper dposNodeMapper;

    @Autowired
    private NodeExitMapper nodeExitMapper;

    @Autowired
    private PunishedMapper punishedMapper;

    @Autowired
    private DposVoterWalletMapper dposVoterWalletMapper;

    @Autowired
    private ContractTokenMapper contractTokenMapper;

    @Autowired
    private PledgeDataMapper pledgeDataMapper;

    @Autowired
    private PledgeTotalDataMapper pledgeTotalDataMapper;

    @Autowired
    private NfcFlowMinerMapper nfcFlowMinerMapper;
    
    @Autowired
    private ContractService contractService;
    
    private Web3j web3j;

    @Scheduled(cron = "${explorerJobCron}")     // 0/5 * * * * ?   0 * * * * ?
    @Transactional
    public synchronized void run() throws Exception {
        logger.info("Start synchronizing data");
        List<Transaction> deposit = transactionMapper.getTransactionByTxType(SscOperatorEnum.Deposit.getCode());
        String pledge = null;
        if(deposit.size()>0){
            pledge =  deposit.get(0).getParam1();
        }else{
            throw  new Exception("no Deposit config set");
        }
        Date yesterday = DateUtil.getBeginDayOfYesterday();
        web3j = Web3jUtils.getWeb3j(ipaddress);
        Long lastBlockNumber = blockMapper.getLeatestBlockNumber();   //Latest block height
        if (0 == lastBlockNumber) {
            lastBlockNumber = 0L;
        }
        long currentBlockNumber = getCurrentBlockNumber(web3j);
        if (lastBlockNumber >= currentBlockNumber) {
            return;
        }
        int blockTotal = lastBlockNumber + taskblock< currentBlockNumber ? taskblock : (int) (currentBlockNumber - lastBlockNumber);
        int txCount = 0;
        int logCount = 0;
        int logTotal = 0;
        int uncleCount = 0;
        int uncleTotal = 0;
        int blockCount = 0;
        int tokenCount = 0;
        int tokenTotal = 0;
        int balanceCount = 0;
        int balanceTotal = 0;
        int contractCount = 0;
        int contractTotal = 0;
        int tokenTransCount = 0;
        int tokenTransTotal = 0;
        long allst = System.currentTimeMillis();
        logger.info("all start .....");
        Long blockNumber = null;
        for (int i = 0; i < blockTotal; i++) {
            long blockst = System.currentTimeMillis();
            logger.info("blockst start .....");
            blockNumber = lastBlockNumber + i + 1;
            BigInteger blockNumberPara = BigInteger.valueOf(blockNumber);
            EthBlock ethBlock;
            try {
                ethBlock = web3j.ethGetBlockByNumber2(DefaultBlockParameter.valueOf(blockNumberPara), true).send();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
            if (null == ethBlock) {
                break;
            }
            EthBlock.Block block = ethBlock.getBlock();
            if (null == block) {
                break;
            }
            long snapst = System.currentTimeMillis();
            logger.info("snap start .....");
            AlienSnapshot.Snapshot snapSign=web3j.alienSnapshotSignerAtNumber(new Long(blockNumber).intValue()).send().getSnapshot();
            Date timeStamp= new Date();
            logger.info("blocknumber="+blockNumber+" getSnapshot time costs="+(System.currentTimeMillis()-snapst));
            long lastst = System.currentTimeMillis();
            AlienSnapshot.Snapshot snapSignLatest=web3j.alienSnapshotSignerAtNumber(new Long(blockNumber-1).intValue()).send().getSnapshot();
            logger.info("blocknumber="+(blockNumber-1)+" getSnapLatest time costs="+(System.currentTimeMillis()-lastst));
            timeStamp = new Date(block.getTimestamp().longValue() * 1000);
            long time = snapSign.getLoopStartTime().longValue() * 1000;
            Date date = new Date(time);
            List<String> signers = snapSign.getSigners();//21 lists of all witness nodes
            signers = signers.stream().map(String::toLowerCase).collect(Collectors.toList());
            saveDposNodeInfo(blockNumber, nodeblock, signers);//Get node data information
            Map<String, BigInteger> thisBlock = snapSign.getPunished();
            Map<String, BigInteger> latestBlock = snapSignLatest.getPunished();
            long fracst = System.currentTimeMillis();
            updateNodeFractions(thisBlock);
            long fraced = System.currentTimeMillis();
            logger.info("blocknumber="+blockNumber+" updateNodeFractions time costs="+(fraced-fracst));
            if (thisBlock.size() == 0 || thisBlock.isEmpty()) {
                if (latestBlock.size() == 0 || latestBlock.isEmpty()) {
                    logger.info("The comparison data are all empty");
                }
            } else {
                savePunishedInfo(pledge,blockNumber, timeStamp, thisBlock, latestBlock, block.getMiner());
            }
            logger.info("blocknumber="+blockNumber+" savePunishedInfo time costs="+(System.currentTimeMillis()-fraced));

            //RewardLockReleaseDataJob
            if(blockNumber!=lockreleaseinterval&&(blockNumber-lockreleaseinterval)%dayOneNumber==0){
                new RewardLockReleaseDataJob(yesterday,timeStamp,web3j,blockNumber,nfcFlowMinerMapper,transferMinerMapper).start();
            }
            if(blockNumber%blocklock==0){
                new BlockLockReleaseDataJob(yesterday,timeStamp,web3j,blockNumber,nfcFlowMinerMapper,transferMinerMapper).start();
            }

            long snapEd = System.currentTimeMillis();
            logger.info("blocknumber="+blockNumber+" snap end  ,spends total time ="+(snapEd-snapst));

            Date dates=new Date(block.getTimestamp().longValue() * 1000);//Block timestamp
            BigInteger blockReward = saveBlockReward(block.getHash(), blockNumber.longValue(), block.getMiner(),dates);
            List<String> uncles = block.getUncles();
            if (null != uncles && !uncles.isEmpty()) {
                uncleTotal += uncles.size();
                uncleCount += saveBlockFork(uncles, block.getHash(), blockNumber.longValue());
                blockReward = blockReward.add(saveUnclesReward(block.getHash(), blockNumber.longValue(), uncles.size(), block.getMiner()));
            }
            Map<String, Boolean> ethAddress = new HashMap<>();
            ethAddress.put(block.getMiner(),false);//miner
            BigInteger feeTotal = BigInteger.ZERO;
            List<EthBlock.TransactionResult> resultList = block.getTransactions();
            if (null != resultList && !resultList.isEmpty()) {
                List<Transaction> trasList = new ArrayList<>();
                Date timestamp = new Date(block.getTimestamp().longValue() * 1000);
                for (EthBlock.TransactionResult resultItem : resultList) {
                    if (null == resultItem)
                        continue;
                    EthBlock.TransactionObject transaction = (EthBlock.TransactionObject) resultItem;
                    TransactionReceipt receipt = getTransactionReceipt(web3j, transaction.getHash());
                    String fromHashs="";
                    if(transaction.getFrom() !=null){
                        fromHashs =transaction.getFrom();
                    }
                    if (null == receipt)
                        continue;
                    long transSt = System.currentTimeMillis();
                    /*if(receipt.getLogs().size()==0){
                        txCount += saveTransaction(trasList,ethAddress,timestamp, transaction, receipt,null) ? 1 : 0;
                    }
                    if(receipt.getLogs().size()!=0){
                        txCount += saveTransaction(trasList,ethAddress,timestamp, transaction, receipt,receipt.getLogs()) ? 1 : 0;
                    }*/
                  //Return Transaction data object
                    Transaction tx = saveTransaction(trasList,ethAddress,timestamp, transaction, receipt,receipt.getLogs());
                    if(tx!=null)
                    	txCount ++;
                    long btst0 = System.currentTimeMillis();
                    if(trasList.size()>0&&trasList.size()/Constants.BATCHCOUNT>=1){
                        transactionMapper.batchTransaction(trasList);
                        trasList = new ArrayList<>();
                    }
                    logger.info("blocknumber="+blockNumber+" batchTransaction end ,spends total time ="+(System.currentTimeMillis()-btst0));
                    long transEd = System.currentTimeMillis();
                    logger.info("blocknumber="+blockNumber+" receipt end  ,spends total time ="+(transEd-transSt));
                    feeTotal = feeTotal.add(transaction.getGasPrice().multiply(receipt.getGasUsed()));
                    ethAddress.put(transaction.getFrom(), false);
                    if (transaction.getTo() != null) {
                        ethAddress.put(transaction.getTo(), false);
                    }
                  //New contract                    
                    if(receipt.getContractAddress()!=null){
                    //	boolean isMatched = tokenService.buildToken(receipt, tx) !=null; 	// Token Contract
					//	if (!isMatched) {
							contractService.buildContract(receipt, tx); 		// Other Contract							
					//	}
						ethAddress.put(receipt.getContractAddress(), true);
                    }
                    List<Log> logList = receipt.getLogs();
                    if (null != logList && logList.size()>0) {
                        logTotal += logList.size();
                        for (Log logItem : logList) {
                            if (null == logItem)
                                continue;
                            List<String> topic = logItem.getTopics();
                            //*erc20*//*
                            if (null != topic && topic.size()>0) {
                                String topFilter=logItem.getTopics().get(0);
                                if(topFilter.equalsIgnoreCase(pledgeFilter)){
                                	String addres=logItem.getAddress();
                                	validateAddress(topFilter,blockNumber,addres,logItem,fromHashs,transaction.getGasPrice(),receipt.getGasUsed(),block.getGasLimit(),transaction,timeStamp,receipt,dates);                                
                                }else{                               		
                            		String contractAddr = tx.getContract();
                            		if(contractAddr==null)
										contractAddr = tx.getToAddr();
                            //		Tokens token = tokenService.getToken(contractAddr);
                            //		if(token!=null){	//代币交易
                            //			isMatch = tokenService.parseTransaction(token, tx, logItem);    	//Token trasaction                            		
                            //		}
                            //		if(!isMatch){		//其他合约交易,如团队锁仓等     
                                		Contract contract = contractService.getContract(contractAddr);
                                		if(contract!=null){
                                			contractService.parseTransaction(contract, tx, logItem);		//Contract transaction	                                			
                                		}
                            //		}                               	
                                }
                            }
                        }
                    }
                }
                logger.info("blocknumber="+blockNumber+" transCount="+resultList.size());
                long btst = System.currentTimeMillis();
                if(trasList.size()>0){
                    transactionMapper.batchTransaction(trasList);
                }
                logger.info("blocknumber="+blockNumber+" batchTransaction end ,spends total time ="+(System.currentTimeMillis()-btst));
            }
            Set<String> addressess = ethAddress.keySet();
            balanceTotal = addressess.size();
            Date insert = new Date(block.getTimestamp().longValue() * 1000);
            handleEthBalance(web3j, addressess, blockNumber,insert);
            saveFeeReward(block.getHash(), block.getMiner(), feeTotal,block.getNumber());
            blockReward = blockReward.add(feeTotal);
            blockCount += saveBlockFromEthBlock(block,blockNumber, blockReward, null != resultList ? resultList.size() : 0) ? 1 : 0;

            long blocked = System.currentTimeMillis();
            logger.info("blocknumber="+blockNumber+" block end ,spends total time ="+(blocked-blockst));
        }
        
        if (blockNumber != null) {
			new AddressFulBalanceModifyJob(web3j, blockNumber, accountMapper).start();
        }
       long alled = System.currentTimeMillis();
       logger.info("all end ,spends total time ="+(alled-allst));
        return;
    }




    //Determine whether the transaction type is locked or pledged
    private void validateAddress(String topFilter, Long blockNumber, String addres, Log logItem,String fromHashs,BigInteger gasPrice,BigInteger gasUsed,BigInteger gasLimit,EthBlock.TransactionObject item,Date timeStamp,TransactionReceipt receipt,Date date)throws Exception  {
        BigInteger value= item.getValue();
        long logLength=receipt.getLogs().size();
        if(value==null){
            value=BigInteger.ZERO;
        }
        BigDecimal valSend=new BigDecimal(value.toString());
        if(pledgeFilter.equalsIgnoreCase(topFilter)){
            //0: Block production pledge and lock-up, 1: Flow pledge and lock-up, 2: Flow reward
            int type = Integer.parseInt(convertToBigDecimal(logItem.getTopics().get(2)).toString());
            if(type == 0){
                saveTransMinerFromLogV2(logItem,3,blockNumber,fromHashs,gasPrice,gasUsed,gasLimit,receipt,date,logLength,valSend) ;
            }else if(type == 1){
                saveTransMinerFromLogV2(logItem,7,blockNumber,fromHashs,gasPrice,gasUsed,gasLimit,receipt,date,logLength,valSend) ;
            }
        }
    }
    private void updateNodeFractions(Map<String, BigInteger> thisBlock){
        List<String> nodes = new ArrayList<>();
        nodes.add("0");
        for(Map.Entry<String,BigInteger> entryNow:thisBlock.entrySet()) {
            String keyNow=entryNow.getKey().toLowerCase();
            nodes.add(keyNow);
            NfcNodeMiner newNode = new NfcNodeMiner();
            newNode.setNode_address(keyNow);
            newNode.setSync_time(new Date());
            newNode.setFractions(entryNow.getValue().intValue());
            nodeExitMapper.updateNodeMiner(newNode);
        }
        if(nodes.size()>0){
            nodeExitMapper.updateNodeBatch(nodes);
        }
    }
    //Get the score information of the penalty node
    private void savePunishedInfo(String pledge,Long blockNumber,Date timeStamp ,Map<String, BigInteger> thisBlock, Map<String, BigInteger> latestBlock,String minerHash)throws  Exception {
        int num =blockNumber.intValue();
        Integer round=getRound(num); //Current round of punishment
        logger.info("Start comparing data");
        minerHash=minerHash.toLowerCase();
        Long feeMiss=30L;//Absent block deducts 30
        for(Map.Entry<String,BigInteger> entryNow:thisBlock.entrySet()){  //Traverse the map of the current block height
//            if(latestBlock.size()==0){
//                Punished  punished = new Punished();
//                punished.setAddress(entryNow.getKey());
//                punished.setBlockNumber(blockNumber);
//                punished.setRound(getRound(num));
//                punished.setFractions(feeMiss);
//                punished.setType(VideoStatusEnum.SUCCESS.value);//1 Absent block
//                punished.setTimeStamp(timeStamp);
//                BigInteger pledgeAmoun = new BigInteger(pledge).divide(new BigInteger(Constants.TOTALMBPOINT+"")).multiply(new BigInteger(feeMiss.toString()));
//                punished.setPledgeAmount(new BigDecimal(pledgeAmoun));
//                punishedMapper.insert(punished);
//            }else{
//                for(Map.Entry<String,BigInteger>entryLatest:latestBlock.entrySet()){
//                    Punished  punished = new Punished();
//                    String keyNow=entryNow.getKey();
//                    String keyLatest=entryLatest.getKey();
//                    BigInteger valueNow =entryNow.getValue();
//                    BigInteger valueLatest =entryLatest.getValue();
//                    if(keyNow.equalsIgnoreCase(keyLatest)){  //Analyze the scores of the same address in adjacent blocks
//                        BigInteger subFee=valueNow.subtract(valueLatest);
//                        if(subFee.longValue()>0){  //Absent block
//                            punished.setAddress(keyNow);
//                            punished.setBlockNumber(blockNumber);
//                            punished.setRound(getRound(num));
//                            punished.setFractions(feeMiss);
//                            punished.setType(VideoStatusEnum.SUCCESS.value);//1 Absent block
//                            punished.setTimeStamp(timeStamp);
//                            BigInteger pledgeAmoun = new BigInteger(pledge).divide(new BigInteger(Constants.TOTALMBPOINT+"")).multiply(new BigInteger(feeMiss.toString()));
//                            punished.setPledgeAmount(new BigDecimal(pledgeAmoun));
//                            punishedMapper.insert(punished);
//                        }
//                    }
//                }
//            }
        	 String address = entryNow.getKey();
             if(latestBlock.containsKey(address)){
             	BigInteger valueNow = entryNow.getValue();
             	BigInteger valueLatest = latestBlock.get(address);
             	if(valueNow.compareTo(valueLatest)<=0)
             		continue;
             }
             Punished  punished = new Punished();
             punished.setAddress(address);
             punished.setBlockNumber(blockNumber);
             punished.setRound(getRound(num));
             punished.setFractions(feeMiss);
             punished.setType(1);//1 Absent block
             punished.setTimeStamp(timeStamp);
             BigInteger pledgeAmoun = new BigInteger(pledge).divide(new BigInteger(Constants.TOTALMBPOINT+"")).multiply(new BigInteger(feeMiss.toString()));
             punished.setPledgeAmount(new BigDecimal(pledgeAmoun));
             punishedMapper.insert(punished);
        }
    }



    private Date getDateByNumber(Web3j web3j,Long blockNumber)throws Exception{
        BigInteger blockNumberParam= BigInteger.valueOf(blockNumber);
        EthBlock ethBlock = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(blockNumberParam), true).send();
        Date date = new Date(ethBlock.getBlock().getTimestamp().longValue()*1000);
        return date;
    }

    private void saveTransMinerFromLogV2(Log logItem,Integer type,Long blockNumber,String fromHash,BigInteger gasPrice,BigInteger gasUsed,BigInteger gasLimit,TransactionReceipt receipt,Date date,Long logLength,BigDecimal transactionValue) throws Exception {
        TransferMiner transferMiner= new TransferMiner();
        List<String> topics=logItem.getTopics();
        String address="";
        if(topics.size()>=1){
            address=Constants.PRE+topics.get(1).substring(26);
        }
        switch (type){
            case 3:
               // BigDecimal values=convertToBigDecimal ("0x"+logItem.getData().substring(2,66));
                NfcNodeMinerQueryForm queryForm = new  NfcNodeMinerQueryForm();
                queryForm.setNode_address(address);
                NfcNodeMiner preNode = nodeExitMapper.getNode(queryForm);
                transferMiner.setTxHash(logItem.getTransactionHash());
                transferMiner.setLogIndex(logItem.getLogIndex().intValue());
                transferMiner.setAddress(address);
                transferMiner.setBlockHash(logItem.getBlockHash());
                transferMiner.setBlockNumber(logItem.getBlockNumber().longValue()-1);
                transferMiner.setTotalAmount(preNode.getPledge_amount());
                transferMiner.setRevenueaddress(StringUtils.isEmpty(preNode.getRevenue_address())?preNode.getNode_address():preNode.getRevenue_address());
                transferMiner.setType(3);//Node lock
                transferMiner.setValue(new BigDecimal(0));
                transferMiner.setStartTime(date);
                transferMiner.setGasLimit(gasLimit.longValue());
                transferMiner.setGasPrice(gasPrice.longValue());
                transferMiner.setGasUsed(gasUsed.longValue());
                transferMiner.setLogLength(logLength);
                transferMiner.setPresentAmount(null);
                List<Transaction> listNode = transactionMapper.getTransactionByTxType(SscOperatorEnum.CndLock.getCode());
                if(listNode.size()>0){
                    Transaction t = listNode.get(0);
                    transferMiner.setUnLockNumber(transferMiner.getBlockNumber()+Long.parseLong(t.getParam1()));
                    transferMiner.setReleaseHeigth(Long.parseLong(t.getParam2()));
                    transferMiner.setReleaseInterval(t.getParam3().longValue());
                    transferMiner.setLockNumHeight(Long.parseLong(t.getParam1()));
                }else{
//                    transferMiner.setUnLockNumber(logItem.getBlockNumber().longValue());
//                    transferMiner.setReleaseHeigth(0L);
//                    transferMiner.setReleaseInterval(0L);
//                    transferMiner.setLockNumHeight(0L);
                    throw  new Exception("no node lock config");
                }
                transferMiner.setNodeNumber(null);
                transferMiner.setReleaseamount(BigDecimal.ZERO);

                //Node exit
                NfcNodeMiner node = new NfcNodeMiner();
                node.setNode_address(address);
                node.setNode_type(NodeTypeEnum.exit.getCode());
                node.setPledge_amount(new BigDecimal(0));
                node.setSync_time(new Date());
                nodeExitMapper.updateNodeMiner(node);
                break;

            case  7:
                NfcFlowMiner pre_miner = nfcFlowMinerMapper.getSingleMiner(address);
                transferMiner.setTxHash(logItem.getTransactionHash());
                transferMiner.setLogIndex(logItem.getLogIndex().intValue());
                transferMiner.setAddress(address);
                transferMiner.setBlockHash(logItem.getBlockHash());
                transferMiner.setBlockNumber(logItem.getBlockNumber().longValue()-1);
                transferMiner.setTotalAmount(pre_miner.getPledge_amount());
                transferMiner.setRevenueaddress(StringUtils.isEmpty(pre_miner.getRevenue_address())?pre_miner.getMiner_addr():pre_miner.getRevenue_address());
                transferMiner.setType(7);
                transferMiner.setValue(new BigDecimal(0));
                transferMiner.setStartTime(date);
                transferMiner.setGasLimit(gasLimit.longValue());
                transferMiner.setGasPrice(gasPrice.longValue());
                transferMiner.setGasUsed(gasUsed.longValue());
                transferMiner.setLogLength(logLength);
                transferMiner.setPresentAmount(null);
                List<Transaction> listMiner = transactionMapper.getTransactionByTxType(SscOperatorEnum.FlwLock.getCode());
                if(listMiner.size()>0){
                    Transaction t = listMiner.get(0);
                    transferMiner.setUnLockNumber(transferMiner.getBlockNumber()+Long.parseLong(t.getParam1()));
                    transferMiner.setReleaseHeigth(Long.parseLong(t.getParam2()));
                    transferMiner.setReleaseInterval(t.getParam3().longValue());
                    transferMiner.setLockNumHeight(Long.parseLong(t.getParam1()));
                }else{
//                    transferMiner.setUnLockNumber(logItem.getBlockNumber().longValue());
//                    transferMiner.setReleaseHeigth(0L);
//                    transferMiner.setReleaseInterval(0L);
//                    transferMiner.setLockNumHeight(0L);
                      throw  new Exception("no miner lock config");
                }
                transferMiner.setNodeNumber(null);
                transferMiner.setReleaseamount(BigDecimal.ZERO);

                //Flow mining miners exit
                NfcFlowMiner miner = new NfcFlowMiner();
                miner.setMiner_addr(address);
                miner.setSync_time(new Date());
                miner.setMiner_status(MinerStatusEnum.exit.getCode());
                miner.setPledge_amount(new BigDecimal(0));
                miner.setBandwidth(new BigDecimal(0));
                miner.setBlocknumber(blockNumber);
                nfcFlowMinerMapper.updateFlowMiner(miner);
                break;

            default :
                return ;
        }
        transferMinerMapper.insertOrUpdateMiner(transferMiner);
        saveOrUpdateAddresses(transferMiner);
        return ;
    }

    /**
     * If it is a flow lock, insert the lock address into the address table
     * @param transferMiner
     */
    private void saveOrUpdateAddresses(TransferMiner transferMiner) {
        Integer type=transferMiner.getType();
        String address=transferMiner.getAddress();
        if(type==5){
            Addresses addrParam=new Addresses();
            addrParam.setAddress(transferMiner.getAddress());
            Addresses addrs=accountMapper.selectOne(addrParam);
            if(addrs==null){
                addrs=new Addresses();
                addrs.setAddress(address);
                addrs.setContract(address);
                addrs.setBlockNumber(transferMiner.getBlockNumber());
                addrs.setBalance(new BigDecimal("0"));
                addrs.setNonce(0l);
                addrs.setHaslock(1);
                addrs.setInsertedTime(new Date());
                accountMapper.saveOrUpdataHaslock(addrs);
            }else{
                if(null==addrs.getHaslock()||0==addrs.getHaslock()){
                    accountMapper.updateHaslock(address);
                }
            }
        }
    }

    /**
     * transaction data
     * @param data    input
     * @param transaction
     */
    private void txData(Map<String, Boolean> ethAddress,String data, Transaction transaction,List<Log> logs) {
        //ufo
        TransactionVo transactionVo = MapCache.getValue(data) ;
        if (transactionVo != null) {
            transaction.setUfoprefix(transactionVo.getUfoprefix());
            transaction.setUfoversion(transactionVo.getUfoversion());
            transaction.setUfooperator(transactionVo.getUfooperator());
            TxDataParse.setTxData(ethAddress,address1,address2,address3,transaction,transactionMapper,nfcFlowMinerMapper,nodeExitMapper,logs,fulRate);
            if("Exch".equals(transactionVo.getUfooperator())){
            	AddressFulBalanceModifyJob.updateAddressFulBalance(web3j, accountMapper, transaction.getParam1(), transaction.getBlockNumber());
            }
        }
    }

    //合约地址
    @Value("${contractAddress}")
    private String contractAddress;

    private Transaction saveTransaction(List<Transaction> trasList,Map<String, Boolean> ethAddress,Date timestamp, EthBlock.TransactionObject item, TransactionReceipt receipt,List<Log> logs) {
        Transaction transaction = new Transaction();
    //    if (null == transaction) {
    //        logger.info("" + item.toString());
    //        return false;
    //    }
        String status = receipt.getStatus();
        if (status.equals("0x1")) { //success
            status = "1";
        } else {
            status = "0";
        }
        transaction.setHash(item.getHash());
        transaction.setIsTrunk(1);
        transaction.setTimeStamp(timestamp);
        transaction.setFromAddr(item.getFrom());
        transaction.setToAddr(item.getTo());
        transaction.setValue(new BigDecimal(item.getValue()));
        transaction.setNonce(item.getNonce().longValue());
        transaction.setGasLimit(item.getGas().longValue());
        transaction.setGasPrice(item.getGasPrice().longValue());
        transaction.setStatus(Integer.valueOf(status));
        transaction.setCumulative(receipt.getCumulativeGasUsed().longValue());
        transaction.setBlockHash(item.getBlockHash());
        transaction.setBlockNumber(item.getBlockNumber().longValue());
        transaction.setBlockIndex(item.getTransactionIndex().intValue());
        transaction.setInternal(0);
        transaction.setInput(item.getInput());
        transaction.setContract(receipt.getContractAddress());
        transaction.setGasUsed(receipt.getGasUsed().longValue());
        transaction.setType(getType(item.getInput()));
        if(item.getTo()!=null && item.getTo().equals(contractAddress) && item.getInput().equals("0x99565ff3")){
            transaction.setType(90);
            BigInteger count = new BigInteger("0") ;
            for(int i=0;i<logs.size();i++){ //有多条
                Log onelog=logs.get(i);
                if((Constants.PRE+"dd6eb07a5b1cff6970b7abdf62d8f670d00202f7404afd55741dfd27ea7a2d9b").equals(onelog.getTopics().get(0))){
                    String who="0x"+onelog.getTopics().get(1).substring(26);
                    BigInteger lockupstart=new BigInteger(onelog.getTopics().get(2).substring(2,66),16);
                    String value=onelog.getData();
                    BigInteger amount=new BigInteger(value.substring(2,66),16);//提取金额
                    BigInteger lockupamount=new BigInteger(value.substring(66,130),16);//提取金额
                    BigInteger leftamount=new BigInteger(value.substring(130),16);
                    count = count.add(amount);
                }
            }
            BigDecimal bigdec = new BigDecimal(count);
            transaction.setParam3(bigdec);
        }
        txData(ethAddress,item.getInput(), transaction,logs);
//        long st = System.currentTimeMillis();
//        transactionMapper.insertorUpdate(transaction);
        trasList.add(transaction);
//        long ed = System.currentTimeMillis();
//        logger.info("blocknumber="+item.getBlockNumber()+",hash="+item.getHash()+",spends total time ="+(ed-st));
        return transaction;
    }

    /*Get the block height on the chain*/
    private Long getCurrentBlockNumber(Web3j web3j) {
        EthBlockNumber ethBlockNumber;
        try {
            ethBlockNumber = web3j.ethBlockNumber().send();
        } catch (IOException e) {
            e.printStackTrace();
            return 0l;
        }
        if (null == ethBlockNumber) {
            return 0l;
        }
        BigInteger number = ethBlockNumber.getBlockNumber();
        if (null == number) {
            return 0l;
        }
        return number.longValue();
    }

    /*Save block reward   */
    private BigInteger saveBlockReward(String blockHash, Long blockNumber, String minerAddress,Date dates) {
        BigDecimal rewardNumber = new BigDecimal("0.0");
        BlockRewards reward = new BlockRewards();
        /*if(blockNumber<=Long.valueOf(blocktotal)){
            rewardNumber=new BigDecimal("3.40740710650912607813");
        }else{
            rewardNumber= getMinerNumber(blockNumber);
        }*/
        rewardNumber= getMinerNumber(blockNumber);
        BigDecimal fee= new BigDecimal(decimals);
        rewardNumber=rewardNumber.multiply(fee);
        if (null != reward) {
            reward.setBlockHash(blockHash);
            reward.setAddress(minerAddress);
            reward.setReward(rewardNumber);
            reward.setRewardType("BlockReward");
            reward.setRewardHash(blockHash);
            reward.setBlockNumber(blockNumber);
            reward.setIsTrunk(1);
            reward.setTimeStamp(dates);
            blockRewardsMapper.insertOrUpdate(reward);
        }
        long blockReward=rewardNumber.longValue();
        return BigInteger.valueOf(blockReward);
    }

    /*Save block fork information*/
    private int saveBlockFork(List<String> uncles, String nephewHash, Long nephewNumber) {
        int forkCount = 0;
        for (String uncleHash : uncles) {
            if (StringUtils.isEmpty(uncleHash))
                continue;
            BlockFork item = new BlockFork();
            if (null == item) {
                continue;
            }
            item.setNepHewHash(nephewHash);
            item.setNepHewNumber(nephewNumber);
            item.setIsTrunk(1);
            item.setUncleHash(uncleHash);
            item.setUncleHandled(0);
            blockForkMapper.saveOrUpdate(item);
            forkCount++;
        }
        return forkCount;
    }

    /*Save uncle block reward*/
    private BigInteger saveUnclesReward(String blockHash, Long blockNumber, int unclesNumber, String minerAddress) {
        BigDecimal rewardNumber = new BigDecimal("0.0");
        BlockRewards rewards = new BlockRewards();
//        if(blockNumber<=Long.valueOf(blocktotal)){
//            rewardNumber=new BigDecimal("3.40740710650912607813");
//        }else{
//            rewardNumber= getMinerNumber(blockNumber);
//        }
        rewardNumber= getMinerNumber(blockNumber);
        BigDecimal fee= new BigDecimal("1000000000000000000");
        rewardNumber=rewardNumber.multiply(fee);
        BlockRewards reward = new BlockRewards();
        if (null != reward) {
            reward.setBlockHash(blockHash);
            reward.setAddress(minerAddress);
            reward.setReward(rewardNumber);
            reward.setRewardType("UncleReward");
            reward.setRewardHash(blockHash);
            reward.setBlockNumber(blockNumber);
            reward.setIsTrunk(1);
           blockRewardsMapper.insertOrUpdate(reward);
        }
        long blockReward=rewardNumber.longValue();
        return BigInteger.valueOf(blockReward);
    }

    private TransactionReceipt getTransactionReceipt(Web3j web3j, String transHash) {
        EthGetTransactionReceipt ethReceipt;
        try {
            ethReceipt = web3j.ethGetTransactionReceipt(transHash).send();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        if (null == ethReceipt) {
            return null;
        }
        TransactionReceipt receipt = ethReceipt.getResult();
        if (null == receipt) {
            return null;
        }
        return receipt;
    }

    private int handleEthBalance(Web3j web3j, Set<String> addresses, Long blockNumber,Date insert) {
        logger.info("All addresses "+addresses.toString());
        int count = 0;
        for (String address : addresses) {
            AddressBalanceModifyJob.getInstance().putq(new Addresses(address,blockNumber));
        }
        return count;
    }

    private void saveFeeReward(String blockHash, String minerAddress, BigInteger fee,BigInteger number) {
        BlockRewards reward = new BlockRewards();
        if (null == reward) {
            return;
        }
        reward.setBlockHash(blockHash);
        reward.setAddress(minerAddress);
        reward.setReward(new BigDecimal(fee));
        reward.setRewardType("FeeReward");
        reward.setRewardHash(blockHash);
        reward.setBlockNumber(number.longValue());
        reward.setIsTrunk(1);
        blockRewardsMapper.insertOrUpdate(reward);
    }

    /*Save block information    */
    private boolean saveBlockFromEthBlock(EthBlock.Block block,Long blockNumber, BigInteger reward, int txCount) {
        Integer round=getRound(blockNumber.intValue());
        BigDecimal fee= new BigDecimal("1000000000000000000");
        Blocks item = new Blocks();
        BigDecimal rewardNumber = new BigDecimal("0.0");
        BigInteger number =block.getNumber();
        /*if(number.longValue()<=Long.valueOf(blocktotal)){
            rewardNumber=new BigDecimal("3.40740710650912607813");
        }else{
            rewardNumber =getMinerNumber(blockNumber);
        }*/
        rewardNumber =getMinerNumber(blockNumber);
        rewardNumber=rewardNumber.multiply(fee);
        if (null == item) {
            return false;
        }
        item.setHash(block.getHash());
        item.setBlockNumber(block.getNumber().longValue());
        item.setIsTrunk(1);
        item.setTimeStamp(new Date(block.getTimestamp().longValue() * 1000));
        item.setMinerAddress(block.getMiner());
        item.setBlockSize(block.getSize().intValue());
        item.setGasLimit(block.getGasLimit().longValue());
        item.setGasUsed(block.getGasUsed().longValue());
        item.setReward(rewardNumber);
        item.setTxsCount(txCount);
        item.setNonce(block.getNonceRaw());
        item.setDifficulty(block.getDifficulty().longValue());
        item.setTotalDifficulty(block.getTotalDifficulty().longValue());
        item.setParentHash(block.getParentHash());
        item.setRound(round);
        blockMapper.insertOrUpdate(item);
        return true;
    }

    public  BigDecimal getMinerNumber(Long nowNumber){
        try {
            long blocknum = yeartotal/perblocksecond;
            long blockreward=blockTotalNfc;
            int n = (int) (nowNumber/blocknum + (nowNumber%blocknum==0?0:1));
            BigDecimal  tbn=new BigDecimal(blockreward*(1-Math.pow(0.5,n/6.0)));
            BigDecimal  tbn_1=new BigDecimal(blockreward*(1-Math.pow(0.5,(n-1)/6.0)));
            BigDecimal  total=tbn.subtract(tbn_1).divide(new BigDecimal(blocknum),20,BigDecimal.ROUND_HALF_UP);
            return total;
        } catch (Exception e) {
            e.printStackTrace();
            return new BigDecimal("0");
        }
    }

    //Get the round of block height
    private  Integer getRound(int blockNumber){
        int rouds=0;
        if(blockNumber<=round){
            rouds=1;
            return rouds;
        }else{
            float cell=blockNumber/round;
            double nYear=(double)blockNumber/round;
            double nFee= Math.ceil(nYear);
            int round = new Double(nFee).intValue();
            return round;
        }
    }

    //Save node data
    private void saveDposNodeInfo(Long blockNumber, long nodeblock, List<String> signers)throws  Exception {
        Integer type;
        BigInteger va;
        Set<String> witNodes = new HashSet<>();
        if(blockNumber%nodeblock==0){
            if( signers!=null&&signers.size()>0){
                for(int k=0;k<signers.size();k++){
                    witNodes.add(signers.get(k).toLowerCase());
                }
            }
        }
        //Synchronize node table status
        if (witNodes.size()>0) {
            List<NfcNodeMiner> list =  nodeExitMapper.getNodeListNotExit();
            for (NfcNodeMiner node: list) {
                if(witNodes.contains(node.getNode_address().toLowerCase())){
                    if(node.getNode_type()==NodeTypeEnum.wait.getCode()){
                        NfcNodeMiner nfcNodeMiner = new NfcNodeMiner();
                        nfcNodeMiner.setNode_address(node.getNode_address());
                        nfcNodeMiner.setNode_type(NodeTypeEnum.witness.getCode());
                        nfcNodeMiner.setSync_time(new Date());
                        nodeExitMapper.updateNodeStatus(nfcNodeMiner);
                    }
                }else {
                    if(node.getNode_type()==NodeTypeEnum.witness.getCode()) {
                        NfcNodeMiner nfcNodeMiner = new NfcNodeMiner();
                        nfcNodeMiner.setNode_address(node.getNode_address());
                        nfcNodeMiner.setNode_type(NodeTypeEnum.wait.getCode());
                        nfcNodeMiner.setSync_time(new Date());
                        nodeExitMapper.updateNodeStatus(nfcNodeMiner);
                    }
                }
            }
            List<Transaction> deposit = transactionMapper.getTransactionByTxType(SscOperatorEnum.Deposit.getCode());
            String pledge = null;
            if(deposit.size()>0){
                pledge = deposit.get(0).getParam1();
            }else{
                throw  new Exception("no Deposit config set");
            }
            for (String witNode:witNodes) {
                NfcNodeMinerQueryForm queryForm = new  NfcNodeMinerQueryForm();
                queryForm.setNode_address(witNode);
                if(nodeExitMapper.getNode(queryForm) == null){
                    NfcNodeMiner node = new NfcNodeMiner() ;
                    node.setNode_address(Constants.pre0XtoNX(witNode));
                    node.setNode_type(NodeTypeEnum.witness.getCode());
                    node.setBlocknumber(blockNumber);
                    node.setPledge_amount(new BigDecimal(pledge));
                    node.setSync_time(new Date());
                    nodeExitMapper.saveOrUpdateNodeMiner(node);
                }
            }
        }

    }

    private Long convertToLong(String value) {
        if (null != value)
            return Numeric.decodeQuantity(value).longValue();
        return null;
    }

    private String truncateAddress(String address) {
        return Constants.PRE + address.substring(address.length() - 40);
    }


    private BigDecimal convertToBigDecimal(String value) {
        if (null != value)
            return new BigDecimal(Numeric.decodeQuantity(value));
        return null;
    }


    public static String hexStr2Str(String s) {
        if(s==null || s.isEmpty()){
            return null;
        }
        s=s.replaceAll("0+$", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "UTF-8");
            new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    public Integer getType(String inputParam) {
        String input = "";
        if (inputParam == null || inputParam == "") {
            return 0;
        }
        if (inputParam.startsWith("0x")&& inputParam.length()>2) {
            inputParam = inputParam.substring(2);
            input = inputParam;
        }else{
            return 0;
        }
        String result = hexStr2Str(input);
        String[] strList = null;
        if (result.startsWith("ufo") && result.length() >= 16) {
            strList = result.split(":");
            if (strList.length >= 3 && Arrays.asList(strList).contains("exit")) {
                return 11;
            }
            if (strList.length >= 3 && Arrays.asList(strList).contains("declare")) {
                return 12;
            }
            String param = "0";
            if (strList.length >= 6 && Arrays.asList(strList).contains("proposal_type")) {
               param=strList[5];
                logger.info("data:" + param);
                if (param.equals("1")) {
                    return 13;
                } else if (param.equals("2")) {
                    return 14;
                } else {
                    return 0;
                }

            } else {
                return 0;
            }

        }
        return 0;
    }
}
