package com.imooc.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imooc.pojo.*;
import com.imooc.pojo.Form.*;
import com.imooc.pojo.Param.TransactionQueryParam;
import com.imooc.pojo.Param.TransferMinerQueryParam;
import com.imooc.pojo.vo.*;
import com.imooc.service.*;
import com.imooc.utils.Constants;
import com.imooc.utils.DateUtil;
import com.imooc.utils.ResultMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/platform")
@Api(tags="AllDataController")

public class AllDataController {

    @Value("${ipaddress}")
    private String ipaddress;
    private Logger logger = LoggerFactory.getLogger(AllDataController.class);

    @Autowired
    private BlockService blockService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AddrBalancesService addrBalancesService;

    @Autowired
    private TransferMinerService transferMinerService;

    @Autowired
    private DposVoterService dposVoterService;

    @Autowired
    private PunishedService punishedService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private NodeExitService nodeExitService;

    @Autowired
    private PledgeDataService pledgeDataService;

    @Autowired
    private NfcExchgListService nfcService;

    @Autowired
    private NfcFlowMinerService nfcFlowMinerService;
    @RequestMapping(value = "/getBwPlgeRange", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultMap getBwPlgeRange() throws IOException {
        return blockService.getBwPlgeRange();
    }

    @ApiOperation("getDatas")
    @RequestMapping(value = "/getDatas", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultMap getDatas() throws IOException {
        return blockService.getDatas();
    }

    @ResponseBody
    @PostMapping("/getBlockNumber")
    public ResultMap getBlockNumber() {
        return blockService.getBlockNumber();
    }

    @ApiOperation(value = "getBlockList", notes = "getBlockList")
    @ResponseBody
    @PostMapping("/getBlockList")
    public ResultMap<Page<BlocksVo>> getBlockList(@Valid @RequestBody BlockQueryForm blockQueryForm) {
        return blockService.pageList(blockQueryForm);
    }

    @ApiOperation(value = "Latest transaction list", notes = "Latest transaction list")
    @ApiImplicitParam(name = "blockQueryForm", value = "Query parameter", required = true, dataType = "BlockQueryForm")
    @ResponseBody
    @PostMapping("/getTransactionList")
    public ResultMap<Page<Transaction>> getTransactionList(@Valid @RequestBody BlockQueryForm blockQueryForm) {
        blockQueryForm.setToAddr(Constants.pre0XtoNX(blockQueryForm.getToAddr()));
        blockQueryForm.setFromAddr(Constants.pre0XtoNX(blockQueryForm.getFromAddr()));
        blockQueryForm.setAddress(Constants.pre0XtoNX(blockQueryForm.getAddress()));
        return transactionService.pageList(blockQueryForm);
    }

    /**提取列表
     * @return
     */
    @ApiOperation(value = "Latest transaction list", notes = "Latest transaction list")
    @ApiImplicitParam(name = "blockQueryForm", value = "Query parameter", required = true, dataType = "BlockQueryForm")
    @ResponseBody
    @PostMapping("transactionList")
    public ResultMap  transactionList(@Valid @RequestBody QueryForm queryForm) {

        return transactionService.page(queryForm);
    }

    /**兑换列表
     * @return
     */
    @ApiOperation(value = "Latest transaction list", notes = "Latest transaction list")
    @ApiImplicitParam(name = "blockQueryForm", value = "Query parameter", required = true, dataType = "BlockQueryForm")
    @ResponseBody
    @PostMapping("getNfcExchgList")
    public ResultMap  getNfcExchgList(@Valid @RequestBody QueryForm queryForm) {

        return nfcService.getContractPageTokenList(queryForm);
    }

    /**统计锁仓总量，和已提取总量
     * @return
     */
    @ApiOperation(value = "Latest transaction list", notes = "Latest transaction list")
    @ApiImplicitParam(name = "blockQueryForm", value = "Query parameter", required = true, dataType = "BlockQueryForm")
    @ResponseBody
    @PostMapping("statisLock")
    public ResultMap  statisLock(@Valid @RequestBody QueryForm queryForm) {

        return nfcService.statisLock(queryForm);
    }

    @ApiOperation(value = "getAddressList", notes = "getAddressList")
    @ResponseBody
    @PostMapping("/getAddressList")
    public ResultMap<Page<AddressVo>> getAddressList(@Valid @RequestBody BlockQueryForm blockQueryForm) {
        blockQueryForm.setAddress(Constants.pre0XtoNX(blockQueryForm.getAddress()));
        return accountService.pageList(blockQueryForm);
    }

    @ApiOperation(value = "searchByParam", notes = "searchByParam")
    @ResponseBody
    @PostMapping("/searchByParam")
    public ResultMap getTransactions(@Valid @RequestBody TransactionQueryForm transactionQueryForm) {
        transactionQueryForm.toParam(TransactionQueryParam.class);
        return transactionService.getTransaction(transactionQueryForm);
    }

    @ApiOperation(value = "getBlockInfoByBlockNumber", notes = "getBlockInfoByBlockNumber")
    @ResponseBody
    @PostMapping("/searchByNumber")
    public ResultMap getBlockInfoByBlockNumber(@RequestParam(value = "blockNumber", required = true) Long blockNumber) {
        return blockService.getBlockInfoByBlockNumber(blockNumber);
    }

    @ApiOperation(value = "getTransactionsByBlockNumber", notes = "getTransactionsByBlockNumber")
    @ResponseBody
    @PostMapping("/getTransactionByBlockNumber")
    public ResultMap<Page<Transaction>> getTransactionsByBlockNumber(@Valid @RequestBody TransactionQueryForm transactionQueryForm) {
        transactionQueryForm.toParam(TransactionQueryParam.class);
        return transactionService.getTransactionByBlockNumber(transactionQueryForm);
    }

    @ApiOperation(value = "getAddressInfoByAddressHash", notes = "getAddressInfoByAddressHash")
    @ResponseBody
    @PostMapping("/getAddressInfoByAddressHash")
    public ResultMap getAddressInfoByAddressHash(@Valid @RequestBody TransactionQueryForm transactionQueryForm) {
        transactionQueryForm.toParam(TransactionQueryParam.class);
        transactionQueryForm.setAddressHash(Constants.pre0XtoNX(transactionQueryForm.getAddressHash()));
        return transactionService.getAddressInfoByHash(transactionQueryForm);
    }

    @ApiOperation(value = "getTransactionByAddressHash", notes = "getTransactionByAddressHash")
    @ResponseBody
    @PostMapping("/getTransactionByAddressHash")
    public ResultMap<Page<Transaction>> getTransactionByAddressHash(@Valid @RequestBody TransactionQueryForm transactionQueryForm) {
        transactionQueryForm.toParam(TransactionQueryParam.class);
        transactionQueryForm.setAddressHash(Constants.pre0XtoNX(transactionQueryForm.getAddressHash()));
        return transactionService.getTransactionInfoByAddressHash(transactionQueryForm);
    }

    @ApiOperation(value = "getTransactionByTxHash", notes = "getTransactionByTxHash")
    @ResponseBody
    @PostMapping("/getTransactionByTxHash")
    public ResultMap getTransactionByTxHash(@Valid @RequestBody TransactionQueryForm transactionQueryForm) {
        transactionQueryForm.toParam(TransactionQueryParam.class);
        transactionQueryForm.setTxHash(transactionQueryForm.getTxHash());
        return transactionService.getTransactionInfoByTxHash(transactionQueryForm);
    }

    @ResponseBody
    @PostMapping("/getBlanceForAddressHash")
    public ResultMap getEchartForBalance(@RequestParam(value = "addressHash", required = true) String addressHash) {
        return addrBalancesService.getBalanceForAddress(Constants.pre0XtoNX(addressHash));
    }

    @ApiOperation(value = "getForAddressDetail", notes = "getForAddressDetail")
    @ResponseBody
    @PostMapping("/selectForAddressDetail")
    public ResultMap getForAddressDetail(@Valid @RequestBody TransactionQueryForm transactionQueryForm) throws Exception {
        transactionQueryForm.toParam(TransactionQueryParam.class);
        transactionQueryForm.setAddressHash(Constants.pre0XtoNX(transactionQueryForm.getAddressHash()));
        return transactionService.getForAddressDetail(transactionQueryForm);
    }

    @ApiOperation(value = "Flow mining", notes = "Flow mining")
    @ResponseBody
    @PostMapping("/getMinerInfoForAddress")
    public ResultMap getMinerInfoForAddress(@Valid @RequestBody TransferMinerForm transferMinerForm) throws Exception {
        return transferMinerService.getMinerInfoForAddress(transferMinerForm);
    }

    @ResponseBody
    @PostMapping("/getMinerInfoForWallet")
    public ResultMap getMinerInfoForWallet(@Valid @RequestBody MinerQueryForm minerQueryForm) throws Exception {
        minerQueryForm.setAddress(Constants.pre0XtoNX(minerQueryForm.getAddress()));
        return transferMinerService.getAllMinerInfoWallet(minerQueryForm);
    }

    @ApiOperation(value = "getDataForNfc", notes = "getDataForNfc")
    @ResponseBody
    @PostMapping("/getDataForNfc")
    public ResultMap getDataForNfc() throws Exception {
        return blockService.getDataForNfc();
    }


    @ApiOperation(value = "getAllParamters", notes = "getAllParamters")
    @ResponseBody
    @PostMapping("/getAllParamters")
    public ResultMap getAllParamters(@RequestParam(value = "types", required = true) long types, @RequestParam(value = "language", required = true) String language) throws Exception {
        return blockService.getAllParamters(types, language);
    }


    @ApiOperation(value = "getTransactionValue", notes = "getTransactionValue")
    @ResponseBody
    @PostMapping("/getTransactionValue")
    public ResultMap getTransactionValue() throws Exception {
        return transactionService.getTransactionValue();
    }


    @ApiOperation(value = "getNewAddress", notes = "getNewAddress")
    @ResponseBody
    @PostMapping("/getNewAddress")
    public ResultMap getNewAddress() throws Exception {
        return transactionService.getNewAccounts();
    }

    @ResponseBody
    @PostMapping("/lockAndPledgeInfo")
    public ResultMap<Page<TransferMiner>> getLockAndPledgeInfo(@Valid @RequestBody LockUpTransferForm lockUpTransferForm) throws Exception {
        return transferMinerService.getLockAndPledgeInfo(lockUpTransferForm);
    }


    @ResponseBody
    @PostMapping("/getMinerAndFeeNfc")
    public ResultMap<Page<BlockAndMinerVo>> getMinerAndFeeNfc(@Valid @RequestBody BlockQueryForm blockQueryForm) throws Exception {
        return blockService.getMinerAndFeeNfc(blockQueryForm);
    }

    @ResponseBody
    @PostMapping("/getDposVoterInfo")
    public ResultMap<Page<DposVotes>> getDposVoterInfo(@Valid @RequestBody BlockQueryForm blockQueryForm) throws IOException {
        return dposVoterService.getDposVoterInfo(blockQueryForm);
    }

    @ResponseBody
    @PostMapping("/getDposNodeInfo")
    public ResultMap<Page<DposNode>> getDposNodeInfo(@Valid @RequestBody BlockQueryForm blockQueryForm) throws IOException {
        return dposVoterService.getDposNodeInfo(blockQueryForm);
    }

    @ResponseBody
    @PostMapping("/lockNfcMinerInfo")
    public ResultMap<Page<TransferMinerVo>> getLockNfcMinerInfo(@Valid @RequestBody BlockQueryForm blockQueryForm) throws Exception {
        blockQueryForm.setAddress(Constants.pre0XtoNX(blockQueryForm.getAddress()));
        blockQueryForm.setRevenueAddress(Constants.pre0XtoNX(blockQueryForm.getRevenueAddress()));
        return transferMinerService.getLockNfcMinerInfo(blockQueryForm);
    }

    @ResponseBody
    @PostMapping("/getNfcMinerLockSum")
    public ResultMap<Page<TransferMinerVo>> getNfcMinerLockSum(@Valid @RequestBody BlockQueryForm blockQueryForm) throws Exception {
        blockQueryForm.setAddress(Constants.pre0XtoNX(blockQueryForm.getAddress()));
        blockQueryForm.setRevenueAddress(Constants.pre0XtoNX(blockQueryForm.getRevenueAddress()));
        return transferMinerService.getNfcMinerLockSum(blockQueryForm);
    }

    @ResponseBody
    @PostMapping("/lockNfcMinersByRevAddress")
    public ResultMap<Page<TransferMinerVo>> getLockNfcMinersByRevAddress(@Valid @RequestBody BlockQueryForm blockQueryForm) throws Exception {
        blockQueryForm.setRevenueAddress(Constants.pre0XtoNX(blockQueryForm.getRevenueAddress()));
        return transferMinerService.getLockNfcMinersByRevAddress(blockQueryForm);
    }

    @ResponseBody
    @PostMapping("/lockSummaryByRevAddress")
    public ResultMap getLockSummaryByRevAddress(@Valid @RequestBody BlockQueryForm blockQueryForm) throws Exception {
        blockQueryForm.setRevenueAddress(Constants.pre0XtoNX(blockQueryForm.getRevenueAddress()));
        return transferMinerService.getLockSummaryByRevAddress(blockQueryForm);
    }

    @ResponseBody
    @PostMapping("/lockNfcMinerInfoForTotal")
    public ResultMap<Page<TransferMinerVo>> lockNfcMinerInfoForTotal(@Valid @RequestBody BlockQueryForm blockQueryForm) throws Exception {
        blockQueryForm.setAddress(Constants.pre0XtoNX(blockQueryForm.getAddress()));
        return transferMinerService.getLockNfcMinerInfoForTotal(blockQueryForm);
    }

    @ResponseBody
    @PostMapping("/lockNfcMinerSerach")
    public ResultMap<Page<TransferMinerVo>> lockNfcMinerSerach(@Valid @RequestBody BlockQueryForm blockQueryForm) throws Exception {
        blockQueryForm.setAddress(Constants.pre0XtoNX(blockQueryForm.getAddress()));
        return transferMinerService.lockNfcMinerSerach(blockQueryForm);
    }

    @ResponseBody
    @PostMapping("/getVoterListInfo")
    public ResultMap<Page<DposVoterVo>> getVoterListInfo(@Valid @RequestBody BlockQueryForm blockQueryForm) throws Exception {
        return dposVoterService.getVoterListInfo(blockQueryForm);

    }

    @ResponseBody
    @PostMapping("/getOverData")
    public ResultMap<Page<TransferMiner>> getOverData(@Valid @RequestBody BlockQueryForm blockQueryForm) throws Exception {
        blockQueryForm.setAddress(Constants.pre0XtoNX(blockQueryForm.getAddress()));
        return transferMinerService.getOverData(blockQueryForm);

    }

    @ResponseBody
    @PostMapping("/getDposVoterTotalForRound")
    public ResultMap getDposVoterTotalForRound() throws IOException {
        return dposVoterService.getDposVoterTotalForRound();
    }

    @ResponseBody
    @PostMapping("/getPledgeExit")
    public ResultMap getPledgeExit() throws Exception {
        return transferMinerService.getPledgeExit();
    }

    @ResponseBody
    @PostMapping("/getDposVoterAddressTotal")
    public ResultMap getDposVoterAddressTotal(@Valid @RequestBody BlockQueryForm blockQueryForm) throws IOException {
        return dposVoterService.getDposVoterAddressTotal(blockQueryForm);
    }


    @ApiOperation(value = "getPunished", notes = "getPunished")
    @ResponseBody
    @PostMapping("/nfc/getPunished")
    public ResultMap<Page<Punished>> getPunishedInfo(@Valid @RequestBody BlockQueryForm blockQueryForm) throws IOException {
        blockQueryForm.setAddress(Constants.pre0XtoNX(blockQueryForm.getAddress()));
        return punishedService.getPunishedInfo(blockQueryForm);
    }


    @ApiOperation(value = "getTokenList", notes = "getTokenList")
    @ResponseBody
    @PostMapping("/getTokensInfo")
    public ResultMap<Page<Tokens>> getTokenList(@Valid @RequestBody BlockQueryForm blockQueryForm) throws IOException {
        blockQueryForm.setAddress(Constants.pre0XtoNX(blockQueryForm.getAddress()));
        return tokenService.getTokenList(blockQueryForm);
    }

    @ApiOperation(value = "getTokenContractList", notes = "getTokenContractList")
    @ResponseBody
    @PostMapping("/getContractToken")
    public ResultMap<Page<TokenContract>> getTokenContractList(@Valid @RequestBody BlockQueryForm blockQueryForm) throws IOException {
        blockQueryForm.setAddress(Constants.pre0XtoNX(blockQueryForm.getAddress()));
        return tokenService.getTokenContractList(blockQueryForm);
    }


    @ApiOperation(value = "getAddressesList")
    @ResponseBody
    @PostMapping("/getAddressInfoList")
    public ResultMap<Page<Addresses>> getAddressesList(@Valid @RequestBody BlockQueryForm blockQueryForm) throws IOException {
        blockQueryForm.setAddress(Constants.pre0XtoNX(blockQueryForm.getAddress()));
        return accountService.getContractPageTokenList(blockQueryForm);
    }

    @ApiOperation(value = "getTransTokenListForContract")
    @ResponseBody
    @PostMapping("/getTransTokenListForContract")
    public ResultMap<Page<TransToken>> getTransTokenListForContract(@Valid @RequestBody BlockQueryForm blockQueryForm) throws IOException {
        blockQueryForm.setAddress(Constants.pre0XtoNX(blockQueryForm.getAddress()));
        return tokenService.getTransTokenListForContract(blockQueryForm);
    }

    @ApiOperation(value = "getContractMap", notes = "getContractMap")
    @ResponseBody
    @PostMapping("/getTokensDescription")
    public ResultMap<Tokens> getContractMap(@Valid @RequestBody BlockQueryForm blockQueryForm) throws IOException {
        blockQueryForm.setAddress(Constants.pre0XtoNX(blockQueryForm.getAddress()));
        return tokenService.getContractMap(blockQueryForm);
    }

    @ResponseBody
    @PostMapping("/getNodeExitForRn")
    public ResultMap<NodeExit> getNodeExit(@Valid @RequestBody BlockQueryForm blockQueryForm) throws IOException {
        blockQueryForm.setAddress(Constants.pre0XtoNX(blockQueryForm.getAddress()));
        return nodeExitService.getNodeExit(blockQueryForm);
    }

    @ApiOperation(value = "Node list query", notes = "Node list query")
    @ResponseBody
    @PostMapping("/node/getcadnodelist")
    public ResultMap<Page<NfcNodeMiner>> getcadnodelist(@Valid @RequestBody NfcNodeMinerQueryForm nfcNodeMinerQueryForm){
        nfcNodeMinerQueryForm.setNode_address(Constants.pre0XtoNX(nfcNodeMinerQueryForm.getNode_address()));
        nfcNodeMinerQueryForm.setRevenue_address(Constants.pre0XtoNX(nfcNodeMinerQueryForm.getRevenue_address()));
        nfcNodeMinerQueryForm.setAddress(Constants.pre0XtoNX(nfcNodeMinerQueryForm.getAddress()));
        return nodeExitService.getNodeList(nfcNodeMinerQueryForm);
    }

    @ApiOperation(value = "Node detailed query", notes = "Node detailed query")
    @ResponseBody
    @PostMapping("/node/getcadnode")
    public ResultMap getcadnode(@Valid @RequestBody NfcNodeMinerQueryForm nfcNodeMinerQueryForm) throws Exception {
        nfcNodeMinerQueryForm.setNode_address(Constants.pre0XtoNX(nfcNodeMinerQueryForm.getNode_address()));
        return nodeExitService.getNode(nfcNodeMinerQueryForm);
    }

    @ApiOperation(value = "Node getcadnodeByRev query", notes = "Node getcadnodeByRev query")
    @ResponseBody
    @PostMapping("/node/getcadnodeByRev")
    public ResultMap getcadnodeByRev(@Valid @RequestBody NfcNodeMinerQueryForm nfcNodeMinerQueryForm) throws Exception {
        nfcNodeMinerQueryForm.setRevenue_address(Constants.pre0XtoNX(nfcNodeMinerQueryForm.getRevenue_address()));
        return nodeExitService.getNodeByRev(nfcNodeMinerQueryForm);
    }

    @ApiOperation(value = "Node pledge amount", notes = "Node pledge amount")
    @ResponseBody
    @PostMapping("/node/getNodePledgeAmount")
    public ResultMap getNodePledgeAmount(@Valid @RequestBody NfcNodeMinerQueryForm nfcNodeMinerQueryForm)throws Exception  {
        nfcNodeMinerQueryForm.setNode_address(Constants.pre0XtoNX(nfcNodeMinerQueryForm.getNode_address()));
        return  nodeExitService.getNodePledgeAmount(nfcNodeMinerQueryForm);
    }

    @ApiOperation(value = "Node getNodeExistRealse query", notes = "Node getNodeExistRealse query")
    @ResponseBody
    @PostMapping("/node/getNodeExistRealse")
    public ResultMap getNodeExistRealse(@Valid @RequestBody NfcNodeMinerQueryForm nfcNodeMinerQueryForm) throws Exception {
        nfcNodeMinerQueryForm.setAddress(Constants.pre0XtoNX(nfcNodeMinerQueryForm.getAddress()));
        return nodeExitService.getNodeExistRealse(nfcNodeMinerQueryForm);
    }

    @ResponseBody
    @PostMapping("/getTokenIsExitCount")
    public ResultMap getTokenIsExitCount(@Valid @RequestBody TokenQueryForm tokenQueryForm) {
        return tokenService.getTokenIsExitCount(tokenQueryForm);
    }

    @ResponseBody
    @PostMapping("/getTransactionByToAddress")
    public ResultMap getTransactionByToAddress(@Valid @RequestBody BlockQueryForm blockQueryForm) {
        blockQueryForm.setFromAddr(Constants.pre0XtoNX(blockQueryForm.getFromAddr()));
        blockQueryForm.setToAddr(Constants.pre0XtoNX(blockQueryForm.getToAddr()));
        return transactionService.getTransactionByToAddress(blockQueryForm);
    }

    @ResponseBody
    @PostMapping("/getFromAndToTransaction")
    public ResultMap<Page<Transaction>> getFromAndToTransaction(@Valid @RequestBody BlockQueryForm blockQueryForm) {
        blockQueryForm.setAddress(Constants.pre0XtoNX(blockQueryForm.getAddress()));
        blockQueryForm.setFromAddr(Constants.pre0XtoNX(blockQueryForm.getFromAddr()));
        blockQueryForm.setToAddr(Constants.pre0XtoNX(blockQueryForm.getToAddr()));
        return transactionService.getFromAndToTransaction(blockQueryForm);
    }

    @ResponseBody
    @PostMapping("/getAllTotalForReceive")
    public ResultMap<Page<Transaction>> getAllTotalForReceive(@RequestParam(value = "address", required = true) String address) {
        return transferMinerService.getAllTotalForReceive(Constants.pre0XtoNX(address));
    }

    @ResponseBody
    @PostMapping("/getDposNodeInfoWallet")
    public ResultMap<Page<DposNode>> getDposNodeInfoWallet(@Valid @RequestBody BlockQueryForm blockQueryForm) throws IOException {
        return dposVoterService.getDposNodeInfoWallet(blockQueryForm);
    }

    @ResponseBody
    @PostMapping("/getVoterListInfoForRound")
    public ResultMap<DposVoterVo> getVoterListInfoForRound(@Valid @RequestBody BlockQueryForm blockQueryForm) throws Exception {
        return dposVoterService.getVoterListInfoForRound(blockQueryForm);
    }

    @ResponseBody
    @PostMapping("/getDposVoterForCandidate")
    public ResultMap<Page<DposVotes>> getDposVoterForCandidate(@Valid @RequestBody BlockQueryForm blockQueryForm) throws Exception {
        return dposVoterService.getDposVoterForCandidate(blockQueryForm);
    }

    @ResponseBody
    @PostMapping("/getDposByRoundAndCandidateForAddress")
    public ResultMap<Page<DposVotes>> getDposByRoundAndCandidateForAddress(@Valid @RequestBody BlockQueryForm blockQueryForm) throws Exception {
        return dposVoterService.getDposByRoundAndCandidateForAddress(blockQueryForm);
    }

    @ResponseBody
    @PostMapping("/getDposNodeForAddress")
    public ResultMap<Page<DposVotesWallet>> getDposNodeForAddress(@Valid @RequestBody BlockQueryForm blockQueryForm) throws Exception {
        return dposVoterService.getDposVoterWallet(blockQueryForm);
    }

    @ResponseBody
    @PostMapping("/getVotersInfoPageList")
    public ResultMap<Page<DposVotesWallet>> getVotersInfoPageList(@Valid @RequestBody DposVoterQueryForm dposVoterQueryForm) throws Exception {
        return dposVoterService.getVotersInfoPageList(dposVoterQueryForm);
    }

    @ResponseBody
    @PostMapping("/getRewardVoterPageList")
    public ResultMap<Page<Blocks>> getRewardVoterPageList(@Valid @RequestBody DposVoterQueryForm dposVoterQueryForm) throws Exception {
        return blockService.getRewardVoterPageList(dposVoterQueryForm);
    }

    @ResponseBody
    @PostMapping("/lockNfcMinerInfoWallet")
    public ResultMap<Page<TransferMinerVo>> lockNfcMinerInfoWallet(@Valid @RequestBody BlockQueryForm blockQueryForm) throws Exception {
        blockQueryForm.setAddress(Constants.pre0XtoNX(blockQueryForm.getAddress()));
        return transferMinerService.getLockNfcMinerInfoWallet(blockQueryForm);
    }

    @ResponseBody
    @PostMapping("/getTransactionByTokenHash")
    public ResultMap getTransactionByTokenHash(@Valid @RequestBody TransactionQueryForm transactionQueryForm) {
        transactionQueryForm.toParam(TransactionQueryParam.class);
        transactionQueryForm.setTxHash(Constants.pre0XtoNX(transactionQueryForm.getTxHash()));
        return transactionService.getTransactionByTokenHash(transactionQueryForm);
    }


    @ResponseBody
    @PostMapping("/getTransactionByTokenHashInfo")
    public ResultMap getTransactionByTokenHashInfo(@RequestParam(value = "txHash", required = true) String txHash) {
        return transactionService.getTransactionByTokenHashInfo(Constants.pre0XtoNX(txHash));
    }

    @ResponseBody
    @PostMapping("/validateCaladateAddress")
    public ResultMap valilockNfcMinerInfodateCaladateAddress(@RequestParam(value = "address", required = false) String address) {
        return transactionService.validateCaladateAddress(Constants.pre0XtoNX(address));
    }

    @ResponseBody
    @PostMapping("/pagePledgeInfoList")
    public ResultMap <Page<PledgeTotalData>>pagePledgeInfoList(@Valid @RequestBody PledgeQueryForm pledgeQueryForm) {
        pledgeQueryForm.setAddress(Constants.pre0XtoNX(pledgeQueryForm.getAddress()));
       return pledgeDataService.selectPagePledgeList(pledgeQueryForm);
    }


    @ResponseBody
    @PostMapping("/pagePledgeInfoDetail")
    public ResultMap <PledgeTotalData>pagePledgeInfoDetail(@Valid @RequestBody PledgeQueryForm pledgeQueryForm) {
        pledgeQueryForm.setAddress(Constants.pre0XtoNX(pledgeQueryForm.getAddress()));
        return pledgeDataService.pagePledgeInfoDetail(pledgeQueryForm);
    }

    @ResponseBody
    @PostMapping("/pagePledgeDetail")
    public ResultMap <PledgeData>pagePledgeEnDetail(@Valid @RequestBody PledgeRnQueryForm pledgeRnQueryForm) {
        pledgeRnQueryForm.setAddress(Constants.pre0XtoNX(pledgeRnQueryForm.getAddress()));
        return pledgeDataService.pagePledgeEnDetail(pledgeRnQueryForm);
    }

    @ApiOperation(value = "getflowMinerlist", notes = "getflowMinerlist")
    @ResponseBody
    @PostMapping("/flow/getflowMinerlist")
    public ResultMap getflowMinerlist(@Valid @RequestBody NfcFlowMinerQueryForm nfcFlowMinerQueryForm) {
        nfcFlowMinerQueryForm.setMiner_addr(Constants.pre0XtoNX(nfcFlowMinerQueryForm.getMiner_addr()));
        nfcFlowMinerQueryForm.setRevenue_address(Constants.pre0XtoNX(nfcFlowMinerQueryForm.getRevenue_address()));
        return nfcFlowMinerService.pageList(nfcFlowMinerQueryForm);
    }

    @ApiOperation(value = "getflowMinerRanklist", notes = "getflowMinerRanklist")
    @ResponseBody
    @PostMapping("/flow/getflowMinerRanklist")
    public ResultMap getflowMinerRanklist(@Valid @RequestBody NfcFlowMinerQueryForm nfcFlowMinerQueryForm) {
        nfcFlowMinerQueryForm.setMiner_addr(Constants.pre0XtoNX(nfcFlowMinerQueryForm.getMiner_addr()));
        nfcFlowMinerQueryForm.setRevenue_address(Constants.pre0XtoNX(nfcFlowMinerQueryForm.getRevenue_address()));
        nfcFlowMinerQueryForm.setTime(DateUtil.getFormatDateTime(DateUtil.addDaysToDate(new Date(),-1), "yyyy-MM-dd"));
        return nfcFlowMinerService.minerRankPageList(nfcFlowMinerQueryForm);
    }

    @ApiOperation(value = "Miner flow mining query", notes = "Miner flow mining query")
    @ResponseBody
    @PostMapping("/flow/pageNfcCltList")
    public ResultMap pageNfcCltList(@Valid @RequestBody NfcFlowMinerQueryForm nfcFlowMinerQueryForm) {
        nfcFlowMinerQueryForm.setMiner_addr(Constants.pre0XtoNX(nfcFlowMinerQueryForm.getMiner_addr()));
        nfcFlowMinerQueryForm.setRevenue_address(Constants.pre0XtoNX(nfcFlowMinerQueryForm.getRevenue_address()));
        return nfcFlowMinerService.pageNfcCltList(nfcFlowMinerQueryForm);
    }

    @ApiOperation(value = "pageNfcCltDetailList", notes = "pageNfcCltDetailList")
    @ResponseBody
    @PostMapping("/flow/pageNfcCltDetailList")
    public ResultMap pageNfcCltDetailList(@Valid @RequestBody NfcFlowMinerQueryForm nfcFlowMinerQueryForm) {
        nfcFlowMinerQueryForm.setMiner_addr(Constants.pre0XtoNX(nfcFlowMinerQueryForm.getMiner_addr()));
        return nfcFlowMinerService.pageNfcCltDetailList(nfcFlowMinerQueryForm);
    }

    @ApiOperation(value = "Miner flow mining details", notes = "Miner flow mining details")
    @ResponseBody
    @PostMapping("/flow/nfcCltDetail")
    public ResultMap nfcCltDetail(@Valid @RequestBody NfcFlowMinerQueryForm nfcFlowMinerQueryForm) {
        return nfcFlowMinerService.nfcFlowCltDetail(nfcFlowMinerQueryForm);
    }

    @ApiOperation(value = "Miner mining day statistics query ", notes = "Miner mining day statistics query ")
    @ResponseBody
    @PostMapping("/flow/getMinerDayStatislist")
    public ResultMap getMinerDayStatislist(@Valid @RequestBody NfcFlowMinerQueryForm nfcFlowMinerQueryForm) {
        nfcFlowMinerQueryForm.setMiner_addr(Constants.pre0XtoNX(nfcFlowMinerQueryForm.getMiner_addr()));
        return nfcFlowMinerService.getMinerDayStatislist(nfcFlowMinerQueryForm);
    }

    @ApiOperation(value = "Overview query", notes = "Overview of query by income address")
    @ResponseBody
    @PostMapping("/my/outLine")
    public ResultMap outLine(@Valid @RequestBody NfcFlowMinerQueryForm nfcFlowMinerQueryForm) {
        nfcFlowMinerQueryForm.setRevenue_address(Constants.pre0XtoNX(nfcFlowMinerQueryForm.getRevenue_address()));
        return nfcFlowMinerService.outLine(nfcFlowMinerQueryForm);
    }

    @ApiOperation(value = "ful conversion ratio query", notes = "ful conversion ratio query")
    @ResponseBody
    @PostMapping("/ful/getExchRate")
    public ResultMap getExchRate(@Valid @RequestBody BlockQueryForm blockQueryForm) {
         return transactionService.getExchRate(blockQueryForm.getTxType());
    }

    @ApiOperation(value = "Recharge record query", notes = "Recharge record query")
    @ResponseBody
    @PostMapping("/getTxRecord")
    public ResultMap getTxRecord(@Valid @RequestBody BlockQueryForm blockQueryForm) {
        blockQueryForm.setAddress(Constants.pre0XtoNX(blockQueryForm.getAddress()));
        blockQueryForm.setParam1(Constants.pre0XtoNX(blockQueryForm.getParam1()));
        blockQueryForm.setFromAddr(Constants.pre0XtoNX(blockQueryForm.getFromAddr()));
        return transactionService.pageList(blockQueryForm);
    }

    @ApiOperation(value = "My list of miners", notes = "My list of miners")
    @ResponseBody
    @PostMapping("/my/getminerlist")
    public ResultMap getminerlist(@Valid @RequestBody NfcFlowMinerQueryForm nfcFlowMinerQueryForm) {
        nfcFlowMinerQueryForm.setMiner_addr(Constants.pre0XtoNX(nfcFlowMinerQueryForm.getMiner_addr()));
        nfcFlowMinerQueryForm.setRevenue_address(Constants.pre0XtoNX(nfcFlowMinerQueryForm.getRevenue_address()));
        return nfcFlowMinerService.pageList(nfcFlowMinerQueryForm);
    }

    @ApiOperation(value = "Miner details", notes = "Miner details")
    @ResponseBody
    @PostMapping("/my/getminerdetail")
    public ResultMap getminerdetail(@Valid @RequestBody NfcFlowMinerQueryForm nfcFlowMinerQueryForm) {
        nfcFlowMinerQueryForm.setMiner_addr(Constants.pre0XtoNX(nfcFlowMinerQueryForm.getMiner_addr()));
        nfcFlowMinerQueryForm.setRevenue_address(Constants.pre0XtoNX(nfcFlowMinerQueryForm.getRevenue_address()));
        return nfcFlowMinerService.getNfcFlowMinerDetail(nfcFlowMinerQueryForm);
    }

    @ApiOperation(value = "Bandwidth (M) is calculated as nfc", notes = "Bandwidth (M) is calculated as nfc")
    @ResponseBody
    @PostMapping("/bandWidthToNfc")
    public ResultMap bandWidthToNfc(@Valid @RequestBody JSONObject json) {
        return nfcFlowMinerService.bandWidthToNfc(json.getLong("bandWidth"));
    }

    @ApiOperation(value = "getArea", notes = "getArea")
    @ResponseBody
    @PostMapping("/getArea")
    public ResultMap getArea() {
        return nfcFlowMinerService.getArea();
    }

    @ApiOperation(value = "getOperatorConfig", notes = "getOperatorConfig")
    @ResponseBody
    @PostMapping("/getOperatorConfig")
    public ResultMap getOperatorConfig() {
        return nfcFlowMinerService.getOperatorConfig();
    }

    @ApiOperation(value = "getNfcNetStatics", notes = "getNfcNetStatics")
    @ResponseBody
    @PostMapping("/getNfcNetStatics")
    public ResultMap getNfcNetStatics(@Valid @RequestBody NfcNetStaticsForm net) {
        return nfcFlowMinerService.getNfcNetStatics(net);
    }

    @ApiOperation(value = "getNetServiceRank", notes = "getNetServiceRank")
    @ResponseBody
    @PostMapping("/getNetServiceRank")
    public ResultMap getNetServiceRank(@Valid @RequestBody NfcFlowMinerQueryForm nfcFlowMinerQueryForm) {
        return nfcFlowMinerService.getNetServiceRank(nfcFlowMinerQueryForm);
    }
    
    
    @ResponseBody
    @PostMapping("/getFlowReportList")
    public ResultMap getFlowReportList(@Valid @RequestBody NfcFlowMinerQueryForm nfcFlowMinerQueryForm) {
        return nfcFlowMinerService.getFlowReportPageList(nfcFlowMinerQueryForm);
    }
    
    @Value("${ful2gb:0.014}")
    private Float ful2gb;
    @ResponseBody
    @RequestMapping("/getFul2Gb")
    public ResultMap getFul2Gb() {    	
    	return ResultMap.getSuccessfulResult(ful2gb);        
    }
    
}