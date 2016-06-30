package com.mapbar.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;

public class TestHttpConn {

	private static final Logger logger = Logger.getLogger(TestHttpConn.class);

	public static String getWebPageUseProxy(String url, String ecoding) {
		StringBuffer sb = new StringBuffer();
		try {
			URL weburl = new URL(url);
			Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress("192.168.13.3", 8088));
			HttpURLConnection conn = (HttpURLConnection) weburl.openConnection(proxy);
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);

			InputStream in = conn.getInputStream();

			BufferedReader bin = new BufferedReader(new InputStreamReader(in, ecoding));
			String s = null;
			while ((s = bin.readLine()) != null) {
				sb.append(s);
				sb.append("\r\n");
			}
		} catch (Exception e) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static JSONArray getHttpRequestRestlt(String urlRequest) {
		JSONArray obj = null;
		int statues = -1;
		try {
			URL url = new URL(urlRequest);
			URLConnection connection = url.openConnection();
			// 请求协议(此处是http)生成的URLConnection类 的子类HttpURLConnection,故此处最好将其转化为HttpURLConnection类型的对象,以便用到 HttpURLConnection更多的API.如下:
			HttpURLConnection httpUrlConnection = (HttpURLConnection) connection;
			httpUrlConnection.setDoOutput(true);
			// 设置是否从httpUrlConnection读入，默认情况下是true;
			httpUrlConnection.setDoInput(true);
			// Post 请求不能使用缓存
			httpUrlConnection.setUseCaches(false);
			// 设定传送的内容类型是可序列化的java对象
			// (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
			// httpUrlConnection.setRequestProperty("Content-type","application/x-java-serialized-object");

			// 设定请求的方法为"POST"，默认是GET
			httpUrlConnection.setRequestMethod("GET");

			// 连接，从上述第2条中url.openConnection()至此的配置必须要在connect之前完成，
			httpUrlConnection.connect();
			// 此处getOutputStream会隐含的进行connect(即：如同调用上面的connect()方法，所以在开发中不调用上述的connect()也可以)。
			// OutputStream outStrm = httpUrlConnection.getOutputStream();
			// 现在通过输出流对象构建对象输出流对象，以实现输出可序列化的对象。
			// ObjectOutputStream objOutputStrm = new ObjectOutputStream(outStrm);

			// 向对象输出流写出数据，这些数据将存到内存缓冲区中
			// objOutputStrm.writeObject(new String("我是测试数据"));

			// 刷新对象输出流，将任何字节都写入潜在的流中（些处为ObjectOutputStream）
			// objOutputStrm.flush();

			// 关闭流对象。此时，不能再向对象输出流写入任何数据，先前写入的数据存在于内存缓冲区中,
			// 在调用下边的getInputStream()函数时才把准备好的http请求正式发送到服务器
			// objOutputStrm.close();

			// 调用HttpURLConnection连接对象的getInputStream()函数,
			// 将内存缓冲区中封装好的完整的HTTP请求电文发送到服务端。
			httpUrlConnection.getInputStream(); // <===注意，实际发送请求的代码段就在这里

			// 上边的httpConn.getInputStream()方法已调用,本次HTTP请求已结束,下边向对象输出流的输出已无意义，
			// 既使对象输出流没有调用close()方法，下边的操作也不会向对象输出流写入任何数据.
			// 因此，要重新发送数据时需要重新创建连接、重新设参数、重新创建流对象、重新写数据、
			// 重新发送数据(至于是否不用重新这些操作需要再研究)
			// objOutputStrm.writeObject(new String(""));

			BufferedReader reader = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream(), "utf-8"));
			String lines;
			StringBuffer info = new StringBuffer();
			while ((lines = reader.readLine()) != null) {
				logger.info(lines);
				info.append(lines);
			}
			reader.close();
			statues = httpUrlConnection.getResponseCode();// 获取返回状态
			if (statues == 200) {
				try {
					obj = JSONArray.parseArray(info.toString());
				} catch (JSONException e) {
					logger.info(("statues:" + statues + "\n   format json error :" + e.getMessage()));
				}
			}
		} catch (Exception e) {
			logger.info(("statues:" + statues + "\n" + e.getMessage()));
		}
		return obj;
	}

	public static String getHttpRequestRestltStr(String urlRequest) {
		int statues = -1;
		String lines;
		StringBuffer info = new StringBuffer();
		try {
			logger.info(urlRequest);
			URL url = new URL(urlRequest);
			URLConnection rulConnection = url.openConnection();// 此处的urlConnection对象实际上是根据URL的
			// 请求协议(此处是http)生成的URLConnection类 的子类HttpURLConnection,故此处最好将其转化
			// 为HttpURLConnection类型的对象,以便用到 HttpURLConnection更多的API.如下:

			HttpURLConnection httpUrlConnection = (HttpURLConnection) rulConnection;
			httpUrlConnection.setDoOutput(true);
			// 设置是否从httpUrlConnection读入，默认情况下是true;
			httpUrlConnection.setDoInput(true);

			// Post 请求不能使用缓存
			httpUrlConnection.setUseCaches(false);

			// 设定传送的内容类型是可序列化的java对象
			// (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
			// httpUrlConnection.setRequestProperty("Content-type","application/x-java-serialized-object");

			// 设定请求的方法为"POST"，默认是GET
			httpUrlConnection.setRequestMethod("GET");

			// 连接，从上述第2条中url.openConnection()至此的配置必须要在connect之前完成，
			httpUrlConnection.connect();
			// 此处getOutputStream会隐含的进行connect(即：如同调用上面的connect()方法，
			// 所以在开发中不调用上述的connect()也可以)。
			// OutputStream outStrm = httpUrlConnection.getOutputStream();
			// 现在通过输出流对象构建对象输出流对象，以实现输出可序列化的对象。
			// ObjectOutputStream objOutputStrm = new ObjectOutputStream(outStrm);

			// 向对象输出流写出数据，这些数据将存到内存缓冲区中
			// objOutputStrm.writeObject(new String("我是测试数据"));

			// 刷新对象输出流，将任何字节都写入潜在的流中（些处为ObjectOutputStream）
			// objOutputStrm.flush();

			// 关闭流对象。此时，不能再向对象输出流写入任何数据，先前写入的数据存在于内存缓冲区中,
			// 在调用下边的getInputStream()函数时才把准备好的http请求正式发送到服务器
			// objOutputStrm.close();

			// 调用HttpURLConnection连接对象的getInputStream()函数,
			// 将内存缓冲区中封装好的完整的HTTP请求电文发送到服务端。
			httpUrlConnection.getInputStream(); // <===注意，实际发送请求的代码段就在这里

			// 上边的httpConn.getInputStream()方法已调用,本次HTTP请求已结束,下边向对象输出流的输出已无意义，
			// 既使对象输出流没有调用close()方法，下边的操作也不会向对象输出流写入任何数据.
			// 因此，要重新发送数据时需要重新创建连接、重新设参数、重新创建流对象、重新写数据、
			// 重新发送数据(至于是否不用重新这些操作需要再研究)
			// objOutputStrm.writeObject(new String(""));

			BufferedReader reader = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream(), "utf-8"));

			while ((lines = reader.readLine()) != null) {
				info.append(lines);
			}
			reader.close();
		} catch (Exception e) {
			logger.info(("statues:" + statues + "\n" + e.getMessage()));
		}
		return info.toString();
	}

	public static String getPostResponseb(String url, String province, String city, String city_pinyin) throws HttpException, IOException {
		String html = "";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		// xj 测试 使用fidler抓包。
		HttpPost post = new HttpPost(url);
		post.setHeader(HttpHeaders.ACCEPT, "*/*");
		post.setHeader(HttpHeaders.CONNECTION, "Keep-Alive");
		post.setHeader(HttpHeaders.USER_AGENT, "WeiZhang_mapbarServicesQuery_getPostResponse");
		post.setHeader(HttpHeaders.ACCEPT_LANGUAGE, "zh,zh-TW;q=0.8,zh-CN;q=0.6");
		post.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");

		// 填入各个表单域的值
		if (!"null".equals(city_pinyin)) {
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			formparams.add(new BasicNameValuePair("province", province));
			formparams.add(new BasicNameValuePair("city", city));
			formparams.add(new BasicNameValuePair("city_pinyin", city_pinyin));
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
			// 将表单的值放入postMethod中
			post.setEntity(entity);
		}
		// 执行postMethod
		HttpResponse response = httpClient.execute(post);
		HttpEntity res = response.getEntity();
		int statusCode = response.getStatusLine().getStatusCode();
		// HttpClient对于要求接受后继服务的请求，象POST和PUT等不能自动处理转发
		// 301或者302
		if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
			// 从头中取出转向的地址
			Header locationHeader = post.getHeaders("location")[0];
			String location = null;
			if (locationHeader != null) {
				location = locationHeader.getValue();
				logger.info("The page was redirected to:" + location);
			} else {
				System.err.println("Location field value is null.");
			}
			return html;
		}
		if (statusCode == 200) {
			BufferedReader in = new BufferedReader(new InputStreamReader(res.getContent(), res.getContentEncoding().getValue()));
			StringBuffer sb = new StringBuffer();
			int chari;
			while ((chari = in.read()) != -1) {
				sb.append((char) chari);
			}
			html = sb.toString();
			in.close();
		} else {
			BufferedReader in = new BufferedReader(new InputStreamReader(res.getContent(), res.getContentEncoding().getValue()));
			StringBuffer sb = new StringBuffer();
			int chari;
			while ((chari = in.read()) != -1) {
				sb.append((char) chari);
			}
			in.close();
			html = "{\"status\":\"err\",\"msg\":\"查询失败，请稍后再试\"}";
		}
		httpClient.close();
		return html;
	}
}
