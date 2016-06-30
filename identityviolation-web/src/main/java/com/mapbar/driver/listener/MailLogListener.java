package com.mapbar.driver.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.mapbar.traffic.score.logs.LogWorkThread;
import com.mapbar.traffic.score.mail.Mail;
import com.mapbar.traffic.score.mail.MailWorkThread;

public class MailLogListener implements ServletContextListener {
	private LogWorkThread logThread = null;
	private MailWorkThread mailThread = null;

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		if (logThread != null) {
			logThread.commitSql();
			// 停止这个线程 thread.stop();
			logThread.setShutdown(false);
		}
		if (mailThread != null) {
			mailThread.setShutdown(false);
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		Mail mail = new Mail();
		mailThread = new MailWorkThread(mail);
		logThread = new LogWorkThread();
		logThread.start();
		mailThread.start();
	}

}
