package com.imooc.controller.listenser;

import java.util.ArrayList;
import java.util.List;

import com.imooc.Utils.AgentSvcTask;
import com.imooc.controller.job.AddressBalanceModifyJob;
import com.imooc.controller.job.ChildFlowCltJob;
import com.imooc.controller.job.ChildFulModifyJob;
import com.imooc.controller.job.DayAddressBalanceModifyJob;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CollectdStarterApplicationListener implements ApplicationListener<ContextRefreshedEvent> {
	private List<AgentSvcTask> svcs = new ArrayList<>();
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            initServices();
            startServices();
        } catch (Exception e) {
            log.warn("onapplication exception. " + e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void initServices() throws Exception {
        log.info("-------CollectdStarterApplicationListener-----");
     //   svcs.add(new ChildFlowCltJob());
     //   svcs.add(new ChildFulModifyJob());
        svcs.add(new DayAddressBalanceModifyJob());
        svcs.add(AddressBalanceModifyJob.getInstance());
    }

    private void startServices() {
        for (AgentSvcTask svcTask : svcs) {
            svcTask.init_svc();
        }
        for (AgentSvcTask svcTask : svcs) {
            svcTask.start();
        }
    }

}
