package com.mapbar.driver.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.mapbar.driver.bean.Driver;
import com.mapbar.driver.dao.DriverDao;
import com.mapbar.driver.dao.SynchronousDao;

@Repository
public class SynchronousDaoImpl implements SynchronousDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private DriverDao driverDao;

	public void updateSynchronous(Driver driver, List<String> licenses) throws Exception {
		if (!StringUtils.isEmpty("key")) {
			String sql = "DELETE FROM drivers_user WHERE key_uuid=?";
			jdbcTemplate.update(sql);
		}
		if (licenses.size() > 0) {
			int size = licenses.size();
			for (int i = 0; i < size; i++) {
				driver.setDriver_license(licenses.get(i));
				driverDao.instertUser(driver);
			}
		}
	}

	@Override
	public int getUuid(String uuid) throws Exception {
		String sql = "select count(1) FROM drivers_user WHERE key_uuid=?";
		int count = jdbcTemplate.queryForObject(sql, Integer.class);
		return count;
	}

	public List<Driver> getUserLogin(String key, String UserID) throws Exception {

		String sql = "SELECT * FROM driver_user a.userid=? ";
		SqlRowSet ss = jdbcTemplate.queryForRowSet(sql);
		List<Driver> list = new ArrayList<Driver>();
		// 查询所有信息
		while (ss.next()) {
			Driver car = new Driver();
			car.setCity(ss.getString("city"));
			car.setCity_pinyin(ss.getString("citypinyin"));
			car.setDriver_name(ss.getString("driver_name"));
			car.setDriver_license(ss.getString("driver_license"));
			car.setLissue_date(ss.getString("lissue_date"));
			list.add(car);
		}
		return list;
	}

	public void updateUserSynchronous(Driver driver, List<String> licenses) throws Exception {
		String sql = "Select count(*) from driver_user WHERE userid=?";
		int count = jdbcTemplate.queryForObject(sql, Integer.class);
		if (count > 0) {
			sql = "DELETE FROM driver_user WHERE userid=?";
			jdbcTemplate.execute(sql);
		}
		int listSize = licenses.size();
		for (int i = 0; i < listSize; i++) {
			driver.setDriver_license(licenses.get(i));
			driverDao.instertUser(driver);
		}
	}

}
