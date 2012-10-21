package com.cocolog_nifty.kjunichi.kjwfx;

import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mail {
	static final String MY_FROM_ADDRESS = "junichi.kajiwara@gmail.com";

	/**
	 * sendSellAlert
	 * 売った方が良いレートになった場合のアラートを送信する。
	 * @param addr
	 * @param ttb
	 * @param tts
	 */
	public static void sendSellAlert(String addr, String ttb, String tts, String currency) {
		//for debug
		Logger logger = Logger.getLogger("Mail");
		logger.warning("sendSellAlert "+ addr);
		
		StringBuffer sb = new StringBuffer();
		
		// メール送信
		Properties props = new Properties();
		// ここからローカルデバッグ用
		props.put("mail.smtp.host", "smtp.nifty.com");

		// ここまで
		Session session = Session.getDefaultInstance(props, null);
		Message msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress(MY_FROM_ADDRESS));

			msg.addRecipient(Message.RecipientType.TO,
					new InternetAddress(addr));
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sb.append(e.getMessage());
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sb.append(e.getMessage());
		}
		try {
			msg.setSubject("Sell "+currency+"!");
			msg.setText(currency+" 売れ！" + tts + "\n" + "買値 = " + ttb);

		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sb.append(e.getMessage());
		}
		try {
			logger.warning("sb = "+ sb.toString());
			Transport.send(msg);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sb.append(e.getMessage());
		}
		logger.warning("e.getMessage = "+ sb.toString());
	}

	/**
	 * sendBuyAlert
	 * 
	 * @param addr
	 * @param ttb
	 * @param tts
	 */
	public static void sendBuyAlert(String addr, String ttb, String tts, String currency) {
		//for debug
		Logger logger = Logger.getLogger("Mail");
		logger.warning("sendBuyAlert "+ addr);
		
		// メール送信
		Properties props = new Properties();
		// ここからローカルデバッグ用
		props.put("mail.smtp.host", "smtp.nifty.com");

		// ここまで
		Session session = Session.getDefaultInstance(props, null);
		Message msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress(MY_FROM_ADDRESS));

			msg.addRecipient(Message.RecipientType.TO,
					new InternetAddress(addr));
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// mailErrorMsg = mailErrorMsg + e.getMessage();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			msg.setSubject("Buy "+currency+"!");
			msg.setText(currency +" 買え！" + ttb + "\n" + "買値 = " + tts);

		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// mailErrorMsg = mailErrorMsg + e.getMessage();
		}
		try {
			Transport.send(msg);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// mailErrorMsg = mailErrorMsg + e.getMessage();
		}

	}
}
