package com.mapbar.driver.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mapbar.driver.bean.Driver;
import com.mapbar.driver.dao.DriverDao;
import com.mapbar.driver.utils.DateUtil;

@Repository
public class DriverDaoImpl implements DriverDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 查询数据库是否有该驾驶人
	 * 
	 * @param driver_license
	 *            驾驶证号
	 * @return
	 * @throws Exception
	 */
	public int getUserByLicense(String driver_license) throws Exception {
		int count = 0;
		String sql = "SELECT COUNT(*) FROM drivers_user WHERE driver_license=?";
		count = jdbcTemplate.queryForObject(sql, new Object[] { driver_license }, Integer.class);
		return count;
	}

	/**
	 * 新增加驾驶人信息
	 */
	@Override
	public synchronized int instertUser(Driver driver) throws Exception {
		String sql = "INSERT INTO `drivers_user` (`province`,`city`,`citypinyin`,`driver_name`,`driver_license`,`lissue_date`,`lissue_archive`,`is_effective`,`effective_date`,`create_time`,`modf_time`,`product_iden`,`phone_imei`,`phone_model`,`phone_system`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		return jdbcTemplate.update(sql, new Object[] { driver.getProvince(), driver.getCity(), driver.getCity_pinyin(), driver.getDriver_name(), driver.getDriver_license(), driver.getLissue_date(), driver.getIs_effective(), DateUtil.currentTime(), DateUtil.currentTime(), DateUtil.currentTime(), driver.getProduct(), driver.getPhone_imei(), driver.getPhone_model(), driver.getPhone_system() });
	}

	/**
	 * 查询用户是否绑定
	 * 
	 * @param 用户唯一标识
	 *            （待定）
	 * @return
	 * @throws Exception
	 */
	public int getUserById(String id) throws Exception {
		int count = 0;
		String sql = "select count(1) from driver_user where driver_license=?";
		count = jdbcTemplate.queryForObject(sql, new Object[] { id }, Integer.class);
		return count;
	}

	/**
	 * 更新推送开关
	 * 
	 * 用户唯一标识
	 * 
	 * @param push_tf
	 *            推送开关
	 * @throws Exception
	 */
	public synchronized void updatePush(String driver_license, String push_tf) throws Exception {
		String sql = "update driver_user set push_tf=? where driver_license=?";
		jdbcTemplate.update(sql, new Object[] { push_tf, driver_license });
	}

	public synchronized void updatePushId(String driver_license, String pushId, String product) throws Exception {
		String sql = "update driver_user set product_iden=?,baidu_channel_id=?  where  driver_license=?";
		jdbcTemplate.update(sql, new Object[] { product, pushId, driver_license });
	}

	/**
	 * 更新用户（根据用户唯一标识）
	 */
	public synchronized void updateUser(Driver driver) throws Exception {
		String sql = "UPDATE `drivers_user` SET `userid`=?, `province`=?, `city`=?, `citypinyin`=?, `driver_name`=?, `driver_license`=?, `lissue_date`=?, `lissue_archive`=?, `is_effective`=?, `effective_date`=?, `modf_time`=?, `query_time`=?, `product_iden`=?, `phone_imei`=?, `phone_model`=?, `phone_system`=? WHERE (`userid`=0)";
		jdbcTemplate.update(sql, new Object[] { driver.getUserId(), driver.getProvince(), driver.getCity(), driver.getCity_pinyin(), driver.getDriver_name(), driver.getDriver_license(), driver.getLissue_date(), driver.getLissue_archive(), driver.getIs_effective(), DateUtil.currentTime(), DateUtil.currentTime(), driver.getProduct(), driver.getPhone_imei(), driver.getPhone_model(), driver.getPhone_system() });
	}

}
