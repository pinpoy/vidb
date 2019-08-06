/*
 * SpiderConsoleLogger.java
 * classes : com.spider.subscriber.util.SpiderConsoleLogger
 * @author liujun
 * V 1.0.0
 * Create at 2014-12-29 下午7:09:33
 * Copyright (c) 2014年  Spider. All Rights Reserved.
 */
package com.moogos.spacex.logger;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * @ClassName: SpiderConsoleLogger
 * @Description:控制台日志
 * @author LiuJun
 * @date 2014-12-29 下午7:09:33
 * 
 */
public class SpiderConsoleLogger implements ISpiderLogger {
	private static final String TAG = "SpiderConsoleLogger";

	private int level; //日志级别

	public SpiderConsoleLogger(int level) {
		this.level = level;
	}

	@Override
	public void d(String tag, String msg) {
		if (level <= LEVEL_DEBUG) {
			Log.d(tag, msg + "");
		}
	}

	@Override
	public void i(String tag, String msg) {
		if (level <= LEVEL_INFO) {
			Log.i(tag, msg + "");
		}
	}

	@Override
	public void w(String tag, String msg) {
		if (level <= LEVEL_WARN) {
			Log.w(tag, msg + "");
		}
	}

	@Override
	public void e(String tag, String msg) {
		if (level <= LEVEL_ERROR) {
			Log.e(tag, msg + "");
		}
	}

	@Override
	public void e(String tag, Exception ex) {

		if (level <= LEVEL_ERROR) {

			StringBuilder error = new StringBuilder();
			try {
				Writer writer = new StringWriter();
				PrintWriter printWriter = new PrintWriter(writer);
				ex.printStackTrace(printWriter);
				Throwable cause = ex.getCause();
				while (cause != null) {
					cause.printStackTrace(printWriter);
					cause = cause.getCause();
				}
				printWriter.close();
				String result = writer.toString();
				error.append(result);

			} catch (Exception e) {
				SpiderLogger.getLogger().e(TAG, e.getMessage());
			}

			Log.e(TAG, error.toString());
		}

	}
}
