package com.mapbar.traffic.score.mail;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MailTaskQueue {

	private MailTaskQueue() {
	}

	private static MailTaskQueue instance = new MailTaskQueue();

	private BlockingQueue<String> queue = new LinkedBlockingQueue<String>();

	public static MailTaskQueue getInstance() {
		return instance;
	}

	public boolean addTask(String s) {
		try {
			return queue.offer(s);
		} catch (Exception e) {
			return false;
		}
	}

	public String getTask() {
		String ret = null;
		try {
			ret = this.queue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return ret;
	}
}
