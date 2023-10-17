package com.imooc.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author zhc 2021-09-24 16:25
 */
public class OrderUtils {
    public static Set<String> nfcFlowMinerList = new HashSet<String>(0);
    public static Set<String> nfcFlowMinerRankList = new HashSet<String>(0);
    static {
        String nfcFlowMinerStr = "join_time|sync_time|blocknumber|draw_amount|release_amount|lock_amount|revenue_amount|payful|miner_flow|pledge_amount|bandwidth";
        String nfcFlowMiner[] = nfcFlowMinerStr.split("\\|");
        for (String str : nfcFlowMiner) {
            nfcFlowMinerList.add(str);
        }

        String nfcFlowMinerRankStr = "bandwidth|revenue_amount|miner_flow";
        String nfcFlowMinerRank[] = nfcFlowMinerRankStr.split("\\|");
        for (String str : nfcFlowMinerRank) {
            nfcFlowMinerRankList.add(str);
        }
    }

    /**
     * @param sortBy
     * @param descending
     * @return
     */
    public static String sort(String sortBy,boolean descending) {
           return " order by "+sortBy+(descending?" desc":"");
    }
}
