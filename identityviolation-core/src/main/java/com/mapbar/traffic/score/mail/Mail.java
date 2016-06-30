package com.mapbar.traffic.score.mail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class Mail {

	private static final String MAIL_SMTP_HOST = "mail.smtp.host";
	private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";

	private String mailSendBoolean; // 发邮件的
	private String mailSmtpHost; // 发邮件服务器SMTP
	private String mailTo; // 邮件接收人地
	private String mailFrom; // 邮件发人地址
	private String userName; // 用户
	private String passWord; // 用户密码

	Properties props; // 创建系统属
	Session session; // 邮件会话对象
	MimeMessage mimeMsg; // MIME邮件对象
	private Multipart mp; // Multipart对象,邮件内容,标题,附件等内容均添加到其中后再生成MimeMessage对象

	public String getMailSendBoolean() {
		return mailSendBoolean;
	}

	public void setMailSendBoolean(String mailSendBoolean) {
		this.mailSendBoolean = mailSendBoolean;
	}

	public String getMailSmtpHost() {
		return mailSmtpHost;
	}

	public void setMailSmtpHost(String mailSmtpHost) {
		this.mailSmtpHost = mailSmtpHost;
	}

	public String getMailTo() {
		return mailTo;
	}

	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}

	public String getMailFrom() {
		return mailFrom;
	}

	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public Mail() {
		if (props == null) {
			props = new Properties();
			try {
				// props.load(new InputStreamReader(new FileInputStream(Mail.class.getResource("/violation.properties").getPath()), "UTF-8"));
				props.load(new InputStreamReader(this.getClass().getResourceAsStream("/violation.properties")));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		mailSendBoolean = props.getProperty("mail.send.boolean");
		mailSmtpHost = props.getProperty("mail.smtp.host");
		mailTo = props.getProperty("mail.to");
		mailFrom = props.getProperty("mail.from");
		userName = props.getProperty("mail.from.username");
		passWord = props.getProperty("mail.from.password");
	}

	public void sendMail(String subject, String body, String file) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		if ("true".equals(mailSendBoolean)) {
			if (mailTo.contains(",")) {
				String[] to = mailTo.split(",");
				for (String user : to) {
					this.setSmtpHost(mailSmtpHost);
					this.createMimeMessage();
					this.setNeedAuth(true);
					if (this.setSubject(subject) == false) {
						return;
					}
					if (this.setBody(body) == false) {
						return;
					}
					// 以下是收件人处理
					if (this.setTo(user) == false) {
						return;
					}
					if (this.setFrom(mailFrom) == false) {
						return;
					}
					// 发送附件
					if (this.addFileAffix(file) == false) {
						return;
					}
					this.setNamePass(userName, passWord);
					if (this.sendout() == false) {
						return;
					}
				}
				return;
			} else {
				this.setSmtpHost(mailSmtpHost);
				this.createMimeMessage();
				this.setNeedAuth(true);
				if (this.setSubject(subject) == false) {
					return;
				}
				if (this.setBody(body) == false) {
					return;
				}
				// 以下是收件人处理
				if (this.setTo(mailTo) == false) {
					return;
				}
				if (this.setFrom(mailFrom) == false) {
					return;
				}
				// 发附
				if (this.addFileAffix(file) == false) {
					return;
				}
				this.setNamePass(userName, passWord);
				if (this.sendout() == false) {
					return;
				}
			}
		}
	}

	/**
	 * 设置发邮件服务器SMTP
	 * 
	 * @param hostName
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public void setSmtpHost(String hostName) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		props.put(MAIL_SMTP_HOST, hostName); // 设置SMTP主机
	}

	/**
	 * 获取邮件回话对象后，创建MIME邮件对象
	 * 
	 * @return boolean
	 */
	public boolean createMimeMessage() {
		try {

			session = Session.getInstance(props, null); // 获得邮件会话对象
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		try {

			mimeMsg = new MimeMessage(session); // 创建MIME邮件对象
			mp = new MimeMultipart();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 是否要设置smtp身份认证
	 * 
	 * @param need
	 */
	public void setNeedAuth(boolean need) {
		if (need) {
			props.put(MAIL_SMTP_AUTH, "true");
		} else {
			props.put(MAIL_SMTP_AUTH, "false");
		}
	}

	/**
	 * 设置用户名和密码
	 * 
	 * @param String
	 *            name 用户
	 * @param String
	 *            pass 密码
	 */
	public void setNamePass(String name, String pass) {
		userName = name;
		passWord = pass;
	}

	/**
	 * 设置邮件主题
	 * 
	 * @param String
	 *            mailSubject 邮件主题
	 * @return boolean
	 */
	public boolean setSubject(String mailSubject) {

		try {
			mimeMsg.setSubject(mailSubject);
			return true;
		} catch (Exception e) {

			return false;
		}
	}

	/**
	 * 设置邮件主体
	 * 
	 * @param String
	 *            mailBody 邮件主体
	 * @return boolean
	 */
	public boolean setBody(String mailBody) {
		try {
			BodyPart bp = new MimeBodyPart();
			bp.setContent(mailBody, "text/html;charset=GB2312");
			mp.addBodyPart(bp);
			return true;
		} catch (Exception e) {

			return false;
		}
	}

	/**
	 * 添加附件
	 * 
	 * @param String
	 *            filename 文件
	 * @return boolean
	 */
	public boolean addFileAffix(String filename) {
		if (null == filename || "".equals(filename)) {
			return true;
		}

		try {
			BodyPart bp = new MimeBodyPart();
			FileDataSource fileds = new FileDataSource(filename);
			bp.setDataHandler(new DataHandler(fileds));
			bp.setFileName(MimeUtility.encodeText(fileds.getName()));
			mp.addBodyPart(bp);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 设置发件人
	 * 
	 * @param String
	 *            from 发件人
	 * @return boolean
	 */
	public boolean setFrom(String from) {

		try {
			mimeMsg.setFrom(new InternetAddress(from)); // 设置发信人
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 设置收件人
	 * 
	 * @param String
	 *            to 收件人
	 * @return boolean
	 */
	public boolean setTo(String to) {
		if (to == null) {
			return false;
		}
		try {
			mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 设置抄送邮件
	 * 
	 * @param String
	 *            抄送地址
	 * @return boolean
	 */
	public boolean setCopyTo(String copyto) {
		if (copyto == null)
			return false;
		try {
			mimeMsg.setRecipients(Message.RecipientType.CC, (Address[]) InternetAddress.parse(copyto));
			return true;
		} catch (Exception e) {
			System.err.println("邮件发送失败！" + e);
			return false;
		}
	}

	/**
	 * 发送邮件
	 */
	public boolean sendout() {
		try {
			mimeMsg.setContent(mp);
			mimeMsg.saveChanges();

			Session mailSession = Session.getInstance(props, null);
			Transport transport = mailSession.getTransport("smtp");
			transport.connect((String) props.get(MAIL_SMTP_HOST), userName, passWord);
			transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.TO));

			transport.close();
			return true;
		} catch (Exception e) {
			System.err.println("邮件发送失败！" + e);
			return false;
		}
	}

}
