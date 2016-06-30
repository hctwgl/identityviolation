package com.mapbar.driver.dao;

import com.mapbar.driver.bean.Driver;

public interface DriverDao {
	public int getUserByLicense(String driver_license) throws Exception;

	public int getUserById(String id) throws Exception;

	public void updatePush(String plate_number, String push_tf) throws Exception;

	public void updatePushId(String driver_license, String pushId, String product) throws Exception;

	public void updateUser(Driver driver) throws Exception;

	public int instertUser(Driver driver) throws Exception;

}
