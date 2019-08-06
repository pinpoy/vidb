/*
 * ISpiderLoger.java
 * classes : com.spider.subscriber.util.ISpiderLoger
 * @author liujun
 * V 1.0.0
 * Create at 2014-12-29 下午6:17:50
 * Copyright (c) 2014年  Spider. All Rights Reserved.
 */
package com.moogos.spacex.logger;

/**
 * @ClassName: ISpiderLogger
 * @Description: 日志接口
 * @author LiuJun
 * @date 2014-12-29 下午6:17:50
 * 
 */
public interface ISpiderLogger {
	
	// 日志级别
	int LEVEL_DEBUG = 0;
	int LEVEL_INFO = 1;
	int LEVEL_WARN = 2;
	int LEVEL_ERROR = 3;
	// 不打印日志
	int LEVEL_NOLOG = 4;
	
	/**
	 * debug日志
	 * @param tag
	 * @param msg
	 */
	void d(String tag, String msg);

	/**
	 * info日志
	 * @param tag
	 * @param msg
	 */
	void i(String tag, String msg);

	/**
	 * warning日志
	 * @param tag
	 * @param msg
	 */
	void w(String tag, String msg);

	/**
	 * error日志
	 * @param tag
	 * @param msg
	 */
	void e(String tag, String msg);

	/**
	 * error日志
	 * @param tag
	 * @param e
	 */
	void e(String tag, Exception e);
}
