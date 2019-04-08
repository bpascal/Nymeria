package com.codido.nymeria.util;

import android.content.Context;

import com.codido.nymeria.bean.req.BaseReq;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 接口处理管理器
 */
public class ProcessManager {
	private ProcessManager() {};

	private static ProcessManager instance;

	public static ProcessManager getInstance() {
		if (instance == null) {
			instance = new ProcessManager();
		}
		return instance;
	}

	ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 20, 1, TimeUnit.SECONDS,
			new ArrayBlockingQueue<Runnable>(3), new ThreadPoolExecutor.CallerRunsPolicy());

	public void addTask(Runnable runnable) {
		if (runnable != null)
			threadPoolExecutor.execute(runnable);
	}

	public TextMessageProcess addProcess(Context context, BaseReq request,
			ProcessListener processListener) {
		TextMessageProcess run = null;
		try {
			run = new TextMessageProcess(context, request, processListener);
		} catch (Exception e) {
			e.printStackTrace();
		}
		addTask(run);
		return run;
	}

}
