package com.imooc.controller.job;

import com.imooc.Utils.AgentSvcTask;
import com.imooc.Utils.SpringContextUtils;
import com.imooc.controller.config.ExploerCfg;
import com.imooc.mapper.AccountMapper;
import com.imooc.mapper.NfcFlowMinerMapper;
import com.imooc.mapper.SyncChildBlocknumberMapper;
import com.imooc.pojo.Addresses;
import com.imooc.pojo.SyncChildBlocknumber;
import com.imooc.pojo.vo.AddressVo;
import com.imooc.service.ChildFlowCltService;
import com.imooc.utils.Constants;
import com.imooc.utils.Web3jChildUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.AlienSnapshot;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@Slf4j
@Component
public class ChildFulModifyJob extends AgentSvcTask {
    private ExploerCfg cfg;
    private AccountMapper accountMapper;
    private NfcFlowMinerMapper nfcFlowMinerMapper;


    protected  void runTask() {
        accountMapper = SpringContextUtils.getBean(AccountMapper.class);
        nfcFlowMinerMapper = SpringContextUtils.getBean(NfcFlowMinerMapper.class);
        cfg = SpringContextUtils.getBean(ExploerCfg.class);
        while(!shutdown) {
            long stime=System.currentTimeMillis();
            Calendar ca = Calendar.getInstance();
            int minut = ca.get(Calendar.MINUTE);
            try {
                if(minut%cfg.getUpdatefulminute()==0) {
                    log.info("ChildFulModifyJob update ful start!");
                    Web3jChildUtils wb = new Web3jChildUtils();
                    Web3j web3j = Web3jChildUtils.getWeb3j(cfg.getChildipaddress());
//                    Map<String, BigDecimal> fulOweMap = new HashMap<>();
//                    AlienSnapshot.Snapshot snap = web3j.alienSnapshotFulArrears().send().getSnapshot();
//                    if(snap!=null&&snap.getFularrears()!=null){
//                        for (Map.Entry<String, Object> m:snap.getFularrears().entrySet()) {
//                            fulOweMap.put(m.getKey().toLowerCase(),new BigDecimal(m.getValue().toString()));
//                        }
//                    }
                    List<AddressVo> addressList =  accountMapper.getAllAddress();
                    List<Addresses> batchList = new ArrayList<>();
                    for (AddressVo address : addressList) {
                        try {
                            BigInteger balance = wb.getAddressBalance(web3j, address.getAddress());
                            if (null == balance)
                                continue;
                            BigInteger nonce = wb.getAddressNonce(web3j, address.getAddress());
                            if (null == nonce)
                                continue;
                            Addresses item = new Addresses();
                            item.setId(address.getId());
//                            String tolowerAddress = address.getAddress().toLowerCase();
//                            if(fulOweMap.containsKey(tolowerAddress)){
//                                item.setFul_owe(fulOweMap.get(tolowerAddress));
//                            }
                            if (balance.doubleValue() > 1000000000000000000000000000000d) {
                                continue;
                            } else {
                                item.setFul_balance(new BigDecimal(balance));
                            }
                            item.setFul_nonce(nonce.longValue());
                            batchList.add(item);
                            if (batchList.size() >= Constants.BATCHCOUNT) {
                                accountMapper.updateFulBatch(batchList);
                                batchList = new ArrayList<>();
                            }
                        }catch (Exception e){
                            log.error("ChildFlowCltJob sync ful error,address="+address.getAddress(),e);
                        }
                    }
                    if(batchList.size()>0){
                        accountMapper.updateFulBatch(batchList);
                    }
                    log.info(" ChildFulModifyJob update ful end!");
                }

            }catch (Exception e) {
                log.error("ChildFulModifyJob error",e);
            }finally {
                try {
                    long etime=System.currentTimeMillis();
                    if(60000-(etime-stime)>0) {
                        Thread.sleep(60000-(etime-stime));
                    }
                } catch (InterruptedException e) {
                }
            }
        }

    }


}
