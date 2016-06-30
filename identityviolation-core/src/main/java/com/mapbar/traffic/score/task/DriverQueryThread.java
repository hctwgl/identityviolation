package com.mapbar.traffic.score.task;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.relay.RelayProcess;

public class DriverQueryThread implements Runnable {

	private DriverProfile driverProfile = null;
	private boolean shutdown = true;

	public DriverQueryThread(boolean shutdown) {
		this.shutdown = shutdown;
	}

	public DriverQueryThread(DriverProfile driverProfile) {
		this.driverProfile = driverProfile;
	}

	@Override
	public void run() {
		while (shutdown) {
			try {
				driverProfile = DriverTaskQueue.getInstance().getTask();
				if (driverProfile != null) {
					RelayProcess.getInstance().processForLocal(driverProfile);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
