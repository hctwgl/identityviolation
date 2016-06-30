package com.mapbar.traffic.score.transfer.factory;

import com.mapbar.traffic.score.base.DriverProfile;

public interface Transfer {

	/**
	 * 检查验证证积分信息
	 * 
	 * @param 
	 * @return
	 */
	String checkDriverScore(DriverProfile driverProfile);

}
