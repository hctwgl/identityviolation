package com.mapbar.traffic.score.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Consts;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLInitializationException;
import org.apache.http.cookie.SM;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.gif.GifDecoder;

public class HttpClientUtil {

	private static final Logger logger = Logger.getLogger(HttpClientUtil.class);

	private static final int KEEP_ALIVE = Integer.parseInt(PropertiesUtils.getProValue("KEEP_ALIVE"));
	private static final int MAX_CONN = Integer.parseInt(PropertiesUtils.getProValue("MAX_CONN"));
	private static final int MAX_PERROUTE = Integer.parseInt(PropertiesUtils.getProValue("MAX_PERROUTE"));
	public static final String DEFAULT_USER_AGENT = "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; chromeframe/31.0.1650.57; .NET CLR 2.0.50727; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)";
	public static final String DEFAULT_ENCODE = System.getProperty("DEFAULT_ENCODE", "UTF-8");
	public static final int CONNECTION_TIMEOUT = Integer.parseInt(System.getProperty("CONNECTION_TIMEOUT", "60000"));
	public static final int READ_TIME_OUT = Integer.parseInt(PropertiesUtils.getProValue("READ_TIME_OUT"));;
	public static final int SOCKETTIMEOUT = Integer.parseInt(PropertiesUtils.getProValue("SOCKETTIMEOUT"));;
	private static PoolingHttpClientConnectionManager connManager = null;
	private static CloseableHttpClient httpClient = null;

	protected HttpClientUtil() {
	}

	public static void destoryHttpClient() {
		HttpClientUtils.closeQuietly(httpClient);
	}

	/**
	 * 初始化http连接池，设置参数、http头等等信息
	 */
	static {
		try {
			// SSLContext sslContext = SSLContext.getInstance("Default");
			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, new TrustManager[] { new X509TrustManager() {

				public X509Certificate[] getAcceptedIssuers() {
					if (logger.isDebugEnabled()) {
						logger.info("getAcceptedIssuers =============");
					}
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
					if (!logger.isDebugEnabled()) {
						return;
					}
					logger.info("checkClientTrusted =============" + authType);
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
					if (!logger.isDebugEnabled()) {
						return;
					}
					logger.info("checkServerTrusted =============" + authType);
				}
			} }, new SecureRandom());
			// SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.getDefaultHostnameVerifier());
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create().register("http", PlainConnectionSocketFactory.INSTANCE).register("https", sslsf).build();

			connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
			ConnectionKeepAliveStrategy keepAliveStrategy = new DefaultConnectionKeepAliveStrategy() {
				@Override
				public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
					long keepAlive = super.getKeepAliveDuration(response, context);
					if (keepAlive == -1) {
						// Keep connections alive 5000 milliseconds if a 5000 value has not be explicitly set by the server
						keepAlive = KEEP_ALIVE;
					}
					return keepAlive;
				}
			};

