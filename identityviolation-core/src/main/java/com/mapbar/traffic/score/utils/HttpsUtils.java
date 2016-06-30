package com.mapbar.traffic.score.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.gif.GifDecoder;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class HttpsUtils {

	private static final Logger logger = Logger.getLogger(HttpsUtils.class);

	public static final String DEFAULT_USER_AGENT = "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; chromeframe/31.0.1650.57; .NET CLR 2.0.50727; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)";
	public static final String DEFAULT_ENCODE = System.getProperty("DEFAULT_ENCODE", "UTF-8");
	public static final int CONNECTION_TIMEOUT = Integer.parseInt(System.getProperty("CONNECTION_TIMEOUT", "60000"));
	public static final int READ_TIME_OUT = 15000;

	public static String postURLContents(String Url, String strReferer, String strData, String encode) throws Exception {
		StringBuffer res = new StringBuffer("");
		try {
			URL url = new URL(Url);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setRequestProperty("Referer", strReferer);
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; U; Android 2.1-update1; en-us; ADR6300 Build/ERE27) AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0 Mobile Safari/530.17");
			connection.setConnectTimeout(CONNECTION_TIMEOUT);
			connection.setReadTimeout(READ_TIME_OUT);
			PrintWriter pw = new PrintWriter(connection.getOutputStream());
			pw.print(strData);
			pw.close();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), encode));
			String line;
			while ((line = in.readLine()) != null) {
				res.append(line);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return res.toString();
	}

	public static String postURLContents(String Url, String strData) {
		StringBuffer res = new StringBuffer("");
		try {
			URL url = new URL(Url);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1) )");
			connection.setConnectTimeout(CONNECTION_TIMEOUT);
			connection.setReadTimeout(30000);
			PrintWriter pout = new PrintWriter(connection.getOutputStream());
			pout.print(strData);
			pout.close();
			if (connection.getResponseCode() == 200) {
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
				String line;
				while ((line = in.readLine()) != null) {
					res.append(line);
				}
				in.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res.toString();
	}

	public static String getURLContentsWithCookies(String strURL, Vector<String> vCookies, String strReferer, Proxy proxy) throws Exception {
		StringBuffer sb = new StringBuffer();
		try {
			URL url = new URL(strURL);
			URLConnection connection = HttpsProxyUtil.openConnection(url, proxy);

			HttpURLConnection httpConn = (HttpURLConnection) connection;
			if (!vCookies.isEmpty()) {
				StringBuffer sbCookies = new StringBuffer();
				for (int i = 0; i < vCookies.size(); i++) {
					if (i > 0) {
						sbCookies.append("; ");
					}
					sbCookies.append((String) vCookies.elementAt(i));
				}
				connection.setRequestProperty("Cookie", sbCookies.toString());
			}
			if (StringUtil.isNotEmpty(strReferer)) {
				connection.setRequestProperty("Referer", strReferer);
			}
			connection.setRequestProperty("User-Agent", DEFAULT_USER_AGENT);
			httpConn.setConnectTimeout(CONNECTION_TIMEOUT);
			httpConn.setReadTimeout(READ_TIME_OUT);

			HttpURLConnection.setFollowRedirects(true);
			Map<String, List<String>> hdrs = connection.getHeaderFields();
			if (hdrs != null) {
				List<String> setcookies = (List) hdrs.get("Set-Cookie");
				if (setcookies != null) {
					parseCookies(vCookies, setcookies);
				}
				setcookies = (List) hdrs.get("set-cookie");
				if (setcookies != null) {
					parseCookies(vCookies, setcookies);
				}
			}
			InputStreamReader isr = new InputStreamReader(httpConn.getInputStream(), DEFAULT_ENCODE);
			BufferedReader br = new BufferedReader(isr);
			String temp;
			while ((temp = br.readLine()) != null) {
				temp = temp.trim();
				if ((temp != null) && (temp.length() > 0)) {
					sb.append(temp);
				}
			}
			br.close();
			isr.close();
		} catch (Exception e) {
			logger.info("[URL]" + proxy + "::" + strURL);
			logger.info("Error " + e.getMessage());
			throw e;
		}
		return sb.toString();
	}

	public static String getURLContentsWithCookies(String strURL, Vector<String> vCookies, String strReferer, Proxy proxy, String encode) throws Exception {
		StringBuffer sb = new StringBuffer();
		try {
			URL url = new URL(strURL);
			URLConnection connection = HttpsProxyUtil.openConnection(url, proxy);

			HttpURLConnection httpConn = (HttpURLConnection) connection;
			if (!vCookies.isEmpty()) {
				StringBuffer sbCookies = new StringBuffer();
				for (int i = 0; i < vCookies.size(); i++) {
					if (i > 0) {
						sbCookies.append("; ");
					}
					sbCookies.append((String) vCookies.elementAt(i));
				}
				connection.setRequestProperty("Cookie", sbCookies.toString());
			}
			if (StringUtil.isNotEmpty(strReferer)) {
				connection.setRequestProperty("Referer", strReferer);
			}
			connection.setRequestProperty("User-Agent", DEFAULT_USER_AGENT);
			httpConn.setConnectTimeout(CONNECTION_TIMEOUT);
			httpConn.setReadTimeout(READ_TIME_OUT);
			// HttpURLConnection.setFollowRedirects(true);
			HttpURLConnection.setFollowRedirects(false);
			httpConn.setInstanceFollowRedirects(false);
			Map<String, List<String>> hdrs = connection.getHeaderFields();

			if ((httpConn.getResponseCode() == 301) || (httpConn.getResponseCode() == 302) || (httpConn.getResponseCode() == 307)) {

				if (hdrs != null) {
					String strLocation = httpConn.getHeaderField("Location");

					List setcookies = (List) hdrs.get("Set-Cookie");
					if (setcookies != null) {
						parseCookies(vCookies, setcookies);
					}

					setcookies = (List) hdrs.get("set-cookie");
					if (setcookies != null) {
						parseCookies(vCookies, setcookies);
					}

					setcookies = (List) hdrs.get("Cookie");
					if (setcookies != null) {
						parseCookies(vCookies, setcookies);
					}
					if (StringUtil.isNotEmpty(strLocation)) {
						if (strLocation.startsWith("/")) {
							strLocation = url.getProtocol() + "://" + url.getHost() + strLocation;
						}
						logger.info("strLocation==" + strLocation);
						return getURLContentsWithCookies(strLocation, vCookies, strReferer, proxy, encode);
					}
				}

			}

			if (hdrs != null) {
				List setcookies = (List) hdrs.get("Set-Cookie");
				if (setcookies != null) {
					parseCookies(vCookies, setcookies);
				}

				setcookies = (List) hdrs.get("set-cookie");
				if (setcookies != null) {
					parseCookies(vCookies, setcookies);
				}
				setcookies = (List) hdrs.get("Cookie");
				if (setcookies != null) {
					System.out.print(setcookies);
					parseCookies(vCookies, setcookies);
				}
			}

			InputStreamReader isr = new InputStreamReader(httpConn.getInputStream(), encode);
			BufferedReader br = new BufferedReader(isr);
			String temp;
			while ((temp = br.readLine()) != null) {
				temp = temp.trim();
				if ((temp != null) && (temp.length() > 0)) {
					sb.append(temp);
				}
			}
			br.close();
			isr.close();
		} catch (Exception e) {
			logger.info("[URL]" + proxy + "::" + strURL);
			logger.info("Error " + e.getMessage());
			throw e;
		}
		return sb.toString();
	}

	public static byte[] getUrlImageByteWithCookies(String Url, Vector<String> vCookies, String stringReferer, HttpsProxy proxy) throws Exception {
		byte bt[] = null;
		try {
			URL url = new URL(Url);
			URLConnection connection = HttpsProxyUtil.openConnection(url, proxy);

			HttpURLConnection httpConn = (HttpURLConnection) connection;
			if (!vCookies.isEmpty()) {
				StringBuffer sbCookies = new StringBuffer();
				for (int i = 0; i < vCookies.size(); i++) {
					if (i > 0)
						sbCookies.append("; ");
					sbCookies.append((String) vCookies.elementAt(i));
				}
				connection.setRequestProperty("Cookie", sbCookies.toString());
			}

			if (StringUtil.isNotEmpty(stringReferer)) {
				connection.setRequestProperty("Referer", stringReferer);
			}

			connection.setRequestProperty("User-Agent", DEFAULT_USER_AGENT);

			httpConn.setConnectTimeout(CONNECTION_TIMEOUT);
			// 超时时间 20150213
			httpConn.setReadTimeout(READ_TIME_OUT);
			HttpURLConnection.setFollowRedirects(true);

			if (httpConn.getResponseCode() != 200) {
				logger.info("[" + httpConn.getResponseCode() + "]" + httpConn.getResponseMessage());
			}

			Map hdrs = connection.getHeaderFields();
			if (hdrs != null) {
				List setcookies = (List) hdrs.get("Set-Cookie");
				if (setcookies != null) {
					parseCookies(vCookies, setcookies);
				}

				setcookies = (List) hdrs.get("set-cookie");
				if (setcookies != null) {
					parseCookies(vCookies, setcookies);
				}

			}

			InputStream is = connection.getInputStream();
			ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
			int i = 0;
			while ((i = is.read()) != -1) {
				out.write(i);
			}

			is.close();

			bt = out.toByteArray();

			out.close();
		} catch (Exception e) {
			logger.info("Error " + e.getMessage());
			throw e;
		}

		return bt;
	}

	public static boolean getUrlImageWithCookies(String Url, Vector<String> vCookies, String strRefUrl, Proxy proxy, String imagePath, String citypy) throws Exception {
		try {
			URL url = new URL(Url);
			URLConnection connection = HttpsProxyUtil.openConnection(url, proxy);

			HttpURLConnection httpConn = (HttpURLConnection) connection;
			if (!vCookies.isEmpty()) {
				StringBuffer sbCookies = new StringBuffer();
				for (int i = 0; i < vCookies.size(); i++) {
					if (i > 0)
						sbCookies.append("; ");
					sbCookies.append((String) vCookies.elementAt(i));
				}
				connection.setRequestProperty("Cookie", sbCookies.toString());
			}

			if (StringUtil.isNotEmpty(strRefUrl)) {
				connection.setRequestProperty("Referer", strRefUrl);
			}

			connection.setRequestProperty("User-Agent", DEFAULT_USER_AGENT);

			httpConn.setConnectTimeout(CONNECTION_TIMEOUT);
			// 超时时间 20150213
			httpConn.setReadTimeout(READ_TIME_OUT);
			HttpURLConnection.setFollowRedirects(true);

			if (httpConn.getResponseCode() != 200) {
				logger.info("[" + httpConn.getResponseCode() + "]" + httpConn.getResponseMessage());
			}

			Map hdrs = connection.getHeaderFields();
			if (hdrs != null) {
				List setcookies = (List) hdrs.get("Set-Cookie");
				if (setcookies != null) {
					parseCookies(vCookies, setcookies);
				}

				setcookies = (List) hdrs.get("set-cookie");
				if (setcookies != null) {
					parseCookies(vCookies, setcookies);
				}

			}

			InputStream is = connection.getInputStream();
			File file = new File(imagePath);

			FileOutputStream out = new FileOutputStream(file);
			int i = 0;
			while ((i = is.read()) != -1) {
				out.write(i);
			}
			out.close();
			is.close();
		} catch (Exception e) {
			logger.info("Error " + e.getMessage());
			throw e;
		}

		return true;

	}

	public static boolean getUrlImageGIFWithCookies(String Url, Vector<String> vCookies, String strRefUrl, Proxy proxy, String imagePath, String citypy) {
		{
			try {
				URL url = new URL(Url);
				URLConnection connection = HttpsProxyUtil.openConnection(url, proxy);

				HttpURLConnection httpConn = (HttpURLConnection) connection;
				if (!vCookies.isEmpty()) {
					StringBuffer sbCookies = new StringBuffer();
					for (int i = 0; i < vCookies.size(); i++) {
						if (i > 0)
							sbCookies.append("; ");
						sbCookies.append((String) vCookies.elementAt(i));
					}
					connection.setRequestProperty("Cookie", sbCookies.toString());
				}

				if (StringUtil.isNotEmpty(strRefUrl)) {
					connection.setRequestProperty("Referer", strRefUrl);
				}

				connection.setRequestProperty("User-Agent", DEFAULT_USER_AGENT);

				httpConn.setConnectTimeout(CONNECTION_TIMEOUT);
				// 超时时间 20150213
				httpConn.setReadTimeout(READ_TIME_OUT);
				HttpURLConnection.setFollowRedirects(true);

				if (httpConn.getResponseCode() != 200) {
					logger.info("[" + httpConn.getResponseCode() + "]" + httpConn.getResponseMessage());
				}

				Map hdrs = connection.getHeaderFields();
				if (hdrs != null) {
					List setcookies = (List) hdrs.get("Set-Cookie");
					if (setcookies != null) {
						parseCookies(vCookies, setcookies);
					}

					setcookies = (List) hdrs.get("set-cookie");
					if (setcookies != null) {
						parseCookies(vCookies, setcookies);
					}

				}

				InputStream is = connection.getInputStream();
				GifDecoder gif = new GifDecoder();
				gif.read(is);
				BufferedImage image = gif.getFrame(0);
				ImageIO.write(image, "gif", new File(imagePath));

				is.close();
			} catch (Exception e) {
				logger.info("Error " + e.getMessage());
				return false;
			}

			return true;
		}
	}

	public static String postURLContentsWithCookies(String Url, String strData, Vector<String> vCookies, String strReferer, Proxy proxy) {
		StringBuffer res = new StringBuffer("");
		try {
			URL url = new URL(Url);

			HttpURLConnection connection = (HttpURLConnection) HttpsProxyUtil.openConnection(url, proxy);
			HttpURLConnection httpConn = connection;

			connection.setRequestMethod("POST");
			connection.setDoOutput(true);

			connection.setRequestProperty("Accept", "application/x-ms-application, image/jpeg, application/xaml+xml, image/gif, image/pjpeg, application/x-ms-xbap, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			if (StringUtil.isNotEmpty(strReferer)) {
				connection.setRequestProperty("Referer", strReferer);
			}

			connection.setRequestProperty("Accept-Language", "zh-CN");

			connection.setRequestProperty("User-Agent", DEFAULT_USER_AGENT);

			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			connection.setRequestProperty("Host", url.getHost());

			connection.setRequestProperty("Content-Length", String.valueOf(strData.length()));
			connection.setRequestProperty("Connection", "Keep-Alive");

			connection.setRequestProperty("Cache-Control", "no-cache");

			if (!vCookies.isEmpty()) {
				StringBuffer sbCookies = new StringBuffer();
				for (int i = 0; i < vCookies.size(); i++) {
					if (i > 0)
						sbCookies.append("; ");
					sbCookies.append((String) vCookies.elementAt(i));
				}
				connection.setRequestProperty("Cookie", sbCookies.toString());
			}

			connection.setConnectTimeout(CONNECTION_TIMEOUT);
			httpConn.setReadTimeout(READ_TIME_OUT);
			HttpURLConnection.setFollowRedirects(false);
			httpConn.setInstanceFollowRedirects(false);

			PrintWriter pout = new PrintWriter(connection.getOutputStream());

			pout.print(strData);
			pout.flush();
			System.out.print("httpConn.getResponseCode()===========" + httpConn.getResponseCode());
			if ((httpConn.getResponseCode() == 301) || (httpConn.getResponseCode() == 302) || (httpConn.getResponseCode() == 307)) {
				Map hdrs = connection.getHeaderFields();
				System.out.print("hdrs===========" + hdrs);
				if (hdrs != null) {
					String strLocation = httpConn.getHeaderField("Location");

					List setcookies = (List) hdrs.get("Set-Cookie");
					if (setcookies != null) {
						System.out.print(setcookies);
						parseCookies(vCookies, setcookies);
					}

					setcookies = (List) hdrs.get("set-cookie");
					if (setcookies != null) {
						System.out.print(setcookies);
						parseCookies(vCookies, setcookies);
					}

					setcookies = (List) hdrs.get("Cookie");
					if (setcookies != null) {
						System.out.print(setcookies);
						parseCookies(vCookies, setcookies);
					}
					if (StringUtil.isNotEmpty(strLocation)) {
						if (strLocation.startsWith("/")) {
							strLocation = url.getProtocol() + "://" + url.getHost() + strLocation;
						}

						return postURLContentsWithCookies(strLocation, strData, vCookies, strReferer);
					}
				}

			}

			Map hdrs = connection.getHeaderFields();
			if (hdrs != null) {
				List setcookies = (List) hdrs.get("Set-Cookie");
				if (setcookies != null) {
					parseCookies(vCookies, setcookies);
				}

				setcookies = (List) hdrs.get("set-cookie");
				if (setcookies != null) {
					parseCookies(vCookies, setcookies);
				}
				setcookies = (List) hdrs.get("Cookie");
				if (setcookies != null) {
					System.out.print(setcookies);
					parseCookies(vCookies, setcookies);
				}
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				res.append(line);
			}
			in.close();

			pout.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return res.toString();
	}

	public static String postURLContentsWithCookies(String Url, String strData, Vector<String> vCookies, String strReferer) {
		return postURLContentsWithCookies(Url, strData, vCookies, strReferer, null);
	}

	public static String postURLContentsToCheckViolation(String Url, String strData, String strReferer, String strCharset, Proxy proxy) throws Exception {
		StringBuffer res = new StringBuffer("");
		try {
			URL url = new URL(Url);
			HttpURLConnection connection = (HttpURLConnection) HttpsProxyUtil.openConnection(url, proxy);
			HttpURLConnection httpConn = connection;

			connection.setRequestMethod("POST");
			connection.setDoOutput(true);

			connection.setRequestProperty("Accept", "application/x-ms-application, image/jpeg, application/xaml+xml, image/gif, image/pjpeg, application/x-ms-xbap, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			if (StringUtil.isNotEmpty(strReferer)) {
				connection.setRequestProperty("Referer", strReferer);
			}
			connection.setRequestProperty("Accept-Language", "zh-CN");

			connection.setRequestProperty("User-Agent", DEFAULT_USER_AGENT);

			connection.setRequestProperty("Content-Type", "text/plain");

			connection.setRequestProperty("Host", url.getHost());

			connection.setRequestProperty("Content-Length", strData.length() + "");

			connection.setRequestProperty("Connection", "Keep-Alive");

			connection.setRequestProperty("Cache-Control", "no-cache");

			connection.setConnectTimeout(CONNECTION_TIMEOUT);

			// 超时时间 20150213
			httpConn.setReadTimeout(30000);
			HttpURLConnection.setFollowRedirects(false);
			httpConn.setInstanceFollowRedirects(false);

			PrintWriter pout = new PrintWriter(connection.getOutputStream());

			pout.print(strData);
			pout.flush();

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), strCharset));
			String line;
			while ((line = in.readLine()) != null) {

				res.append(line);
			}
			in.close();

			pout.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return res.toString();
	}

	public static String postURLContentsToCheckDriver(String Url, String strData, String strReferer, String strCharset, Proxy proxy) throws Exception {
		StringBuffer res = new StringBuffer("");
		try {
			URL url = new URL(Url);
			HttpURLConnection connection = (HttpURLConnection) HttpsProxyUtil.openConnection(url, proxy);
			HttpURLConnection httpConn = connection;

			connection.setRequestMethod("POST");
			connection.setDoOutput(true);

			connection.setRequestProperty("Accept", "application/x-ms-application, image/jpeg, application/xaml+xml, image/gif, image/pjpeg, application/x-ms-xbap, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			if (StringUtil.isNotEmpty(strReferer)) {
				connection.setRequestProperty("Referer", strReferer);
			}
			connection.setRequestProperty("Accept-Language", "zh-CN");

			connection.setRequestProperty("User-Agent", DEFAULT_USER_AGENT);

			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			connection.setRequestProperty("Host", url.getHost());

			connection.setRequestProperty("Content-Length", strData.length() + "");

			connection.setRequestProperty("Connection", "Keep-Alive");

			connection.setRequestProperty("Cache-Control", "no-cache");

			connection.setConnectTimeout(CONNECTION_TIMEOUT);

			// 超时时间 20150213
			httpConn.setReadTimeout(30000);
			HttpURLConnection.setFollowRedirects(false);
			httpConn.setInstanceFollowRedirects(false);

			PrintWriter pout = new PrintWriter(connection.getOutputStream());

			pout.print(strData);
			pout.flush();

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), strCharset));
			String line;
			while ((line = in.readLine()) != null) {

				res.append(line);
			}
			in.close();

			pout.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return res.toString();
	}

	public static String postURLContentsWithCookies(String Url, String strData, List<String> vCookies, String strReferer, String strCharset, Proxy proxy) throws Exception {
		StringBuffer res = new StringBuffer("");
		try {
			URL url = new URL(Url);
			HttpURLConnection connection = (HttpURLConnection) HttpsProxyUtil.openConnection(url, proxy);
			HttpURLConnection httpConn = connection;

			connection.setRequestMethod("POST");
			connection.setDoOutput(true);

			connection.setRequestProperty("Accept", "application/x-ms-application, image/jpeg, application/xaml+xml, image/gif, image/pjpeg, application/x-ms-xbap, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			if (StringUtil.isNotEmpty(strReferer)) {
				connection.setRequestProperty("Referer", strReferer);
			}
			connection.setRequestProperty("Accept-Language", "zh-CN");

			connection.setRequestProperty("User-Agent", DEFAULT_USER_AGENT);

			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			connection.setRequestProperty("Host", url.getHost());

			connection.setRequestProperty("Content-Length", strData.length() + "");

			connection.setRequestProperty("Connection", "Keep-Alive");

			connection.setRequestProperty("Cache-Control", "no-cache");
			if (!vCookies.isEmpty()) {
				StringBuffer sbCookies = new StringBuffer();
				for (int i = 0; i < vCookies.size(); i++) {
					if (i > 0) {
						sbCookies.append("; ");
					}
					sbCookies.append((String) vCookies.get(i));
				}
				connection.setRequestProperty("Cookie", sbCookies.toString());
			}
			connection.setConnectTimeout(CONNECTION_TIMEOUT);

			httpConn.setReadTimeout(45000);
			HttpURLConnection.setFollowRedirects(false);
			httpConn.setInstanceFollowRedirects(false);

			PrintWriter pout = new PrintWriter(connection.getOutputStream());

			pout.print(strData);
			pout.flush();
			if ((httpConn.getResponseCode() == 301) || (httpConn.getResponseCode() == 302) || (httpConn.getResponseCode() == 307)) {
				Map<String, List<String>> hdrs = connection.getHeaderFields();
				if (hdrs != null) {
					String strLocation = httpConn.getHeaderField("Location");

					List<String> setcookies = (List) hdrs.get("Set-Cookie");

					if (setcookies != null) {
						parseCookies(vCookies, setcookies);
					}
					setcookies = (List) hdrs.get("set-cookie");
					if (setcookies != null) {
						parseCookies(vCookies, setcookies);
					}
					if (StringUtil.isNotEmpty(strLocation)) {
						if (strLocation.startsWith("/")) {
							int port = url.getPort();
							if (port != -1) {
								strLocation = url.getProtocol() + "://" + url.getHost() + ":" + port + strLocation;
							} else {
								strLocation = url.getProtocol() + "://" + url.getHost() + strLocation;
							}

						}
						logger.info("strLocation=====" + strLocation);
						if (strLocation.equals("weizhang.asp")) {
							return "查询验证码输入错误，请重新输入!";
						} else {
							return postURLContentsWithCookies(strLocation, strData, vCookies, strReferer, strCharset, proxy);
						}

					}
				}
			}
			Map<String, List<String>> hdrs = connection.getHeaderFields();
			if (hdrs != null) {
				List<String> setcookies = (List) hdrs.get("Set-Cookie");
				if (setcookies != null) {
					parseCookies(vCookies, setcookies);
				}
				setcookies = (List) hdrs.get("set-cookie");
				if (setcookies != null) {
					parseCookies(vCookies, setcookies);
				}
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), strCharset));
			String line;
			while ((line = in.readLine()) != null) {

				res.append(line);
			}
			in.close();
			pout.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return res.toString();
	}

	public static String postURLContentsWithCookiesForAjax(String Url, String strData, Vector<String> vCookies, String strReferer, String strCharset, Proxy proxy) throws Exception {
		StringBuffer res = new StringBuffer("");
		try {
			URL url = new URL(Url);
			HttpURLConnection connection = (HttpURLConnection) HttpsProxyUtil.openConnection(url, proxy);
			HttpURLConnection httpConn = connection;

			connection.setRequestMethod("POST");
			connection.setDoOutput(true);

			connection.setRequestProperty("Accept", "application/json, text/javascript, */*");
			if (StringUtil.isNotEmpty(strReferer)) {
				connection.setRequestProperty("Referer", strReferer);
			}
			connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");

			// Accept-Encoding:gzip, deflate
			connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
			connection.setRequestProperty("User-Agent", DEFAULT_USER_AGENT);
			// connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
			connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

			connection.setRequestProperty("Host", url.getHost());

			connection.setRequestProperty("Content-Length", strData.length() + "");

			connection.setRequestProperty("Connection", "Keep-Alive");

			connection.setRequestProperty("Cache-Control", "no-cache");
			if (!vCookies.isEmpty()) {
				StringBuffer sbCookies = new StringBuffer();
				for (int i = 0; i < vCookies.size(); i++) {
					if (i > 0) {
						sbCookies.append("; ");
					}
					sbCookies.append((String) vCookies.elementAt(i));
				}
				connection.setRequestProperty("Cookie", sbCookies.toString());
			}
			connection.setConnectTimeout(CONNECTION_TIMEOUT);

			httpConn.setReadTimeout(45000);
			HttpURLConnection.setFollowRedirects(false);
			httpConn.setInstanceFollowRedirects(false);

			PrintWriter pout = new PrintWriter(connection.getOutputStream());

			pout.print(strData);
			pout.flush();
			if ((httpConn.getResponseCode() == 301) || (httpConn.getResponseCode() == 302) || (httpConn.getResponseCode() == 307)) {
				Map<String, List<String>> hdrs = connection.getHeaderFields();
				if (hdrs != null) {
					String strLocation = httpConn.getHeaderField("Location");

					List<String> setcookies = (List) hdrs.get("Set-Cookie");

					if (setcookies != null) {
						parseCookies(vCookies, setcookies);
					}
					setcookies = (List) hdrs.get("set-cookie");
					if (setcookies != null) {
						parseCookies(vCookies, setcookies);
					}
					if (StringUtil.isNotEmpty(strLocation)) {
						if (strLocation.startsWith("/")) {
							int port = url.getPort();
							if (port != -1) {
								strLocation = url.getProtocol() + "://" + url.getHost() + ":" + port + strLocation;
							} else {
								strLocation = url.getProtocol() + "://" + url.getHost() + strLocation;
							}

						}
						logger.info("strLocation=====" + strLocation);
						if (strLocation.equals("weizhang.asp")) {
							return "查询验证码输入错误，请重新输入!";
						} else {
							return postURLContentsWithCookies(strLocation, strData, vCookies, strReferer, strCharset, proxy);
						}

					}
				}
			}
			Map<String, List<String>> hdrs = connection.getHeaderFields();
			if (hdrs != null) {
				List<String> setcookies = (List) hdrs.get("Set-Cookie");
				if (setcookies != null) {
					parseCookies(vCookies, setcookies);
				}
				setcookies = (List) hdrs.get("set-cookie");
				if (setcookies != null) {
					parseCookies(vCookies, setcookies);
				}
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), strCharset));
			String line;
			while ((line = in.readLine()) != null) {

				res.append(line);
			}
			in.close();

			pout.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return res.toString();
	}

	public static String postURLContentsWithCookiesForXinjiang(String Url, String strData, Vector<String> vCookies, String strReferer, String strCharset, Proxy proxy) {
		StringBuffer res = new StringBuffer("");
		try {
			URL url = new URL(Url);
			HttpURLConnection connection = (HttpURLConnection) HttpsProxyUtil.openConnection(url, proxy);
			HttpURLConnection httpConn = connection;

			connection.setRequestMethod("POST");
			connection.setDoOutput(true);

			connection.setRequestProperty("Accept", "application/x-ms-application, image/jpeg, application/xaml+xml, image/gif, image/pjpeg, application/x-ms-xbap, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			if (StringUtil.isNotEmpty(strReferer)) {
				connection.setRequestProperty("Referer", strReferer);
			}
			connection.setRequestProperty("Accept-Language", "zh-CN");

			connection.setRequestProperty("User-Agent", DEFAULT_USER_AGENT);

			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			connection.setRequestProperty("Host", url.getHost());

			connection.setRequestProperty("Content-Length", strData.length() + "");

			connection.setRequestProperty("Connection", "Keep-Alive");

			connection.setRequestProperty("Cache-Control", "no-cache");
			if (!vCookies.isEmpty()) {
				StringBuffer sbCookies = new StringBuffer();
				for (int i = 0; i < vCookies.size(); i++) {
					if (i > 0) {
						sbCookies.append("; ");
					}
					sbCookies.append((String) vCookies.elementAt(i));
				}
				connection.setRequestProperty("Cookie", sbCookies.toString());
			}
			connection.setConnectTimeout(CONNECTION_TIMEOUT);

			httpConn.setReadTimeout(15000);
			HttpURLConnection.setFollowRedirects(false);
			httpConn.setInstanceFollowRedirects(false);

			PrintWriter pout = new PrintWriter(connection.getOutputStream());

			pout.print(strData);
			pout.flush();
			if ((httpConn.getResponseCode() == 301) || (httpConn.getResponseCode() == 302) || (httpConn.getResponseCode() == 307)) {
				Map<String, List<String>> hdrs = connection.getHeaderFields();
				if (hdrs != null) {
					String strLocation = httpConn.getHeaderField("Location");

					List<String> setcookies = (List) hdrs.get("Set-Cookie");
					if (setcookies != null) {
						parseCookies(vCookies, setcookies);
					}
					setcookies = (List) hdrs.get("set-cookie");
					if (setcookies != null) {
						parseCookies(vCookies, setcookies);
					}
					if (StringUtil.isNotEmpty(strLocation)) {
						if (strLocation.startsWith("/")) {
							int port = url.getPort();
							strLocation = url.getProtocol() + "://" + url.getHost() + ":" + port + strLocation;
						}
						return getURLContentsWithCookies(strLocation, vCookies, strReferer, proxy, strCharset);
					}
				}
			}
			Map<String, List<String>> hdrs = connection.getHeaderFields();
			if (hdrs != null) {
				List<String> setcookies = (List) hdrs.get("Set-Cookie");
				if (setcookies != null) {
					parseCookies(vCookies, setcookies);
				}
				setcookies = (List) hdrs.get("set-cookie");
				if (setcookies != null) {
					parseCookies(vCookies, setcookies);
				}
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), strCharset));
			String line;
			while ((line = in.readLine()) != null) {

				res.append(line);
			}
			in.close();

			pout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res.toString();
	}

	private static void parseCookies(List<String> vCookies, List<String> setcookies) {
		try {
			if (!vCookies.isEmpty()) {
				int f = 0;
				for (String s : vCookies) {
					if (s.contains("com.trs.idm.coSessionId")) {
						f = 1;
					}
				}
				if (f == 0) {
					vCookies.clear();
				}

			}
			logger.info("setcookies===============" + setcookies);
			for (int i = 0; i < setcookies.size(); i++) {
				String strCookie = (String) setcookies.get(i);
				String[] strItems = strCookie.split(";");
				for (int k = 0; k < strItems.length; k++) {
					strCookie = strItems[k].trim();
					if ((!strCookie.startsWith("Path=")) && (!strCookie.startsWith("path=")) && (!strCookie.startsWith("domain=")) && (!strCookie.startsWith("expires=")) && (strCookie.indexOf("deleted") <= 0) && (!strCookie.equalsIgnoreCase("httponly")) && (!vCookies.contains(strCookie)))
						if (!vCookies.contains(strCookie)) {
							vCookies.add(strCookie);
						}
				}
			}
		} catch (Exception exx) {
			exx.printStackTrace();
		}
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
		HttpURLConnection conn = null;
		try {
			token = token.replace(" ", "+");
			URL url = new URL(tokenUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("user_token", token);
			conn.setRequestProperty("Connection", "close");
			conn.connect();
			int stateCode = conn.getResponseCode();
			if (stateCode == 200) {
				StringBuffer sb = new StringBuffer("");
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				while (reader.ready()) {
					sb.append(reader.readLine() + "\r\n");
				}
				reader.close();
				JSONObject obj;
				try {
					obj = JSONObject.parseObject(sb.toString());
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
			} else {
				return stateCode;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return 500;
		} finally {
			conn.disconnect();
		}
	}

}
