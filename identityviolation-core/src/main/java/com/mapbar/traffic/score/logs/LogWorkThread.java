package com.mapbar.traffic.score.logs;

import java.util.ArrayList;
import java.util.List;

import com.mapbar.traffic.score.db.DbHelp;

public class LogWorkThread extends Thread {

	private boolean shutdown = true;

	private List<String> sqllist = new ArrayList<String>();

	@Override
	public void run() {
		while (shutdown) {
			try {
				SourceLogBean log = LogTaskQueue.getInstance().getTask();
				if ("ok".equals(log.getRequstStatus())) {
					SourceRepCounter.addSuccessCount(log);
				} else {
					SourceRepCounter.addErrCount(log);
				}
				String insertsql = log.toInsertSql();
				sqllist.add(insertsql);
				if (sqllist.size() >= 20) {
					DbHelp.excuteInsertLog(sqllist);
					sqllist.clear();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setShutdown(boolean shutdown) {
		this.shutdown = shutdown;
	}

	public void commitSql() {
		if (sqllist.size() > 0) {
			DbHelp.excuteInsertLog(sqllist);
			sqllist.clear();
		}
	}

}
