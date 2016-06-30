package com.mapbar.driver.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.mapbar.driver.bean.City;
import com.mapbar.driver.dao.CityDao;

@Repository
public class CityDaoImpl implements CityDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 获取省份拼音列表
	 */
	public List<String> getProPyList(String sql) {
		List<String> list = jdbcTemplate.queryForList(sql, String.class);
		return list;
	}

	/**
	 * 获取城市信息
	 */
	public List<City> getCityList(String sql) {
		SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
		List<City> carList = new ArrayList<City>();
		// 查询每个城市信息
		while (rs.next()) {
			City cityModel = new City();
			cityModel.setProvince_pinyin(rs.getString("province_pinyin"));// 省份拼音简称
			cityModel.setProvince(rs.getString("province"));// 省份名称
			cityModel.setPro(rs.getString("pro"));// 省份名称简写
			cityModel.setCity_code(rs.getString("city_code"));// 城市编码
			cityModel.setCity_name(rs.getString("city_name"));// 城市名称
			cityModel.setCity_status(rs.getString("city_status"));// 城市开关
			cityModel.setIsyzm(rs.getInt("is_yzm"));// 是否需要图片验证码
			cityModel.setUpdate_time(rs.getString("update_time"));// 城市列表更新时间
			cityModel.setIs_name(rs.getString("is_name"));// 是否需要驾驶人姓名
			cityModel.setIs_license(rs.getString("is_license"));// 是否需要驾驶证号
			cityModel.setIs_licensedate(rs.getString("is_licensedate"));// 是否需要初次领证日期
			cityModel.setIs_archive(rs.getString("is_archive"));// 是否需要驾驶证档案编号
			cityModel.setIs_effective(rs.getString("is_effective"));// 是否需要驾驶证是否长期有效
			cityModel.setEffective_date(rs.getString("effective_date"));// 是否需要驾驶证有效截至日期
			carList.add(cityModel);
		}
		return carList;
	}

	public String getMaxUpdateTime() {
		String sql = "SELECT MAX(update_time) FROM city_list";
		return jdbcTemplate.queryForObject(sql, String.class);
	}

}
