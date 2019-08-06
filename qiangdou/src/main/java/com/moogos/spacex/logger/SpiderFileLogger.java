/*
 * FileLogger.java
 * classes : com.spider.subscriber.util.FileLogger
 * @author liujun
 * V 1.0.0
 * Create at 2014-12-29 下午7:08:34
 * Copyright (c) 2014年  Spider. All Rights Reserved.
 */
package com.moogos.spacex.logger;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: FileLogger
 * @Description: 文件日志
 * @author LiuJun
 * @date 2014-12-29 下午7:08:34
 * 
 */
public class SpiderFileLogger implements ISpiderLogger {
	private static final String TAG = "FileLoger";

	public static final String[] LEVELS = { "Debug", "Info", "Warn", "Error" };

	public static final String LOG_FILE_FORMAT = "yyyy-MM-dd";
	public static final String CRASH_FILE_FORMAT = "yyyy-MM-dd HH:mm:ss E";
	public static final String LOG_CONTENT_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static final long LOG_MAXSIZE = 1024 * 1024 * 10; // 日志容量

	public static final String LOG_PATH = "/Log";
	public static final String CRASH_PATH = "/crash";
	public static final String LOG_SUFFIX = ".log";

	private Context context;
	private int level;
	private String logDir;

	public SpiderFileLogger(Context context, int level) {
		this.context = context;
		this.level = level;
		logDir = getLogPath();
		initLogDir();
	}

	/**
	 * 初始化日志目录
	 */
	private void initLogDir() {
		File file = new File(logDir);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	/**
	 * 获取日志路径
	 * 
	 * @return
	 */
	private String getLogPath() {
		String logPath = null;
		try {
			String cachePath = null;
			if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
					&& !Environment.isExternalStorageRemovable()) {

				cachePath = context.getExternalCacheDir().getPath();
			} else {
				cachePath = context.getCacheDir().getPath();
			}
			logPath = cachePath + LOG_PATH;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return logPath;
	}

	@Override
	public void d(String tag, String msg) {
		if (level <= LEVEL_DEBUG) {
			writeLog(tag, msg, level);
		}
	}

	@Override
	public void i(String tag, String msg) {
		if (level <= LEVEL_DEBUG) {
			writeLog(tag, msg, level);
		}
	}

	@Override
	public void w(String tag, String msg) {
		if (level <= LEVEL_DEBUG) {
			writeLog(tag, msg, level);
		}
	}

	@Override
	public void e(String tag, String msg) {
		if (level <= LEVEL_DEBUG) {
			writeLog(tag, msg, level);
		}
	}


	/**
	 * 写日志
	 * 
	 * @param msg
	 * @param level
	 */
	private void writeLog(String tag, String msg, int level) {
		if (logDir == null || msg == null)
			return;
		initLogDir();
		deleteOldLog();
		FileOutputStream fos = null;
		try {

			SimpleDateFormat sdf = new SimpleDateFormat(LOG_FILE_FORMAT);
			String dateStr = sdf.format(new Date());
			File file = new File(logDir, dateStr + LOG_SUFFIX);
			fos = new FileOutputStream(file, true);

			StackTraceElement[] stacks = new Throwable().getStackTrace();
			String className = stacks[3].getClassName();
			String methodName = stacks[3].getMethodName();
			int lineNumber = stacks[3].getLineNumber();
			sdf = new SimpleDateFormat(LOG_CONTENT_FORMAT);
			dateStr = sdf.format(new Date());
			String content = dateStr + "[" + LEVELS[level] + "]" + " "
					+ className + ":" + methodName + "(" + lineNumber
					+ "行) --- " + "[" + tag + "]" + " " + msg;
			fos.write(content.getBytes());
			fos.write("\r\n".getBytes());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
					fos = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 日志容量超出最大值，删除最旧的日志
	 */
	private void deleteOldLog() {
		File dir = new File(logDir);
		if (!dir.exists() || !dir.isDirectory())
			return;
		File[] files = dir.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String filename) {
				return filename.endsWith(LOG_SUFFIX);
			}
		});
		if (files == null || files.length == 0)
			return;

		List<File> fileList = Arrays.asList(files);
		Collections.sort(fileList, new Comparator<File>() {

			@Override
			public int compare(File lhs, File rhs) {
				return lhs.getName().compareTo(rhs.getName());
			}

		});
		long totalSize = 0;
		for (File file : fileList) {
			totalSize += file.length();
		}
		long overSize = totalSize - LOG_MAXSIZE;
		for (int i = 0; i < fileList.size(); i++) {
			File file = fileList.get(i);
			if (overSize > 0) {
				overSize -= file.length();
				file.delete();
			} else {
				return;
			}
		}

	}


	@Override
	public void e(String tag, Exception ex) {

	}
}
