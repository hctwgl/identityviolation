package com.mapbar.httpcore.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Future;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.Consts;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.pool.BasicConnFactory;
import org.apache.http.impl.pool.BasicConnPool;
import org.apache.http.impl.pool.BasicPoolEntry;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.junit.Test;

public class TestRequest {

	private static final Logger logger = Logger.getLogger(TestRequest.class);

	/**
	 * Elemental example for executing multiple GET requests sequentially.
	 */
	@Test
	public void testGet() throws UnknownHostException, IOException, HttpException {
		HttpProcessor httpproc = HttpProcessorBuilder.create().add(new RequestContent()).add(new RequestTargetHost()).add(new RequestConnControl()).add(new RequestUserAgent("Test/1.1")).add(new RequestExpectContinue(true)).build();

		HttpRequestExecutor httpexecutor = new HttpRequestExecutor();

		HttpCoreContext coreContext = HttpCoreContext.create();
		HttpHost host = new HttpHost("localhost", 8080);
		coreContext.setTargetHost(host);

		DefaultBHttpClientConnection conn = new DefaultBHttpClientConnection(8 * 1024);
		ConnectionReuseStrategy connStrategy = DefaultConnectionReuseStrategy.INSTANCE;

		try {

			String[] targets = { "/", "/servlets-examples/servlet/RequestInfoExample", "/somewhere%20in%20pampa" };

			for (int i = 0; i < targets.length; i++) {
				if (!conn.isOpen()) {
					Socket socket = new Socket(host.getHostName(), host.getPort());
					conn.bind(socket);
				}
				BasicHttpRequest request = new BasicHttpRequest("GET", targets[i]);
				logger.info(">> Request URI: " + request.getRequestLine().getUri());

				httpexecutor.preProcess(request, httpproc, coreContext);
				HttpResponse response = httpexecutor.execute(request, conn, coreContext);
				httpexecutor.postProcess(response, httpproc, coreContext);

				logger.info("<< Response: " + response.getStatusLine());
				logger.info(EntityUtils.toString(response.getEntity()));
				logger.info("==============");
				if (!connStrategy.keepAlive(response, coreContext)) {
					conn.close();
				} else {
					logger.info("Connection kept alive...");
				}
			}
		} finally {
			conn.close();
		}
	}

	/**
	 * Elemental example for executing multiple POST requests sequentially.
	 */
	@Test
	public void testPost() throws Exception {
		HttpProcessor httpproc = HttpProcessorBuilder.create().add(new RequestContent()).add(new RequestTargetHost()).add(new RequestConnControl()).add(new RequestUserAgent("Test/1.1")).add(new RequestExpectContinue(true)).build();

		HttpRequestExecutor httpexecutor = new HttpRequestExecutor();

		HttpCoreContext coreContext = HttpCoreContext.create();
		HttpHost host = new HttpHost("localhost", 8080);
		coreContext.setTargetHost(host);

		DefaultBHttpClientConnection conn = new DefaultBHttpClientConnection(8 * 1024);
		ConnectionReuseStrategy connStrategy = DefaultConnectionReuseStrategy.INSTANCE;

		try {

			HttpEntity[] requestBodies = { new StringEntity("This is the first test request", ContentType.create("text/plain", Consts.UTF_8)), new ByteArrayEntity("This is the second test request".getBytes(Consts.UTF_8), ContentType.APPLICATION_OCTET_STREAM), new InputStreamEntity(new ByteArrayInputStream("This is the third test request (will be chunked)".getBytes(Consts.UTF_8)), ContentType.APPLICATION_OCTET_STREAM) };

			for (int i = 0; i < requestBodies.length; i++) {
				if (!conn.isOpen()) {
					Socket socket = new Socket(host.getHostName(), host.getPort());
					conn.bind(socket);
				}
				BasicHttpEntityEnclosingRequest request = new BasicHttpEntityEnclosingRequest("POST", "/servlets-examples/servlet/RequestInfoExample");
				request.setEntity(requestBodies[i]);
				logger.info(">> Request URI: " + request.getRequestLine().getUri());

				httpexecutor.preProcess(request, httpproc, coreContext);
				HttpResponse response = httpexecutor.execute(request, conn, coreContext);
				httpexecutor.postProcess(response, httpproc, coreContext);

				logger.info("<< Response: " + response.getStatusLine());
				logger.info(EntityUtils.toString(response.getEntity()));
				logger.info("==============");
				if (!connStrategy.keepAlive(response, coreContext)) {
					conn.close();
				} else {
					logger.info("Connection kept alive...");
				}
			}
		} finally {
			conn.close();
		}
	}

	/**
	 * Elemental example for executing multiple GET requests from different threads using a connection pool.
	 */
	@Test
	public void testPoolingHttpGet() throws Exception {
		final HttpProcessor httpproc = HttpProcessorBuilder.create().add(new RequestContent()).add(new RequestTargetHost()).add(new RequestConnControl()).add(new RequestUserAgent("Test/1.1")).add(new RequestExpectContinue(true)).build();

		final HttpRequestExecutor httpexecutor = new HttpRequestExecutor();

		final BasicConnPool pool = new BasicConnPool(new BasicConnFactory());
		pool.setDefaultMaxPerRoute(2);
		pool.setMaxTotal(2);

		HttpHost[] targets = { new HttpHost("www.google.com", 80), new HttpHost("www.yahoo.com", 80), new HttpHost("www.apache.com", 80) };

		class WorkerThread extends Thread {

			private final HttpHost target;

			WorkerThread(final HttpHost target) {
				super();
				this.target = target;
			}

			@Override
			public void run() {
				ConnectionReuseStrategy connStrategy = DefaultConnectionReuseStrategy.INSTANCE;
				try {
					Future<BasicPoolEntry> future = pool.lease(this.target, null);

					boolean reusable = false;
					BasicPoolEntry entry = future.get();
					try {
						HttpClientConnection conn = entry.getConnection();
						HttpCoreContext coreContext = HttpCoreContext.create();
						coreContext.setTargetHost(this.target);

						BasicHttpRequest request = new BasicHttpRequest("GET", "/");
						logger.info(">> Request URI: " + request.getRequestLine().getUri());

						httpexecutor.preProcess(request, httpproc, coreContext);
						HttpResponse response = httpexecutor.execute(request, conn, coreContext);
						httpexecutor.postProcess(response, httpproc, coreContext);

						logger.info("<< Response: " + response.getStatusLine());
						logger.info(EntityUtils.toString(response.getEntity()));

						reusable = connStrategy.keepAlive(response, coreContext);
					} catch (IOException ex) {
						throw ex;
					} catch (HttpException ex) {
						throw ex;
					} finally {
						if (reusable) {
							logger.info("Connection kept alive...");
						}
						pool.release(entry, reusable);
					}
				} catch (Exception ex) {
					logger.info("Request to " + this.target + " failed: " + ex.getMessage());
				}
			}

		}
		;

		WorkerThread[] workers = new WorkerThread[targets.length];
		for (int i = 0; i < workers.length; i++) {
			workers[i] = new WorkerThread(targets[i]);
		}
		for (int i = 0; i < workers.length; i++) {
			workers[i].start();
		}
		for (int i = 0; i < workers.length; i++) {
			workers[i].join();
		}
	}
}
