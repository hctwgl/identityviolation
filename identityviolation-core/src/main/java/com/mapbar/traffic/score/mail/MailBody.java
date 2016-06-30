package com.mapbar.traffic.score.mail;

public class MailBody {
	public static String createBody(String subject, String content) {
		StringBuffer body = new StringBuffer();
		body.append("<meta http-equiv=Content-Type content=text/html;charset=gb2312>");
		body.append("<div style='font:14px;color:red;line-height:25px;width:60%'>");
		body.append(content.replaceAll("\n", "<br>"));
		body.append("<br><br>");
		body.append("本邮件由数据商店接口平台发出(服务器部门专用)，请勿回复！");
		body.append("<hr>");
		body.append("<div style='font:14px;color:red;line-height:25px' align='right'>");
		body.append("<br>").append(subject);
		body.append("<br>").append("");
		body.append("</div>");
		body.append("</div>");

		return body.toString();
	}
}
