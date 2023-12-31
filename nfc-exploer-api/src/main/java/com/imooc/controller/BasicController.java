package com.imooc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.imooc.utils.RedisOperator;

@RestController
public class BasicController {
   
	@Autowired
	public RedisOperator redis;
	
	public static final String USER_REDIS_SESSION="user_redis_session";
	
	public static final String FILE_SPACE_PARENT = "C:";
	public static final String FILE_SPACE_CHILD="/imooc_videos_dev";
	public static final String FILE_SPACE = FILE_SPACE_PARENT+FILE_SPACE_CHILD;
	
	
	public static final String FFMPEG_EXE = "C:\\ffmpeg\\bin\\ffmpeg.exe";
	
	public static final Integer PAGE_SIZE = 5;
}