			// Create socket configuration
			SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
			RedirectStrategy redirectStrategy = new LaxRedirectStrategy();
			connManager.setDefaultSocketConfig(socketConfig);
			// Create message constraints
			// MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount(200).setMaxLineLength(2000).build();
			// Create connection configuration
			// ConnectionConfig connectionConfig = ConnectionConfig.custom().setMalformedInputAction(CodingErrorAction.IGNORE).setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8).setMessageConstraints(messageConstraints).build();
			connManager.setMaxTotal(MAX_CONN);
			connManager.setDefaultMaxPerRoute(MAX_PERROUTE);
			// connManager.setMaxPerRoute(new HttpRoute(new HttpHost("localhost", 80)), 20);
			httpClient = HttpClients.custom().setConnectionManager(connManager).setKeepAliveStrategy(keepAliveStrategy).setRedirectStrategy(redirectStrategy).build();
			// 连接回收策略
			HttpClientIdelThread.newInstance(connManager).start();
		} catch (SSLInitializationException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String postJsonBody(String url, JSONObject jobj, String encode) {

		String ret = null;
		HttpPost post = new HttpPost(url);
		try {
			post.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SOCKETTIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).setConnectionRequestTimeout(READ_TIME_OUT).setExpectContinueEnabled(false).build();
			post.setConfig(requestConfig);

			String postData = jobj.toJSONString();
			post.setEntity(new StringEntity(postData, encode));
			logger.info("[HttpUtils Post] begin invoke url:" + url + " , params:" + postData);
			CloseableHttpResponse response = httpClient.execute(post);
			try {
				HttpEntity entity = response.getEntity();
				try {
					if (entity != null) {
						ret = EntityUtils.toString(entity, encode);
					}
				} finally {
					EntityUtils.consume(entity);
				}
			} finally {
				HttpClientUtils.closeQuietly(response);
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("UnsupportedEncodingException", e);
		} catch (Exception e) {
			logger.error("Exception", e);
		}
		return ret;
	}

	/**
	 * get请求
	 * 
	 * @param url
	 * @param params
	 * @param encode
	 * @param connectTimeout
	 * @param soTimeout
	 * @return
	 */
	public static String getUrlWithParams(String url, Map<String, String> params, String encode) {
		String ret = null;
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SOCKETTIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).setConnectionRequestTimeout(READ_TIME_OUT).build();

		StringBuilder sb = new StringBuilder();
		sb.append(url);
		int i = 0;
		for (Entry<String, String> entry : params.entrySet()) {
			if (i == 0 && !url.contains("?")) {
				sb.append("?");
			} else {
				sb.append("&");
			}
			sb.append(entry.getKey());
			sb.append("=");
			String value = entry.getValue();
			try {
				sb.append(URLEncoder.encode(value, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				logger.warn("encode http get params error, value is " + value, e);
			}
			i++;
		}
		logger.info("[HttpUtils Get] begin invoke:" + sb.toString());
		HttpGet get = new HttpGet(sb.toString());
		get.setConfig(requestConfig);

		try {
			CloseableHttpResponse response = httpClient.execute(get);
			try {
				HttpEntity entity = response.getEntity();
				try {
					if (entity != null) {
						ret = EntityUtils.toString(entity, encode);
					}
				} finally {
					EntityUtils.consume(entity);
				}
			} catch (Exception e) {
				logger.error(String.format("[HttpUtils Get]get response error, url:%s", sb.toString()), e);
			} finally {
				HttpClientUtils.closeQuietly(response);
			}
		} catch (SocketTimeoutException e) {
			logger.error(String.format("[HttpUtils Get]invoke get timout error, url:%s", sb.toString()), e);
		} catch (Exception e) {
			logger.error(String.format("[HttpUtils Get]invoke get error, url:%s", sb.toString()), e);
		}
		return ret;
	}

	public static String postURLContents(String url, String referer, Map<String, String> params, String encode) throws Exception {
		String ret = "";
		HttpPost post = new HttpPost(url);
		try {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(CONNECTION_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).setConnectionRequestTimeout(READ_TIME_OUT).build();
			post.setConfig(requestConfig);
			if (StringUtil.isNotEmpty(referer)) {
				post.setHeader(HttpHeaders.REFERER, referer);
			}
			post.setHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Linux; U; Android 2.1-update1; en-us; ADR6300 Build/ERE27) AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0 Mobile Safari/530.17");
			List<NameValuePair> formParams = new ArrayList<NameValuePair>();
			// 绑定到请求 Entry
			for (Entry<String, String> entry : params.entrySet()) {
				formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			post.setEntity(new UrlEncodedFormEntity(formParams, encode));
			CloseableHttpResponse response = httpClient.execute(post);
			try {
				// 执行POST请求
				HttpEntity entity = response.getEntity(); // 获取响应实体
				try {
					if (null != entity) {
						ret = EntityUtils.toString(entity, encode);
					}
				} finally {
					EntityUtils.consume(entity);
				}
			} finally {
				HttpClientUtils.closeQuietly(response);
			}
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException", e);
		} catch (IOException e) {
			logger.error("IOException", e);
		}
		return ret;
	}

	public static String postURLContents(String url, Map<String, String> params) throws Exception {
		// Header[] headers = { new BasicHeader(HttpHeaders.USER_AGENT, "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1) )") };
		// post.setHeaders(headers);
		return postURLContents(url, null, params, "UTF-8");
	}

	public static String getURLContentsWithCookies(String url, List<String> cookies, String referer, HttpHost proxy) throws Exception {
		return getURLContentsWithCookies(url, cookies, referer, proxy, "UTF-8");
	}

	public static String getURLContentsWithCookies(String url, List<String> cookies, String referer, HttpHost proxy, String encode) throws Exception {
		String ret = "";
		HttpClientContext context = HttpClientContext.create();
		HttpGet get = new HttpGet(url);
		try {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(CONNECTION_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).setConnectionRequestTimeout(READ_TIME_OUT).build();
			get.setConfig(requestConfig);
			HttpClientProxyUtils.setProxy(get, proxy);

			if (!cookies.isEmpty()) {
				StringBuffer sbCookies = new StringBuffer();
				for (int i = 0; i < cookies.size(); i++) {
					if (i > 0) {
						sbCookies.append("; ");
					}
					sbCookies.append((String) cookies.get(i));
				}
				get.setHeader(SM.COOKIE, sbCookies.toString());
			}
			if (StringUtil.isNotEmpty(referer)) {
				get.setHeader(HttpHeaders.REFERER, referer);
			}
			get.setHeader(HttpHeaders.USER_AGENT, DEFAULT_USER_AGENT);
			CloseableHttpResponse response = httpClient.execute(get);

			int code = response.getStatusLine().getStatusCode();

			if (code == HttpStatus.SC_MOVED_PERMANENTLY || code == HttpStatus.SC_MOVED_TEMPORARILY || code == HttpStatus.SC_TEMPORARY_REDIRECT) {
				HeaderElementIterator headers = new BasicHeaderElementIterator(response.headerIterator(SM.SET_COOKIE));
				if (headers != null) {
					parseCookies(cookies, headers);
				}

				headers = new BasicHeaderElementIterator(response.headerIterator(SM.COOKIE));

				if (headers != null) {
					parseCookies(cookies, headers);
				}
				HttpHost target = context.getTargetHost();
				List<URI> redirectLocations = context.getRedirectLocations();
				URI location = URIUtils.resolve(get.getURI(), target, redirectLocations);
				logger.info("Final HTTP location: " + location.toASCIIString());
				// Header locations = response.getFirstHeader(HttpHeaders.LOCATION);
				if (location != null) {
					return getURLContentsWithCookies(location.toString(), cookies, referer, proxy, encode);
				}
			}
			try {
				// 执行get请求
				HttpEntity entity = response.getEntity(); // 获取响应实体
				try {
					if (null != entity) {
						ret = EntityUtils.toString(entity, encode);
					}
				} finally {
					EntityUtils.consume(entity);
				}
			} finally {
				HttpClientUtils.closeQuietly(response);
			}
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException", e);
		} catch (IOException e) {
			logger.error("IOException", e);
		}
		return ret;
	}

	public static byte[] getUrlImageByteWithCookies(String url, List<String> cookies, String referer, HttpHost proxy) throws Exception {
		byte[] bt = null;
		HttpGet get = new HttpGet(url);
		try {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(CONNECTION_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).setConnectionRequestTimeout(READ_TIME_OUT).build();
			get.setConfig(requestConfig);
			HttpClientProxyUtils.setProxy(get, proxy);
			if (!cookies.isEmpty()) {
				StringBuffer sbCookies = new StringBuffer();
				for (int i = 0; i < cookies.size(); i++) {
					if (i > 0) {
						sbCookies.append("; ");
					}
					sbCookies.append((String) cookies.get(i));
				}
				get.setHeader(SM.COOKIE, sbCookies.toString());
			}
			if (StringUtil.isNotEmpty(referer)) {
				get.setHeader(HttpHeaders.REFERER, referer);
			}
			get.setHeader(HttpHeaders.USER_AGENT, DEFAULT_USER_AGENT);
			CloseableHttpResponse response = httpClient.execute(get);
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != HttpStatus.SC_OK) {
				logger.info("[" + status.getStatusCode() + "]" + status.getReasonPhrase());
			}
			HeaderElementIterator headers = new BasicHeaderElementIterator(response.headerIterator(SM.SET_COOKIE));
			if (headers != null) {
				parseCookies(cookies, headers);
			}

			try {
				// 执行get请求
				HttpEntity entity = response.getEntity(); // 获取响应实体
				try {
					if (null != entity) {
						bt = EntityUtils.toByteArray(entity);
					}
				} finally {
					EntityUtils.consume(entity);
				}
			} finally {
				HttpClientUtils.closeQuietly(response);
			}
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException", e);
		} catch (IOException e) {
			logger.error("IOException", e);
		}
		return bt;
	}

	public static boolean getUrlImageWithCookies(String url, List<String> cookies, String referer, HttpHost proxy, String imagePath, String citypy) throws Exception {
		HttpGet get = new HttpGet(url);
		try {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(CONNECTION_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).setConnectionRequestTimeout(READ_TIME_OUT).build();
			get.setConfig(requestConfig);
			HttpClientProxyUtils.setProxy(get, proxy);

			if (!cookies.isEmpty()) {
				StringBuffer sbCookies = new StringBuffer();
				for (int i = 0; i < cookies.size(); i++) {
					if (i > 0) {
						sbCookies.append("; ");
					}
					sbCookies.append((String) cookies.get(i));
				}
				get.setHeader(SM.COOKIE, sbCookies.toString());
			}
			if (StringUtil.isNotEmpty(referer)) {
				get.setHeader(HttpHeaders.REFERER, referer);
			}

			get.setHeader(HttpHeaders.USER_AGENT, DEFAULT_USER_AGENT);
			CloseableHttpResponse response = httpClient.execute(get);
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != HttpStatus.SC_OK) {
				logger.info("[" + status.getStatusCode() + "]" + status.getReasonPhrase());
			}

			HeaderElementIterator headers = new BasicHeaderElementIterator(response.headerIterator(SM.SET_COOKIE));
			if (headers != null) {
				parseCookies(cookies, headers);
			}
			try {
				// 执行get请求
				HttpEntity entity = response.getEntity(); // 获取响应实体
				try {
					if (null != entity) {
						InputStream is = entity.getContent();
						File file = new File(imagePath);
						FileOutputStream out = new FileOutputStream(file);
						int i = 0;
						while ((i = is.read()) != -1) {
							out.write(i);
						}
						out.close();
					}
				} finally {
					EntityUtils.consume(entity);
				}
			} finally {
				HttpClientUtils.closeQuietly(response);
			}
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException", e);
		} catch (IOException e) {
			logger.error("IOException", e);
		}
		return true;
	}

	public static boolean getUrlImageGIFWithCookies(String url, List<String> cookies, String referer, HttpHost proxy, String imagePath, String citypy) {
		HttpGet get = new HttpGet(url);
		try {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(CONNECTION_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).setConnectionRequestTimeout(READ_TIME_OUT).build();
			get.setConfig(requestConfig);
			HttpClientProxyUtils.setProxy(get, proxy);
			if (!cookies.isEmpty()) {
				StringBuffer sbCookies = new StringBuffer();
				for (int i = 0; i < cookies.size(); i++) {
					if (i > 0) {
						sbCookies.append("; ");
					}
					sbCookies.append((String) cookies.get(i));
				}
				get.setHeader(SM.COOKIE, sbCookies.toString());
			}
			if (StringUtil.isNotEmpty(referer)) {
				get.setHeader(HttpHeaders.REFERER, referer);
			}
			get.setHeader(HttpHeaders.USER_AGENT, DEFAULT_USER_AGENT);
			CloseableHttpResponse response = httpClient.execute(get);
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != HttpStatus.SC_OK) {
				logger.info("[" + status.getStatusCode() + "]" + status.getReasonPhrase());
			}

			HeaderElementIterator headers = new BasicHeaderElementIterator(response.headerIterator(SM.SET_COOKIE));
			if (headers != null) {
				parseCookies(cookies, headers);
			}
			try {
				// 执行get请求
				HttpEntity entity = response.getEntity(); // 获取响应实体
				try {
					if (null != entity) {
						InputStream is = entity.getContent();
						GifDecoder gif = new GifDecoder();
						gif.read(is);
						BufferedImage image = gif.getFrame(0);
						ImageIO.write(image, "gif", new File(imagePath));
					}
				} finally {
					EntityUtils.consume(entity);
				}
			} finally {
				HttpClientUtils.closeQuietly(response);
			}
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException", e);
		} catch (IOException e) {
			logger.error("IOException", e);
		}
		return true;
	}

	public static String postURLContentsWithCookies(String url, Map<String, String> params, List<String> cookies, String referer, HttpHost proxy) {
		String responseContent = "";
		HttpPost post = new HttpPost(url);
		HttpClientContext context = HttpClientContext.create();
		try {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(CONNECTION_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).setConnectionRequestTimeout(READ_TIME_OUT).build();
			post.setConfig(requestConfig);
			HttpClientProxyUtils.setProxy(post, proxy);
			post.setHeader(HttpHeaders.ACCEPT, "application/x-ms-application, image/jpeg, application/xaml+xml, image/gif, image/pjpeg, application/x-ms-xbap, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			if (StringUtil.isNotEmpty(referer)) {
				post.setHeader(HttpHeaders.REFERER, referer);
			}
			post.setHeader(HttpHeaders.ACCEPT, "application/x-ms-application, image/jpeg, application/xaml+xml, image/gif, image/pjpeg, application/x-ms-xbap, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			post.setHeader(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN");
			post.setHeader(HttpHeaders.USER_AGENT, DEFAULT_USER_AGENT);
			post.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
			post.setHeader(HttpHeaders.HOST, post.getURI().getHost());
			post.setHeader(HttpHeaders.CONNECTION, "Keep-Alive");
			post.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache");

			if (!cookies.isEmpty()) {
				StringBuffer sbCookies = new StringBuffer();
				for (int i = 0; i < cookies.size(); i++) {
					if (i > 0) {
						sbCookies.append("; ");
					}
					sbCookies.append((String) cookies.get(i));
				}
				post.setHeader(SM.COOKIE, sbCookies.toString());
			}
			List<NameValuePair> formParams = new ArrayList<NameValuePair>();
			// 绑定到请求 Entry
			for (Entry<String, String> entry : params.entrySet()) {
				formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			post.setEntity(new UrlEncodedFormEntity(formParams, Consts.UTF_8));
			// post.setHeader(HttpHeaders.CONTENT_LENGTH, );
			CloseableHttpResponse response = httpClient.execute(post);
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != HttpStatus.SC_OK) {
				logger.info("[" + status.getStatusCode() + "]" + status.getReasonPhrase());
			}
			int code = status.getStatusCode();
			if (code == HttpStatus.SC_MOVED_PERMANENTLY || code == HttpStatus.SC_MOVED_TEMPORARILY || code == HttpStatus.SC_TEMPORARY_REDIRECT) {
				HeaderElementIterator headers = new BasicHeaderElementIterator(response.headerIterator(SM.SET_COOKIE));
				if (headers != null) {
					parseCookies(cookies, headers);
				}

				headers = new BasicHeaderElementIterator(response.headerIterator(SM.COOKIE));

				if (headers != null) {
					parseCookies(cookies, headers);
				}
				HttpHost target = context.getTargetHost();
				List<URI> redirectLocations = context.getRedirectLocations();
				URI location = URIUtils.resolve(post.getURI(), target, redirectLocations);
				logger.info("Final HTTP location: " + location.toASCIIString());
				// Header locations = response.getFirstHeader(HttpHeaders.LOCATION);
				if (location != null) {
					return postURLContentsWithCookies(location.toString(), params, cookies, referer);
				}
			}

			HeaderElementIterator headers = new BasicHeaderElementIterator(response.headerIterator(SM.SET_COOKIE));
			if (headers != null) {
				parseCookies(cookies, headers);
			}

			headers = new BasicHeaderElementIterator(response.headerIterator(SM.COOKIE));

			if (headers != null) {
				parseCookies(cookies, headers);
			}
			try {
				// 执行get请求
				HttpEntity entity = response.getEntity(); // 获取响应实体
				try {
					if (null != entity) {
						responseContent = EntityUtils.toString(entity, "UTF-8");
					}
				} finally {
					EntityUtils.consume(entity);
				}
			} finally {
				HttpClientUtils.closeQuietly(response);
			}
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException", e);
		} catch (IOException e) {
			logger.error("IOException", e);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseContent;
	}

	public static String postURLContentsWithCookies(String url, Map<String, String> params, List<String> cookies, String referer) {
		return postURLContentsWithCookies(url, params, cookies, referer, null);
	}

	public static String postURLContentsToCheckText(String url, Map<String, String> params, String referer, String encode, HttpHost proxy) throws Exception {
		String responseContent = "";
		HttpPost post = new HttpPost(url);
		try {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(CONNECTION_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).setConnectionRequestTimeout(READ_TIME_OUT).build();
			post.setConfig(requestConfig);
			HttpClientProxyUtils.setProxy(post, proxy);
			post.setHeader(HttpHeaders.ACCEPT, "application/x-ms-application, image/jpeg, application/xaml+xml, image/gif, image/pjpeg, application/x-ms-xbap, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			if (StringUtil.isNotEmpty(referer)) {
				post.setHeader(HttpHeaders.REFERER, referer);
			}
			post.setHeader(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN");
			post.setHeader(HttpHeaders.USER_AGENT, DEFAULT_USER_AGENT);
			post.setHeader(HttpHeaders.CONTENT_TYPE, "text/plain");
			post.setHeader(HttpHeaders.HOST, post.getURI().getHost());
			post.setHeader(HttpHeaders.CONNECTION, "Keep-Alive");
			post.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
			List<NameValuePair> formParams = new ArrayList<NameValuePair>();
			// 绑定到请求 Entry
			for (Entry<String, String> entry : params.entrySet()) {
				formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			post.setEntity(new UrlEncodedFormEntity(formParams, encode));
			// post.setHeader(HttpHeaders.CONTENT_LENGTH, );
			CloseableHttpResponse response = httpClient.execute(post);
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != HttpStatus.SC_OK) {
				logger.info("[" + status.getStatusCode() + "]" + status.getReasonPhrase());
			}
			try {
				// 执行get请求
				HttpEntity entity = response.getEntity(); // 获取响应实体
				try {
					if (null != entity) {
						responseContent = EntityUtils.toString(entity, encode);
					}
				} finally {
					EntityUtils.consume(entity);
				}
			} finally {
				HttpClientUtils.closeQuietly(response);
			}
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException", e);
		} catch (IOException e) {
			logger.error("IOException", e);
		}
		return responseContent;
	}

	public static String postURLContentsToCheckForm(String url, Map<String, String> params, String referer, String encode, HttpHost proxy) throws Exception {
		String ret = "";
		HttpPost post = new HttpPost(url);
		try {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(CONNECTION_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).setConnectionRequestTimeout(READ_TIME_OUT).build();
			post.setConfig(requestConfig);
			HttpClientProxyUtils.setProxy(post, proxy);
			post.setHeader(HttpHeaders.ACCEPT, "application/x-ms-application, image/jpeg, application/xaml+xml, image/gif, image/pjpeg, application/x-ms-xbap, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			if (StringUtil.isNotEmpty(referer)) {
				post.setHeader(HttpHeaders.REFERER, referer);
			}
			post.setHeader(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN");
			post.setHeader(HttpHeaders.USER_AGENT, DEFAULT_USER_AGENT);
			post.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
			post.setHeader(HttpHeaders.HOST, post.getURI().getHost());
			post.setHeader(HttpHeaders.CONNECTION, "Keep-Alive");
			post.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
			List<NameValuePair> formParams = new ArrayList<NameValuePair>();
			// 绑定到请求 Entry
			for (Entry<String, String> entry : params.entrySet()) {
				formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			post.setEntity(new UrlEncodedFormEntity(formParams, Consts.UTF_8));
			// post.setHeader(HttpHeaders.CONTENT_LENGTH, );
			CloseableHttpResponse response = httpClient.execute(post);
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != HttpStatus.SC_OK) {
				logger.info("[" + status.getStatusCode() + "]" + status.getReasonPhrase());
			}
			try {
				// 执行get请求
				HttpEntity entity = response.getEntity(); // 获取响应实体
				try {
					if (null != entity) {
						ret = EntityUtils.toString(entity, encode);
					}
				} finally {
					EntityUtils.consume(entity);
				}
			} finally {
				HttpClientUtils.closeQuietly(response);
			}
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException", e);
		} catch (IOException e) {
			logger.error("IOException", e);
		}
		return ret;
	}

	public static String postURLContentsWithCookies(String url, Map<String, String> params, List<String> cookies, String referer, String encode, HttpHost proxy) throws Exception {
		String responseContent = "";
		HttpPost post = new HttpPost(url);
		HttpClientContext context = HttpClientContext.create();
		try {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(CONNECTION_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).setConnectionRequestTimeout(450000).build();
			post.setConfig(requestConfig);
			HttpClientProxyUtils.setProxy(post, proxy);
			post.setHeader(HttpHeaders.ACCEPT, "application/x-ms-application, image/jpeg, application/xaml+xml, image/gif, image/pjpeg, application/x-ms-xbap, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			if (StringUtil.isNotEmpty(referer)) {
				post.setHeader(HttpHeaders.REFERER, referer);
			}
			post.setHeader(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN");
			post.setHeader(HttpHeaders.USER_AGENT, DEFAULT_USER_AGENT);
			post.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
			post.setHeader(HttpHeaders.HOST, post.getURI().getHost());
			post.setHeader(HttpHeaders.CONNECTION, "Keep-Alive");
			post.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
			List<NameValuePair> formParams = new ArrayList<NameValuePair>();
			// 绑定到请求 Entry
			for (Entry<String, String> entry : params.entrySet()) {
				formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(formParams, encode);
			post.setEntity(formEntity);
			if (!cookies.isEmpty()) {
				StringBuffer sbCookies = new StringBuffer();
				for (int i = 0; i < cookies.size(); i++) {
					if (i > 0) {
						sbCookies.append("; ");
					}
					sbCookies.append((String) cookies.get(i));
				}
				post.setHeader(SM.COOKIE, sbCookies.toString());
			}
			CloseableHttpResponse response = httpClient.execute(post);
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != HttpStatus.SC_OK) {
				logger.info("[" + status.getStatusCode() + "]" + status.getReasonPhrase());
			}
			int code = status.getStatusCode();
			if (code == HttpStatus.SC_MOVED_PERMANENTLY || code == HttpStatus.SC_MOVED_TEMPORARILY || code == HttpStatus.SC_TEMPORARY_REDIRECT) {
				HeaderElementIterator headers = new BasicHeaderElementIterator(response.headerIterator(SM.SET_COOKIE));
				if (headers != null) {
					parseCookies(cookies, headers);
				}

				headers = new BasicHeaderElementIterator(response.headerIterator(SM.COOKIE));

				if (headers != null) {
					parseCookies(cookies, headers);
				}
				HttpHost target = context.getTargetHost();
				List<URI> redirectLocations = context.getRedirectLocations();
				URI location = URIUtils.resolve(post.getURI(), target, redirectLocations);
				logger.info("Final HTTP location: " + location.toASCIIString());
				// Header locations = response.getFirstHeader(HttpHeaders.LOCATION);
				if (location != null) {
					if (location.toString().equals("weizhang.asp")) {
						return "查询验证码输入错误，请重新输入!";
					} else {
						return postURLContentsWithCookies(location.toString(), params, cookies, referer, encode, proxy);
					}
				}
			}
			HeaderElementIterator headers = new BasicHeaderElementIterator(response.headerIterator(SM.SET_COOKIE));
			if (headers != null) {
				parseCookies(cookies, headers);
			}
			try {
				// 执行get请求
				HttpEntity entity = response.getEntity(); // 获取响应实体
				try {
					if (null != entity) {
						responseContent = EntityUtils.toString(entity, encode);
					}
				} finally {
					EntityUtils.consume(entity);
				}
			} finally {
				HttpClientUtils.closeQuietly(response);
			}
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException", e);
		} catch (IOException e) {
			logger.error("IOException", e);
		}
		return responseContent;
	}

	public static String postURLContentsWithCookiesString(String url, String params, List<String> cookies, String referer, String charset, HttpHost proxy) throws Exception {
		String responseContent = "";
		HttpPost post = new HttpPost(url);
		HttpClientContext context = HttpClientContext.create();
		try {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(CONNECTION_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).setConnectionRequestTimeout(450000).build();
			post.setConfig(requestConfig);
			HttpClientProxyUtils.setProxy(post, proxy);
			post.setHeader(HttpHeaders.ACCEPT, "application/x-ms-application, image/jpeg, application/xaml+xml, image/gif, image/pjpeg, application/x-ms-xbap, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			if (StringUtil.isNotEmpty(referer)) {
				post.setHeader(HttpHeaders.REFERER, referer);
			}
			post.setHeader(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN");
			post.setHeader(HttpHeaders.USER_AGENT, DEFAULT_USER_AGENT);
			post.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
			post.setHeader(HttpHeaders.HOST, post.getURI().getHost());
			post.setHeader(HttpHeaders.CONNECTION, "Keep-Alive");
			post.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
			post.setEntity(new StringEntity(params));
			if (!cookies.isEmpty()) {
				StringBuffer sbCookies = new StringBuffer();
				for (int i = 0; i < cookies.size(); i++) {
					if (i > 0) {
						sbCookies.append("; ");
					}
					sbCookies.append((String) cookies.get(i));
				}
				post.setHeader(SM.COOKIE, sbCookies.toString());
			}
			CloseableHttpResponse response = httpClient.execute(post);
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != HttpStatus.SC_OK) {
				logger.info("[" + status.getStatusCode() + "]" + status.getReasonPhrase());
			}
			int code = status.getStatusCode();
			if (code == HttpStatus.SC_MOVED_PERMANENTLY || code == HttpStatus.SC_MOVED_TEMPORARILY || code == HttpStatus.SC_TEMPORARY_REDIRECT) {
				HeaderElementIterator headers = new BasicHeaderElementIterator(response.headerIterator(SM.SET_COOKIE));
				if (headers != null) {
					parseCookies(cookies, headers);
				}

				headers = new BasicHeaderElementIterator(response.headerIterator(SM.COOKIE));

				if (headers != null) {
					parseCookies(cookies, headers);
				}
				HttpHost target = context.getTargetHost();
				List<URI> redirectLocations = context.getRedirectLocations();
				URI location = URIUtils.resolve(post.getURI(), target, redirectLocations);
				logger.info("Final HTTP location: " + location.toASCIIString());
				// Header locations = response.getFirstHeader(HttpHeaders.LOCATION);
				if (location != null) {
					if (location.toString().equals("weizhang.asp")) {
						return "查询验证码输入错误，请重新输入!";
					} else {
						return postURLContentsWithCookiesString(location.toString(), params, cookies, referer, charset, proxy);
					}
				}
			}
			HeaderElementIterator headers = new BasicHeaderElementIterator(response.headerIterator(SM.SET_COOKIE));
			if (headers != null) {
				parseCookies(cookies, headers);
			}
			try {
				// 执行get请求
				HttpEntity entity = response.getEntity(); // 获取响应实体
				try {
					if (null != entity) {
						responseContent = EntityUtils.toString(entity, charset);
					}
				} finally {
					EntityUtils.consume(entity);
				}
			} finally {
				HttpClientUtils.closeQuietly(response);
			}
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException", e);
		} catch (IOException e) {
			logger.error("IOException", e);
		}
		return responseContent;
	}

	public static String postURLContentsWithCookiesForAjax(String url, Map<String, String> params, List<String> cookies, String referer, String encode, HttpHost proxy) throws Exception {
		String responseContent = "";
		HttpPost post = new HttpPost(url);
		HttpClientContext context = HttpClientContext.create();
		try {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(CONNECTION_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).setConnectionRequestTimeout(450000).build();
			post.setConfig(requestConfig);
			HttpClientProxyUtils.setProxy(post, proxy);
			post.setHeader(HttpHeaders.ACCEPT, "application/json, text/javascript, */*");
			if (StringUtil.isNotEmpty(referer)) {
				post.setHeader(HttpHeaders.REFERER, referer);
			}
			post.setHeader(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.8,en;q=0.6");
			post.setHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate");
			post.setHeader(HttpHeaders.USER_AGENT, DEFAULT_USER_AGENT);
			post.setHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");
			post.setHeader(HttpHeaders.HOST, post.getURI().getHost());
			post.setHeader(HttpHeaders.CONNECTION, "Keep-Alive");
			post.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
			List<NameValuePair> formParams = new ArrayList<NameValuePair>();
			// 绑定到请求 Entry
			for (Entry<String, String> entry : params.entrySet()) {
				formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			post.setEntity(new UrlEncodedFormEntity(formParams, encode));
			// connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
			if (!cookies.isEmpty()) {
				StringBuffer sbCookies = new StringBuffer();
				for (int i = 0; i < cookies.size(); i++) {
					if (i > 0) {
						sbCookies.append("; ");
					}
					sbCookies.append((String) cookies.get(i));
				}
				post.setHeader(SM.COOKIE, sbCookies.toString());
			}

			CloseableHttpResponse response = httpClient.execute(post);
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != HttpStatus.SC_OK) {
				logger.info("[" + status.getStatusCode() + "]" + status.getReasonPhrase());
			}
			int code = status.getStatusCode();
			if (code == HttpStatus.SC_MOVED_PERMANENTLY || code == HttpStatus.SC_MOVED_TEMPORARILY || code == HttpStatus.SC_TEMPORARY_REDIRECT) {
				HeaderElementIterator headers = new BasicHeaderElementIterator(response.headerIterator(SM.SET_COOKIE));
				if (headers != null) {
					parseCookies(cookies, headers);
				}
				HttpHost target = context.getTargetHost();
				List<URI> redirectLocations = context.getRedirectLocations();
				URI location = URIUtils.resolve(post.getURI(), target, redirectLocations);
				logger.info("Final HTTP location: " + location.toASCIIString());
				// Header locations = response.getFirstHeader(HttpHeaders.LOCATION);
				if (location != null) {
					if (location.toString().equals("weizhang.asp")) {
						return "查询验证码输入错误，请重新输入!";
					} else {
						return postURLContentsWithCookies(location.toString(), params, cookies, referer, encode, proxy);
					}
				}
			}
			HeaderElementIterator headers = new BasicHeaderElementIterator(response.headerIterator(SM.SET_COOKIE));
			if (headers != null) {
				parseCookies(cookies, headers);
			}
			try {
				// 执行post请求
				HttpEntity entity = response.getEntity(); // 获取响应实体
				try {
					if (null != entity) {
						responseContent = EntityUtils.toString(entity, encode);
					}
				} finally {
					EntityUtils.consume(entity);
				}
			} finally {
				HttpClientUtils.closeQuietly(response);
			}
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException", e);
		} catch (IOException e) {
			logger.error("IOException", e);
		}
		return responseContent;
	}

	public static String postURLContentsWithCookiesForXinjiang(String url, Map<String, String> params, List<String> cookies, String referer, String encode, HttpHost proxy) {
		String responseContent = "";
		HttpPost post = new HttpPost(url);
		HttpClientContext context = HttpClientContext.create();
		try {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(CONNECTION_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).setConnectionRequestTimeout(READ_TIME_OUT).build();
			post.setConfig(requestConfig);
			HttpClientProxyUtils.setProxy(post, proxy);
			post.setHeader(HttpHeaders.ACCEPT, "application/x-ms-application, image/jpeg, application/xaml+xml, image/gif, image/pjpeg, application/x-ms-xbap, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			if (StringUtil.isNotEmpty(referer)) {
				post.setHeader(HttpHeaders.REFERER, referer);
			}
			post.setHeader(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN");
			post.setHeader(HttpHeaders.USER_AGENT, DEFAULT_USER_AGENT);
			post.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
			post.setHeader(HttpHeaders.HOST, post.getURI().getHost());
			post.setHeader(HttpHeaders.CONNECTION, "Keep-Alive");
			post.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
			List<NameValuePair> formParams = new ArrayList<NameValuePair>();
			// 绑定到请求 Entry
			for (Entry<String, String> entry : params.entrySet()) {
				formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			post.setEntity(new UrlEncodedFormEntity(formParams, encode));

			if (!cookies.isEmpty()) {
				StringBuffer sbCookies = new StringBuffer();
				for (int i = 0; i < cookies.size(); i++) {
					if (i > 0) {
						sbCookies.append("; ");
					}
					sbCookies.append((String) cookies.get(i));
				}
				post.setHeader(SM.COOKIE, sbCookies.toString());
			}
			CloseableHttpResponse response = httpClient.execute(post);
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != HttpStatus.SC_OK) {
				logger.info("[" + status.getStatusCode() + "]" + status.getReasonPhrase());
			}
			int code = status.getStatusCode();
			if (code == HttpStatus.SC_MOVED_PERMANENTLY || code == HttpStatus.SC_MOVED_TEMPORARILY || code == HttpStatus.SC_TEMPORARY_REDIRECT) {
				HeaderElementIterator headers = new BasicHeaderElementIterator(response.headerIterator(SM.SET_COOKIE));
				if (headers != null) {
					parseCookies(cookies, headers);
				}
				HttpHost target = context.getTargetHost();
				List<URI> redirectLocations = context.getRedirectLocations();
				URI location = URIUtils.resolve(post.getURI(), target, redirectLocations);
				logger.info("Final HTTP location: " + location.toASCIIString());
				// Header locations = response.getFirstHeader(HttpHeaders.LOCATION);
				if (location != null) {
					return getURLContentsWithCookies(location.toString(), cookies, referer, proxy, encode);
				}
			}
			HeaderElementIterator headers = new BasicHeaderElementIterator(response.headerIterator(SM.SET_COOKIE));
			if (headers != null) {
				parseCookies(cookies, headers);
			}
			try {
				// 执行get请求
				HttpEntity entity = response.getEntity(); // 获取响应实体
				try {
					if (null != entity) {
						responseContent = EntityUtils.toString(entity, encode);
					}
				} finally {
					EntityUtils.consume(entity);
				}
			} finally {
				HttpClientUtils.closeQuietly(response);
			}
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException", e);
		} catch (IOException e) {
			logger.error("IOException", e);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseContent;
	}

	/**
     * 
     */
	public static byte[] postPPURLBinaryContents(String url, byte[] inBuf) {
		byte[] retBuf = null;
		HttpPost post = new HttpPost(url);
		Socket socket = null;
		BufferedWriter wr = null;
		InputStream is = null;
		try {
			String hostname = post.getURI().getHost();
			int port = 80;
			InetAddress addr = InetAddress.getByName(hostname);

			socket = new Socket(addr, port);

			socket.setSoTimeout(CONNECTION_TIMEOUT);

			StringBuffer sbHeader = new StringBuffer();
			wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			sbHeader.append("POST " + url + " HTTP/1.1\r\n");
			sbHeader.append("User-Agent: 25PP\r\n");
			sbHeader.append("Host: dev-auth.25pp.com\r\n");
			sbHeader.append("Content-Length: " + inBuf.length + "\r\n");
			sbHeader.append("Pragma: no-cache\r\n");
			sbHeader.append("Cookie: __utma=113739972.2008533957.1359344528.1359344528.1359344528.1; __utmz=113739972.1359344528.1.1.utmcsr=pro.25pp.com|utmccn=(referral)|utmcmd=referral|utmcct=/; Hm_lvt_80c7667d40c35eec40368ef5cd6547d4=1359344573,1359345104,1359345729\r\n");
			sbHeader.append("\r\n");

			OutputStream os = socket.getOutputStream();
			os.write(sbHeader.toString().getBytes());
			os.write(inBuf);
			os.flush();

			is = socket.getInputStream();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			byte[] tmpBuf = new byte[10240];
			int iLen = is.read(tmpBuf);
			while (iLen >= 0) {
				if (iLen > 0)
					baos.write(tmpBuf, 0, iLen);
				iLen = is.read(tmpBuf);
			}
			retBuf = baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (wr != null) {
				try {
					wr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return retBuf;
	}

	public static byte[] postHttpURLBinaryContents(String url, byte[] inBuf, Map<String, String> params, HttpHost proxy) {
		byte[] retBuf = null;
		HttpPost post = new HttpPost(url);
		try {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(CONNECTION_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).setConnectionRequestTimeout(READ_TIME_OUT).build();
			post.setConfig(requestConfig);
			HttpClientProxyUtils.setProxy(post, proxy);

			List<NameValuePair> formParams = new ArrayList<NameValuePair>();
			// 绑定到请求 Entry
			for (Entry<String, String> entry : params.entrySet()) {
				formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			post.setEntity(new UrlEncodedFormEntity(formParams, Consts.UTF_8));

			post.setHeader(HttpHeaders.HOST, post.getURI().getHost());
			// post.setEntity(new ByteArrayEntity(inBuf, ContentType.APPLICATION_ATOM_XML));
			post.setEntity(new ByteArrayEntity(inBuf));
			CloseableHttpResponse response = httpClient.execute(post);
			try {
				// 执行请求
				HttpEntity entity = response.getEntity(); // 获取响应实体
				try {
					if (null != entity) {
						retBuf = EntityUtils.toByteArray(entity);
					}
				} finally {
					EntityUtils.consume(entity);
				}
			} finally {
				HttpClientUtils.closeQuietly(response);
			}
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException", e);
		} catch (IOException e) {
			logger.error("IOException", e);
		}

		return retBuf;
	}

	public static byte[] postHttpURLBinaryContents(String url, byte[] inBuf, List<String> cookies, String referer, String userAgent) {
		byte[] retBuf = null;
		HttpPost post = new HttpPost(url);
		try {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(CONNECTION_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).setConnectionRequestTimeout(READ_TIME_OUT).build();
			post.setConfig(requestConfig);
			if (StringUtil.isNotEmpty(referer)) {
				post.setHeader(HttpHeaders.REFERER, referer);
			}

			if (StringUtil.isNotEmpty(userAgent)) {
				post.setHeader(HttpHeaders.USER_AGENT, userAgent);
			} else {
				post.setHeader(HttpHeaders.USER_AGENT, DEFAULT_USER_AGENT);
			}
			post.setHeader(HttpHeaders.HOST, post.getURI().getHost());
			post.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
			if (!cookies.isEmpty()) {
				StringBuffer sbCookies = new StringBuffer();
				for (int i = 0; i < cookies.size(); i++) {
					if (i > 0) {
						sbCookies.append("; ");
					}
					sbCookies.append((String) cookies.get(i));
				}
				post.setHeader(SM.COOKIE, sbCookies.toString());
			}

			if (inBuf != null) {
				post.setEntity(new ByteArrayEntity(inBuf));
			}

			if (!cookies.isEmpty()) {
				StringBuffer sbCookies = new StringBuffer();
				for (int i = 0; i < cookies.size(); i++) {
					if (i > 0) {
						sbCookies.append("; ");
					}
					sbCookies.append((String) cookies.get(i));
				}
				post.setHeader(SM.COOKIE, sbCookies.toString());
			}
			CloseableHttpResponse response = httpClient.execute(post);
			try {
				// 执行请求
				HttpEntity entity = response.getEntity(); // 获取响应实体
				try {
					if (null != entity) {
						retBuf = EntityUtils.toByteArray(entity);
					}
				} finally {
					EntityUtils.consume(entity);
				}
			} finally {
				HttpClientUtils.closeQuietly(response);
			}

		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException", e);
		} catch (IOException e) {
			logger.error("IOException", e);
		}
		return retBuf;
	}

	public static String getGo2MapDirect(String city, int page, String key) {
		StringBuffer sb = new StringBuffer();
		try {
			String hostname = "lightmip.go2map.com";
			int port = 80;
			InetAddress addr = InetAddress.getByName(hostname);

			Socket socket = new Socket(addr, port);
			socket.setSoTimeout(120000);

			String path = "/BlurrySearch/BlurrySearch.asp?hidden_MapTool=BlurrySearch&hidden_Variant=HirerID==go2map!!DestGeoset==" + city + "!!FeatureDesc==" + key;
			String strPage = "50," + page;
			String value = ":ALL!!FeatureDescType==Name,,!!ResultOrder==" + strPage + "!!QueryFlag==1&viodDateAndTime=1092053655810";

			StringBuffer sbHeader = new StringBuffer();
			BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

			sbHeader.append("GET " + path + value + " HTTP/1.1\r\n");

			sbHeader.append("Host: lightmip.go2map.com\r\n");
			sbHeader.append("Accept: */*\r\n");
			sbHeader.append("Accept-Encoding: gzip, deflate\r\n");
			sbHeader.append("Accept-Language: zh-cn\r\n");
			sbHeader.append("Cookie: SITESERVER=ID=76b78be4f53d2556a5cd129a24e1ba16\r\n");

			sbHeader.append("Connection: Keep-Alive\r\n");
			sbHeader.append("Referer: http://www.go2map.com/lightmip/map/mappage.asp\r\n");
			sbHeader.append("User-Agent: Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; .NET CLR 1.1.4322)\r\n");

			sbHeader.append("\r\n");

			wr.write(sbHeader.toString());
			wr.flush();

			BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				if ((line == null) || (line.indexOf("var js_Return =") < 0))
					continue;
				sb.append(line);
				sb.append("\n");
				break;
			}

			wr.close();
			rd.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	public static String getGo2MapPois(String strAppID, int page, String strID) {
		StringBuffer sb = new StringBuffer();
		try {
			String hostname = "lspengine.go2map.com";
			int port = 80;
			InetAddress addr = InetAddress.getByName(hostname);

			Socket socket = new Socket(addr, port);
			socket.setSoTimeout(120000);

			String path = "/WebSiteAD/Engine?hidden_MapTool=search.FuzzyQuery&hidden_Variant=UserInfo==,,!!APPID==" + strAppID + "!!SPInfo==1,1!!FeatureDescType==Name,,!!FeatureDesc==_:" + strID + "!!ResultOrder==50," + page + "!!QueryFlag==1&hidden_APPID=" + strAppID + "&rnd=1118839257819452";

			StringBuffer sbHeader = new StringBuffer();
			BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

			sbHeader.append("GET " + path + " HTTP/1.1\r\n");

			sbHeader.append("Host: lspengine.go2map.com\r\n");
			sbHeader.append("Accept: */*\r\n");
			sbHeader.append("Accept-Encoding: gzip, deflate\r\n");
			sbHeader.append("Accept-Language: zh-cn\r\n");
			sbHeader.append("Cookie: JSESSIONID=626D2BD3C6CC0A8AE1319F54AD9EBE87; SITESERVER=ID=76b78be4f53d2556a5cd129a24e1ba16; SUV=0412260036523386\r\n");

			sbHeader.append("Connection: Keep-Alive\r\n");
			sbHeader.append("Referer: http://maps.go2map.com/city/syspage/mapEngineProcess.htm?rnd=1118839257819021\r\n");
			sbHeader.append("User-Agent: Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; .NET CLR 1.1.4322)\r\n");

			sbHeader.append("\r\n");

			wr.write(sbHeader.toString());
			wr.flush();

			BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				if ((line == null) || (line.indexOf("var js_Return =") < 0))
					continue;
				sb.append(line);
				sb.append("\n");
				break;
			}

			wr.close();
			rd.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	public static String getGo2MapSearchContents(String strAppID, String key, int page) {
		StringBuffer sb = new StringBuffer();
		try {
			String hostname = "localsearch.go2map.com";
			int port = 80;
			InetAddress addr = InetAddress.getByName(hostname);

			Socket socket = new Socket(addr, port);
			socket.setSoTimeout(300000);

			String path = "/LocalWeb/Engine?hidden_MapTool=local.LocalSearchXMLEX&hidden_Variant=APPID==" + strAppID + "!!UserInfo==,,!!SPInfo==1,1!!FeatureDescType==Name,,!!FeatureDesc==ALL:ALL!!ObjectLayers==ALL!!ResultOrder==10," + page + "!!What==" + key + "!!QueryFlag==1&hidden_APPID=" + strAppID + "&rnd=1132499189455481";

			StringBuffer sbHeader = new StringBuffer();
			BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

			sbHeader.append("GET " + path + " HTTP/1.1\r\n");

			sbHeader.append("Host: " + hostname + "\r\n");
			sbHeader.append("Accept: */*\r\n");
			sbHeader.append("Accept-Encoding: gzip, deflate\r\n");
			sbHeader.append("Accept-Language: zh-cn\r\n");

			sbHeader.append("Connection: Keep-Alive\r\n");
			sbHeader.append("Referer: http://maps.sogou.com/syspage/p.htm\r\n");
			sbHeader.append("User-Agent: Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; .NET CLR 1.1.4322)\r\n");

			sbHeader.append("\r\n");

			wr.write(sbHeader.toString());
			wr.flush();

			BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			boolean bRet = false;
			String line;
			while ((line = rd.readLine()) != null) {
				if ((line != null) && (line.indexOf("var js_Return =") >= 0)) {
					bRet = true;
				}

				if (!(bRet))
					continue;
				sb.append(line);
			}
			wr.close();
			rd.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	public static String getGo2MapBusRoute(String strAppID, String strRND, String strID) {
		StringBuffer sb = new StringBuffer();
		try {
			String hostname = "lspengine.go2map.com";
			int port = 80;
			InetAddress addr = InetAddress.getByName(hostname);

			Socket socket = new Socket(addr, port);
			socket.setSoTimeout(120000);

			String path = "/WebSiteAD/Engine?hidden_MapTool=search.QueryFeatureAttribute&hidden_Variant=UserInfo==,,!!APPID==" + strAppID + "!!SPInfo==1,1!!FeatureDescType==UID,,!!FeatureDesc==" + strID + "&hidden_APPID=" + strAppID + "&rnd=" + strRND;

			StringBuffer sbHeader = new StringBuffer();
			BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

			sbHeader.append("GET " + path + " HTTP/1.1\r\n");

			sbHeader.append("Host: lspengine.go2map.com\r\n");
			sbHeader.append("Accept: */*\r\n");
			sbHeader.append("Accept-Encoding: gzip, deflate\r\n");
			sbHeader.append("Accept-Language: zh-cn\r\n");
			sbHeader.append("Cookie: JSESSIONID=3EE95CEB0717A76BBEA1366C496A7109; SITESERVER=ID=76b78be4f53d2556a5cd129a24e1ba16; SUV=0412260036523386\r\n");

			sbHeader.append("Connection: Keep-Alive\r\n");
			sbHeader.append("Referer: http://maps.go2map.com/city/syspage/mapEngineProcess.htm?rnd=1118839698582013\r\n");
			sbHeader.append("User-Agent: Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; .NET CLR 1.1.4322)\r\n");

			sbHeader.append("\r\n");

			wr.write(sbHeader.toString());
			wr.flush();

			BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				if ((line == null) || (line.indexOf("var js_Return =") < 0))
					continue;
				sb.append(line);
				sb.append("\n");
				break;
			}

			wr.close();
			rd.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	public static String postRawWeiboForm(String url, String strData) {
		String strResp = null;
		try {
			String hostname = "weibo.cn";
			int port = 80;
			InetAddress addr = InetAddress.getByName(hostname);
			Socket socket = new Socket(addr, port);
			socket.setSoTimeout(120000);

			StringBuffer sbHeader = new StringBuffer();
			BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			sbHeader.append("POST " + url + " HTTP/1.1\r\n");
			sbHeader.append("Host: " + hostname + "\r\n");
			sbHeader.append("Accept: */*\r\n");

			sbHeader.append("Accept-Language: zh-cn\r\n");
			sbHeader.append("Connection: Keep-Alive\r\n");
			sbHeader.append("Referer: " + url + "\r\n");
			sbHeader.append("User-Agent: Opera/9.80 (Android 2.3.7; Linux; Opera Mobi/35779; U; zh-cn) Presto/2.10.254 Version/12.00\r\n");
			sbHeader.append("Content-Length: " + strData.length() + "\r\n");
			sbHeader.append("Content-Type: application/x-www-form-urlencoded\r\n");
			sbHeader.append("\r\n");

			wr.write(sbHeader.toString());

			wr.write(strData + "\r\n");
			wr.flush();

			BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream(), DEFAULT_ENCODE));

			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null) {
				if (!(StringUtil.isNotEmpty(line)))
					continue;
				sb.append(line + "\n");
			}

			wr.close();
			rd.close();
			socket.close();
			strResp = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strResp;
	}

	public static String postRawUmeng(String strUrl, String strData, Map<String, String> params) {
		String strResp = null;
		try {
			String hostname = "alog.umeng.com";
			int port = 80;
			InetAddress addr = InetAddress.getByName(hostname);
			Socket socket = new Socket(addr, port);
			socket.setSoTimeout(120000);

			StringBuffer sbHeader = new StringBuffer();
			BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			sbHeader.append("POST " + strUrl + " HTTP/1.1\r\n");
			sbHeader.append("Host: " + hostname + "\r\n");
			sbHeader.append("Connection: Keep-Alive\r\n");
			if ((params != null) && (!(params.isEmpty()))) {
				for (Entry<String, String> entry : params.entrySet()) {
					String strKey = (String) entry.getKey();
					String strValue = (String) params.get(strKey);
					sbHeader.append(strKey + ": " + strValue + "\r\n");
				}
			}
			sbHeader.append("\r\n");

			wr.write(sbHeader.toString());

			wr.write(strData + "\r\n");
			wr.flush();

			BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream(), DEFAULT_ENCODE));

			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null) {
				if (!(StringUtil.isNotEmpty(line)))
					continue;
				sb.append(line + "\n");
			}

			wr.close();
			rd.close();
			socket.close();
			strResp = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strResp;
	}

	public static String getURLContents(String url, HttpHost proxy) {
		HttpGet get = new HttpGet(url);
		String ret = "";
		try {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(CONNECTION_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).setConnectionRequestTimeout(READ_TIME_OUT).build();
			get.setConfig(requestConfig);
			HttpClientProxyUtils.setProxy(get, proxy);
			String strUserAgent = "Mozilla/5.0 (Linux; U; Android 2.1-update1; en-us; ADR6300 Build/ERE27) AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0 Mobile Safari/530.17";
			strUserAgent = DEFAULT_USER_AGENT;

			strUserAgent = System.getProperty("http.agent", strUserAgent);
			get.setHeader(HttpHeaders.USER_AGENT, strUserAgent);
			CloseableHttpResponse response = httpClient.execute(get);
			try {
				// 执行get请求
				HttpEntity entity = response.getEntity(); // 获取响应实体
				try {
					if (null != entity) {
						ret = EntityUtils.toString(entity, DEFAULT_ENCODE);
					}
				} finally {
					EntityUtils.consume(entity);
				}
			} finally {
				HttpClientUtils.closeQuietly(response);
			}
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException", e);
		} catch (IOException e) {
			logger.error("IOException", e);
		}
		return ret;
	}

	public static String getURLContents(String url, HttpHost proxy, Map<String, String> htHeaders) {
		String responseContent = "";
		HttpGet get = new HttpGet(url);
		try {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(CONNECTION_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).setConnectionRequestTimeout(30000).build();
			get.setConfig(requestConfig);
			HttpClientProxyUtils.setProxy(get, proxy);

			String strUserAgent = "Mozilla/5.0 (Linux; U; Android 2.1-update1; en-us; ADR6300 Build/ERE27) AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0 Mobile Safari/530.17";
			strUserAgent = System.getProperty("http.agent", strUserAgent);
			get.setHeader(HttpHeaders.USER_AGENT, strUserAgent);

			if (!(htHeaders.isEmpty())) {
				for (Entry<String, String> entry : htHeaders.entrySet()) {
					get.setHeader(entry.getKey(), entry.getValue());
				}
			}
			CloseableHttpResponse response = httpClient.execute(get);
			try {
				// 执行请求
				HttpEntity entity = response.getEntity(); // 获取响应实体
				try {
					if (null != entity) {
						responseContent = EntityUtils.toString(entity, DEFAULT_ENCODE);
					}
				} finally {
					EntityUtils.consume(entity);
				}
			} finally {
				HttpClientUtils.closeQuietly(response);
			}

		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException", e);
		} catch (IOException e) {
			logger.error("IOException", e);
		}
		return responseContent;
	}

	public static String getURLContentsWithCookie(String url, String cookie) {
		String responseContent = "";
		HttpGet get = new HttpGet(url);
		try {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(CONNECTION_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).setConnectionRequestTimeout(30000).build();
			get.setConfig(requestConfig);
			get.setHeader(SM.COOKIE, cookie);
			get.setHeader(HttpHeaders.USER_AGENT, DEFAULT_USER_AGENT);
			CloseableHttpResponse response = httpClient.execute(get);
			try {
				// 执行get请求
				HttpEntity entity = response.getEntity(); // 获取响应实体
				try {
					if (null != entity) {
						responseContent = EntityUtils.toString(entity, "UTF-8");
					}
				} finally {
					EntityUtils.consume(entity);
				}
			} finally {
				HttpClientUtils.closeQuietly(response);
			}
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException", e);
		} catch (IOException e) {
			logger.error("IOException", e);
		}
		return responseContent;
	}

	public static String getURLContents(String url, String encode) {
		String responseContent = "";
		HttpGet get = new HttpGet(url);
		try {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(CONNECTION_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).setConnectionRequestTimeout(READ_TIME_OUT).build();
			get.setConfig(requestConfig);
			get.setHeader(HttpHeaders.USER_AGENT, "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1) Google Spider 8.18");
			CloseableHttpResponse response = httpClient.execute(get);
			try {
				// 执行get请求
				HttpEntity entity = response.getEntity(); // 获取响应实体
				try {
					if (null != entity) {
						responseContent = EntityUtils.toString(entity, encode);
					}
				} finally {
					EntityUtils.consume(entity);
				}
			} finally {
				HttpClientUtils.closeQuietly(response);
			}
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException", e);
		} catch (IOException e) {
			logger.error("IOException", e);
		}
		return responseContent;
	}

	public static String getURLContentsWithReferer(String url, String referer, HttpHost proxy) {
		String responseContent = "";
		HttpGet get = new HttpGet(url);
		try {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(CONNECTION_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).setConnectionRequestTimeout(READ_TIME_OUT).build();
			get.setConfig(requestConfig);
			HttpClientProxyUtils.setProxy(get, proxy);
			if (StringUtil.isNotEmpty(referer)) {
				get.setHeader(HttpHeaders.REFERER, referer);
			}
			get.setHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Linux; U; Android 2.1-update1; en-us; ADR6300 Build/ERE27) AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0 Mobile Safari/530.17");
			CloseableHttpResponse response = httpClient.execute(get);
			try {
				// 执行get请求
				HttpEntity entity = response.getEntity(); // 获取响应实体
				try {
					if (null != entity) {
						responseContent = EntityUtils.toString(entity, "UTF-8");
					}
				} finally {
					EntityUtils.consume(entity);
				}
			} finally {
				HttpClientUtils.closeQuietly(response);
			}

		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException", e);
		} catch (IOException e) {
			logger.error("IOException", e);
		}
		return responseContent;
	}

	private static void parseCookies(List<String> cookies, HeaderElementIterator headers) {
		try {
			if (!cookies.isEmpty()) {
				int f = 0;
				for (String cookie : cookies) {
					if (cookie.contains("com.trs.idm.coSessionId")) {
						f = 1;
					}
				}
				if (f == 0) {
					cookies.clear();
				}
			}
			// logger.info("headers===============" + headers);
			HeaderElement header = null;
			String name = "";
			String value = "";
			while (headers.hasNext()) {
				header = headers.nextElement();
				name = header.getName();
				value = header.getValue();
				if (!name.equalsIgnoreCase("path") && name.equalsIgnoreCase("domain") && !name.equalsIgnoreCase("expires") && !name.equalsIgnoreCase("deleted") && !name.equalsIgnoreCase("httponly") && !cookies.contains(value)) {
					cookies.add(value);
				}
			}
		} catch (Exception exx) {
			exx.printStackTrace();
		}
	}

	/**
	 * 根据请求Map参数，地址，请求类型 获取请求JSON 结果
	 * 
	 * @param map
	 * @param url
	 * @param type
	 *            get/post
	 * @return
	 */
	public static JSONObject getRequestRestltByJson(Map<String, String> params, String url) {
		JSONObject obj = null;
		int statues = -1;
		String responseString = null;
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SOCKETTIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).setConnectionRequestTimeout(READ_TIME_OUT).build();

		StringBuilder sb = new StringBuilder();
		sb.append(url);
		int i = 0;
		for (Entry<String, String> entry : params.entrySet()) {
			if (i == 0 && !url.contains("?")) {
				sb.append("?");
			} else {
				sb.append("&");
			}
			sb.append(entry.getKey());
			sb.append("=");
			String value = entry.getValue();
			try {
				sb.append(URLEncoder.encode(value, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				logger.warn("encode http get params error, value is " + value, e);
			}
			i++;
		}
		logger.info("[HttpUtils Get] begin invoke:" + sb.toString());
		HttpGet get = new HttpGet(sb.toString());
		get.setConfig(requestConfig);

		try {
			CloseableHttpResponse response = httpClient.execute(get);
			try {
				HttpEntity entity = response.getEntity();
				try {
					if (entity != null) {
						responseString = EntityUtils.toString(entity, "UTF-8");
						statues = response.getStatusLine().getStatusCode();
						try {
							obj = JSONObject.parseObject(responseString);
						} catch (Exception e) {
							obj = new JSONObject();
							obj.put("code", statues);
							obj.put("error", "json format error");
							obj.put("value", responseString);
							logger.info((params.toString() + "-->" + responseString));
							return obj;
						}
					}
				} finally {
					EntityUtils.consume(entity);
				}
			} catch (Exception e) {
				logger.error(String.format("[HttpUtils Get]get response error, url:%s", sb.toString()), e);
				obj = new JSONObject();
				obj.put("code", statues);
				logger.info((params.toString() + "-->" + e.toString()));
				return obj;
			} finally {
				HttpClientUtils.closeQuietly(response);
			}
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException", e);
		} catch (IOException e) {
			logger.error("IOException", e);
		}
		return obj;
	}

	public static JSONObject postRequestRestltByJson(Map<String, String> params, String url) {
		JSONObject obj = null;
		int statues = -1;
		String responseString = null;
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SOCKETTIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).setConnectionRequestTimeout(READ_TIME_OUT).build();
		HttpPost post = new HttpPost(url);
		List<NameValuePair> formParams = new ArrayList<NameValuePair>();
		// 绑定到请求 Entry
		for (Entry<String, String> entry : params.entrySet()) {
			formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		post.setEntity(new UrlEncodedFormEntity(formParams, Consts.UTF_8));
		post.setConfig(requestConfig);

		try {
			CloseableHttpResponse response = httpClient.execute(post);
			try {
				HttpEntity entity = response.getEntity();
				try {
					if (entity != null) {
						responseString = EntityUtils.toString(entity, "UTF-8");
						statues = response.getStatusLine().getStatusCode();
						try {
							obj = JSONObject.parseObject(responseString);
						} catch (Exception e) {
							obj = new JSONObject();
							obj.put("code", statues);
							obj.put("error", "json format error");
							obj.put("value", responseString);
							logger.info((params.toString() + "-->" + responseString));
							return obj;
						}
					}
				} finally {
					EntityUtils.consume(entity);
				}
			} catch (Exception e) {
				obj = new JSONObject();
				obj.put("code", statues);
				logger.info((params.toString() + "-->" + e.toString()));
				return obj;
			} finally {
				HttpClientUtils.closeQuietly(response);
			}
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException", e);
		} catch (IOException e) {
			logger.error("IOException", e);
		}
		return obj;
	}

	/**
	 * 微信相关
	 */
	public static String getWeixinVerify(String connurl) {
		String responseContent = "";
		HttpGet get = new HttpGet(connurl);
		try {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(CONNECTION_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).setConnectionRequestTimeout(READ_TIME_OUT).build();
			get.setConfig(requestConfig);
			get.setHeader(HttpHeaders.ACCEPT_CHARSET, "UTF-8");
			get.setHeader(HttpHeaders.CONTENT_TYPE, "UTF-8");
			CloseableHttpResponse response = httpClient.execute(get);
			StatusLine statusLine = response.getStatusLine();
			int code = statusLine.getStatusCode();
			if (code == 200) {
				try {
					// 执行get请求
					HttpEntity entity = response.getEntity(); // 获取响应实体
					try {
						if (null != entity) {
							responseContent = EntityUtils.toString(entity, "UTF-8");
						}
					} finally {
						EntityUtils.consume(entity);
					}
				} finally {
					HttpClientUtils.closeQuietly(response);
				}
			}
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException", e);
		} catch (IOException e) {
			logger.error("IOException", e);
		}
		return responseContent;
	}

	/**
	 * 发送请求token验证
	 * 
	 * @param tokenUrl
	 * @param token
	 * @return
	 */
	public static int checkToken(String tokenUrl, String token) {
		if (!StringUtil.isNotEmpty(token)) {
			return 2080;
		}
		int code = 0;
		HttpGet get = new HttpGet(tokenUrl);
		try {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(CONNECTION_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).setConnectionRequestTimeout(READ_TIME_OUT).build();
			get.setConfig(requestConfig);
			get.setHeader("user_token", token);
			get.setHeader("Connection", "close");
			CloseableHttpResponse response = httpClient.execute(get);
			StatusLine statusLine = response.getStatusLine();
			code = statusLine.getStatusCode();
			if (code == 200) {
				try {
					// 执行get请求
					HttpEntity entity = response.getEntity(); // 获取响应实体
					try {
						if (null != entity) {
							String responseContent = EntityUtils.toString(entity, "UTF-8");
							JSONObject obj;
							try {
								obj = JSONObject.parseObject(responseContent);
								if (obj.containsKey("code")) {
									if (obj.get("code").equals(200)) {
										return 200;
									} else {
										return 2080;
									}
								} else {
									if (obj.getBoolean("login")) {
										return 200;
									} else {
										return 2080;
									}
								}
							} catch (JSONException e) {
								e.printStackTrace();
								return 1080;
							}

						}
					} finally {
						EntityUtils.consume(entity);
					}
				} finally {
					HttpClientUtils.closeQuietly(response);
				}
			}
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException", e);
		} catch (IOException e) {
			logger.error("IOException", e);
		}
		return code;

	}

}