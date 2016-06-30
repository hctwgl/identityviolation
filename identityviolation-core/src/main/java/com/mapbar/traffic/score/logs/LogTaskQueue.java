package com.mapbar.traffic.score.logs;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class LogTaskQueue {

	private LogTaskQueue() {
	}

	private static LogTaskQueue instance = new LogTaskQueue();
	// 阻塞式队列 （线程安全的）
	private BlockingQueue<SourceLogBean> queue = new LinkedBlockingQueue<SourceLogBean>();

	public static LogTaskQueue getInstance() {
		return instance;
	}

	public void clear() {
		queue.clear();
	}

	public void addTask(SourceLogBean s) {
		try {
			this.queue.put(s);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public SourceLogBean getTask() {
		SourceLogBean ret = null;
		try {
			ret = this.queue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return ret;
	}
}
