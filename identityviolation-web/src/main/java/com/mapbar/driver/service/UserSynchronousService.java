package com.mapbar.driver.service;

import com.alibaba.fastjson.JSONException;
import com.mapbar.driver.bean.Driver;

public interface UserSynchronousService {

	public String getSynchronous(Driver driver) throws Exception;

	/**
	 * 用户登录同步接口 功能流程1：删除设备绑定 2：同步账户绑定表
	 * 
	 * @param carObj
	 * @return
	 * @throws Exception 
	 * @throws JSONException
	 */
	public String getUserSynchronous(Driver driver) throws Exception;

}
