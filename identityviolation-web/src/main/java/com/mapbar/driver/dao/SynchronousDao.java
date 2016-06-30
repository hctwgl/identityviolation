package com.mapbar.driver.dao;

import java.util.List;

import com.mapbar.driver.bean.Driver;

public interface SynchronousDao {

	public void updateSynchronous(Driver driver, List<String> licenses) throws Exception;

	public int getUuid(String uuid) throws Exception;

	public List<Driver> getUserLogin(String key, String UserID) throws Exception;

	public void updateUserSynchronous(Driver driver, List<String> licenses) throws Exception;
}
