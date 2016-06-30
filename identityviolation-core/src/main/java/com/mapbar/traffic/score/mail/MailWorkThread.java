package com.mapbar.traffic.score.mail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class MailWorkThread extends Thread {

	private static final String SYSTEM_NAME = "驾驶证积分查询-服务告警";
	private boolean shutdown = true;//控制线程的停止
	private Mail mail;

	public MailWorkThread(Mail mail) {
		this.mail = mail;
	}

	public void run() {
		try {
			while (shutdown) {
				String content = MailTaskQueue.getInstance().getTask();
				mail.sendMail(SYSTEM_NAME, content, null);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setShutdown(boolean shutdown) {
		this.shutdown = shutdown;
	}
	
}
