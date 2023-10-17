package com.imooc.utils;

public class TimeSpend {

	private long start;
	
	public TimeSpend(){
		start = System.currentTimeMillis();
	}
	
	public long getSpendMilliSecond() {
		long now = System.currentTimeMillis();
		return now - start;
	}
	
	public String getSpendTime(){
		long now = System.currentTimeMillis();
		long spend = now - start;
		if (spend < 1000)
			return spend + " ms ";
		else if (spend < 3000000)	//小于5分钟,还是显示秒
			return ((double) spend / 1000) + " seconds ";
		else
			return spend / 60000 + " minutes ";
	}
}
