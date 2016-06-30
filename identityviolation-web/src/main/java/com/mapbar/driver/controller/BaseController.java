package com.mapbar.driver.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.restlet.Response;
import org.restlet.data.CacheDirective;
import org.restlet.data.CharacterSet;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public abstract class BaseController extends ServerResource {

	private String xmlORjson = "JSON";

	/**
	 * @说明:获得客户端的参数
	 */
	@Override
	protected void doInit() {
		try {
			decodeCharacterSet();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/***
	 * 处理字符集，判断返回格式
	 * 
	 * @throws UnsupportedEncodingException
	 * 
	 */
	private void decodeCharacterSet() throws UnsupportedEncodingException {
		setXmlORjson(getXmlORjson());
		// String encode = getQueryValue("encode");
		// Form form = new Form(req.getEntity());//post请求发过来的
		// Form form = req.getResourceRef().getQueryAsForm();
		Form form = getQuery();
		// Map<String, Object> attributes = getRequestAttributes();
		String user_Ip = getServerInfo().getAddress();
		if (StringUtils.isNotEmpty(user_Ip) && user_Ip.indexOf(",") >= 0) {
			String[] forwarded_ip = user_Ip.split(",");
			user_Ip = forwarded_ip[0];
		}
		form.set("ip", user_Ip);
		getParameter(form);
	}

	/**
	 * 通过get请求来得到服务的返回结果
	 */
	@Override
	@Get("xml|json")
	public Representation get() {
		Representation representation = getRepresentation();
		Response restletResponse = getResponse();
		List<CacheDirective> cacheDirectives = new ArrayList<CacheDirective>();
		cacheDirectives.add(CacheDirective.noCache());
		restletResponse.setCacheDirectives(cacheDirectives);
		// HttpServletResponse servletResponse = ServletUtils.getResponse(restletResponse);
		// servletResponse.setHeader(HeaderConstants.HEADER_CACHE_CONTROL, HeaderConstants.CACHE_NO_CACHE);
		// restletResponse.setEntity(representation);
		return representation;
	}

	/**
	 * post请求来得到服务的返回结果
	 */
	@Override
	@Post("xml|json")
	public Representation post(Representation entity) {
		Form form = new Form(entity);
		getParameter(form);
		Representation representation = getRepresentation();
		return representation;
	}

	/**
	 * 入口方法，获得返回结果
	 * 
	 * @return
	 * 
	 */
	private Representation getRepresentation() {
		Representation representation = null;
		if ("xml".equalsIgnoreCase(getXmlORjson())) {
			representation = getXMLRepresentation();
		} else {
			representation = getJSONRepresentation();
			representation.setMediaType(MediaType.TEXT_HTML);
		}
		representation.setCharacterSet(CharacterSet.UTF_8);
		return representation;
	};

	/***
	 * 配置接收参数与实体对应
	 * 
	 * @param form
	 * 
	 */
	protected abstract void getParameter(Form form);

	/***
	 * 返回xml格式的数据
	 * 
	 * @return
	 */
	protected abstract Representation getXMLRepresentation();

	/**
	 * 返回json格式的数据
	 * 
	 * @return
	 */
	protected abstract Representation getJSONRepresentation();

	public String getXmlORjson() {
		return xmlORjson;
	}

	public void setXmlORjson(String xmlORjson) {
		this.xmlORjson = xmlORjson;
	}
}
