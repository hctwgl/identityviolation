package com.mapbar.traffic.score.task;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.mapbar.traffic.score.base.DriverProfile;

public class DriverTaskQueue {

	private DriverTaskQueue() {
	}

	private static DriverTaskQueue instance = new DriverTaskQueue();

	// 阻塞式队列 （线程安全的）
	private BlockingQueue<DriverProfile> queue = new LinkedBlockingQueue<DriverProfile>(90);

	public static DriverTaskQueue getInstance() {
		return instance;
	}

	public void clear() {
		queue.clear();
	}

	public DriverProfile ifcanget() {
		return queue.peek();
	}

	public boolean addTask(DriverProfile s) {
		try {
			return queue.offer(s);
		} catch (Exception e) {
			return false;
		}

	}

	public boolean has(DriverProfile s) {
		try {
			return queue.contains(s);
		} catch (Exception e) {
			return false;
		}

	}

	public DriverProfile getTask() {
		DriverProfile ret = null;
		try {
			ret = this.queue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return ret;
	}
}
