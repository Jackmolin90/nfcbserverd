package com.imooc.pojo;

import lombok.Data;

import javax.persistence.Id;
import java.util.Date;

/**
 * @author zhc 2021-12-08 10:56
 */
@Data
public class SyncChildBlocknumber {
    @Id
    private String syncid ;
    private Long blocknumber ;
    private Date synctime ;

    public SyncChildBlocknumber(String syncid, Long blocknumber, Date synctime) {
        this.syncid = syncid;
        this.blocknumber = blocknumber;
        this.synctime = synctime;
    }
}
