package com.mapbar.traffic.score.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mapbar.traffic.score.base.CityProfile;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class DbHelp {

	private static String url = PropertiesUtils.getProValue("jdbc.mysql.url");

	private static String user = PropertiesUtils.getProValue("jdbc.mysql.user");

	private static String pswd = PropertiesUtils.getProValue("jdbc.mysql.pswd");

	public static Connection getConnetion() {
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url, user, pswd);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}

	public static Map<String, CityProfile> getAllCity() {
		Map<String, CityProfile> result = new HashMap<String, CityProfile>();

		Connection con = getConnetion();
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			String sql = "select * from city_list where city_status = '1' order by province,city_code ";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String cityPy = rs.getString("city_code");
				String cityName = rs.getString("city_name");
				String proPy = rs.getString("province_pinyin") == null ? "" : rs.getString("province_pinyin");
				String province = rs.getString("province");
				String pro = rs.getString("pro");
				CityProfile city = new CityProfile();
				city.setCityName(cityName);
				city.setCityPy(cityPy.toLowerCase());
				city.setPro(pro);
				city.setProvince(province);
				city.setProPy(proPy);
				result.put(cityPy.toLowerCase(), city);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return result;
	}

	public static Map<String, String> getCitySource() {
		Map<String, String> result = new HashMap<String, String>();
		Connection con = getConnetion();
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			String sql = "select * from city_source_config where usable = 1 order by citypy,source_index desc ";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String py = rs.getString("citypy");
				String source = rs.getString("source_class");
				if (result.containsKey(py)) {
					if (!result.get(py).contains(source)) {
						String value = result.get(py) + "," + source;
						result.put(py, value);
					}

				} else {
					result.put(py, source);
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		return result;

	}

	public static int updateCitys(String cityPys, String usable) {
		int n = 0;
		Connection con = getConnetion();
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			String updateSql = "update city_list set city_status = '" + usable + "' where 1=1 ";

			if (StringUtil.isNotEmpty(cityPys)) {
				String param = "";
				if (cityPys.contains(",")) {
					param = "'" + cityPys.replace(",", "','") + "'";
				} else {
					param = "'" + cityPys + "'";
				}
				updateSql += " and city_code in (" + param + ") ";

				n = stmt.executeUpdate(updateSql);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return n;
	}

	public static int updateConfig(String cityPy, String className, String usable) {
		int n = 0;
		Connection con = getConnetion();
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			String updateSql = "update city_source_config set usable = '" + usable + "' where 1=1 ";
			if (StringUtil.isNotEmpty(className)) {
				updateSql += " and source_class ='" + className + "' ";
			}

			if (StringUtil.isNotEmpty(cityPy)) {
				updateSql += " and citypy ='" + cityPy + "' ";
			}

			n = stmt.executeUpdate(updateSql);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return n;
	}

	public static Map<String, String> getCitySourceRole() {
		Map<String, String> result = new HashMap<String, String>();
		Connection con = getConnetion();
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			String sql = "select * from city_source_config where usable = 1";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String py = rs.getString("citypy");
				String source = rs.getString("source_class");
				String rule = rs.getString("source_rule");
				result.put(py + "," + source, rule);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		return result;

	}

	public static void excuteInsertLog(List<String> sqlList) {
		Connection con = getConnetion();
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			for (String q : sqlList) {

				stmt.addBatch(q);
			}
			stmt.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

	}

}
