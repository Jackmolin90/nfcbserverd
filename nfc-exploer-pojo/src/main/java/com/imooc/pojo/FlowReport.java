package com.imooc.pojo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class FlowReport {

	private Long id;
	
	private String miner_addr;
	
	private Long enid;
	
	private Long deviceid;
	
	private String client_addr;
	
	private Long report_number;
	
	private BigDecimal flow_value;
	
	private BigDecimal ful;
	
	private Integer status;
	
	private Long blocknumber;
	
	private String blockhash;
	
	private Date timestamp;
}
