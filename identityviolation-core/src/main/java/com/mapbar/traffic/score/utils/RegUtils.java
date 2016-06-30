package com.mapbar.traffic.score.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class RegUtils {

	private static final Logger logger = Logger.getLogger(RegUtils.class);
	public static String DEFAULT_ENCODE = System.getProperty("DEFAULT_ENCODE", "UTF-8");
	public static String DISABLED_TAG = "_interal_disabled";

	public static Vector<String> readFile(String strFile, int iStart, int iEnd) {
		Vector<String> vRecords = new Vector<String>();
		try {
			BufferedReader br = null;
			if (strFile.endsWith(".gz")) {
				br = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(strFile)), DEFAULT_ENCODE));
			} else {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(strFile), DEFAULT_ENCODE));
			}

			int i = 0;
			for (String strLine = null; ((strLine = br.readLine()) != null) && (i < iEnd); ++i) {
				if ((!(StringUtil.isNotEmpty(strLine))) || (i < iStart))
					continue;
				strLine = strLine.trim();

				if ((!(StringUtil.isNotEmpty(strLine))) || (strLine.startsWith("#")))
					continue;
				vRecords.addElement(strLine);
			}
			br.close();
			logger.info("[" + strFile + "]Loaded." + vRecords.size());
		} catch (Exception e) {
			logger.info("[readFile.Error]" + e.getMessage());
		}
		return vRecords;
	}

	public static Vector<String> readFile(String strFile) {
		return readFile(strFile, 0, 2147483647);
	}

	public static int getNumberInRange(int min, int max) {
		int iRet = (min + max) / 2;
		try {
			if (min == max) {
				iRet = min;
			}
			iRet = Math.max(min, (int) Math.round(Math.random() * (max - min) + min));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iRet;
	}

	public static String loadHtml(String strFile) {
		StringBuffer sb = new StringBuffer();
		try {
			System.setProperty("file.encoding", "UTF-8");
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(strFile), "utf-8"));
			for (String strLine = null; (strLine = br.readLine()) != null;) {
				if (!(StringUtil.isNotEmpty(strLine)))
					continue;
				sb.append(strLine);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static String loadText(String strFile) {
		return loadText(strFile, "UTF-8");
	}

	public static String loadText(String strFile, String strCodec) {
		StringBuffer sb = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(strFile), strCodec));
			for (String strLine = null; (strLine = br.readLine()) != null;) {
				if (!(StringUtil.isNotEmpty(strLine)))
					continue;
				sb.append(strLine + "\n");
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	public static String loadPlainText(String strFile) {
		StringBuffer sb = new StringBuffer();
		try {
			System.setProperty("file.encoding", "UTF-8");
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(strFile), "UTF-8"));
			for (String strLine = null; (strLine = br.readLine()) != null;) {
				sb.append(strLine + "\n");
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static Element parseForm(String strHtml, String strId) {
		return parseForm(strHtml, strId, 0);
	}

	public static Element parseForm(String strHtml, String strId, int idx) {
		Element form = null;
		try {
			Document doc = Jsoup.parse(strHtml);
			if ((StringUtil.isNotEmpty(strId)) && (idx == 0)) {
				form = doc.getElementById(strId);
			}

			Elements forms = doc.getElementsByTag("form");
			if (StringUtil.isNotEmpty(strId)) {
				forms = doc.getElementsByAttributeValue("id", strId);
			}
			if ((forms != null) && (forms.size() > idx))
				form = forms.get(idx);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return form;
	}

	public static String parseFormData(Element form, Hashtable<String, String> htKVPs, String encode) {
		return parseFormData(form, htKVPs, encode, false);
	}

	public static String parseFormData(Element form, Hashtable<String, String> htKVPs, String encode, boolean ignoreEmptyValue) {
		StringBuffer sb = new StringBuffer();
		try {
			Elements inputs = form.getElementsByTag("input");

			Hashtable<String, String> htKeys = new Hashtable<String, String>();
			for (int i = 0; i < inputs.size(); ++i) {
				Element e = inputs.get(i);
				String strName = e.attr("name");
				String strValue = e.val();
				String strId = e.id();

				if ((htKVPs != null) && (htKVPs.containsKey(strName))) {
					strValue = (String) htKVPs.get(strName);
					if (!(DISABLED_TAG.equals(strValue)))
						;
				} else if ((htKVPs != null) && (StringUtil.isNotEmpty(strId)) && (htKVPs.contains(strId))) {
					strValue = (String) htKVPs.get(strId);
					if (!(DISABLED_TAG.equals(strValue)))
						;
				} else {
					if (StringUtil.isNotEmpty(strValue)) {
						strValue = URLEncoder.encode(strValue, encode);
					}
					if ((!(StringUtil.isNotEmpty(strName))) || (htKeys.containsKey(strName)) || ((ignoreEmptyValue) && (!(StringUtil.isNotEmpty(strValue)))) || ("hiddenBottomBackButtonId".equalsIgnoreCase(strId)) || ("hiddenFirstButtonId".equalsIgnoreCase(strId)))
						continue;
					htKeys.put(strName, strValue);
					if (sb.length() > 0)
						sb.append("&");
					sb.append(strName + "=" + strValue);
				}
			}

			Elements selects = form.getElementsByTag("select");
			for (int i = 0; i < selects.size(); ++i) {
				Element e = selects.get(i);
				String strName = e.attr("name");
				String strValue = null;

				if ((htKVPs != null) && (htKVPs.containsKey(strName))) {
					strValue = (String) htKVPs.get(strName);
				} else {
					Elements options = e.getElementsByTag("option");
					int idx = 0;
					for (int k = 0; k < options.size(); ++k) {
						Element opt = options.get(k);
						if ((k == idx) && (!(StringUtil.isNotEmpty(strValue))))
							strValue = opt.val();
						if (!("selected".equalsIgnoreCase(opt.attr("selected"))))
							continue;
						strValue = opt.val();
						break;
					}
				}
				if (sb.length() > 0)
					sb.append("&");
				sb.append(strName + "=" + strValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static String formNewUrl(URL lastUrl, String uri) {
		String strUrl = uri;
		try {
			if ((!(uri.startsWith("http://"))) && (!(uri.startsWith("https://")))) {
				if (uri.startsWith("/")) {
					strUrl = lastUrl.getProtocol() + "://" + lastUrl.getHost() + uri;
				} else {
					String lastUri = lastUrl.toString();
					int idx = lastUri.lastIndexOf("/");
					strUrl = lastUri.substring(0, idx + 1) + uri;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strUrl;
	}

	public static String parseHyperLinkByKey(String strContent, String strKey) {
		return parseHyperLinkByKey(strContent, strKey, 0);
	}

	private static String parseHyperLinkByKey(String strContent, String strKey, int offset) {
		String strLink = null;
		try {
			if ((StringUtil.isNotEmpty(strContent)) && (strContent.contains(strKey))) {
				int idx = strContent.indexOf(strKey, offset);
				if (idx >= 0) {
					int idxFrom = strContent.substring(0, idx).lastIndexOf("\"");
					int idxTo = strContent.indexOf("\"", idx);

					if ((idxFrom >= 0) && (idxTo >= 0) && (idxTo > idxFrom)) {
						strLink = strContent.substring(idxFrom + 1, idxTo);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strLink;
	}

	public static Vector<String> parseAllHyperLinksByKey(String strContent, String strKey) {
		Vector<String> vLinks = new Vector<String>();
		try {
			if ((StringUtil.isNotEmpty(strContent)) && (strContent.contains(strKey))) {
				String strLink = parseHyperLinkByKey(strContent, strKey);

				while (StringUtil.isNotEmpty(strLink)) {
					vLinks.addElement(strLink);
					int idx = strContent.indexOf(strLink);
					strLink = parseHyperLinkByKey(strContent, strKey, idx + strLink.length());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return vLinks;
	}

}