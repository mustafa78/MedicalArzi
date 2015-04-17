package com.example.medicalarzi.util;

/* @description:This classes used to send mails.
 *	@tile: PwaMail.java
 * @author: vijaya kumar pakalapati, juan mendez
 * @author-mail:Vijay_Kumar-CW@discovery.com (or) vijay_pakalapati@yahoo.com
 * @version $Revision: 1.4 $, $Date: 2013/01/16 00:34:42 $
 * @details: Virtual Library Light box (PWA-Personal Work Area Ver 3.0)
 * Copyright (C) 2003 Discovery Comunications Inc.. All rights reserved
 **/
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @author mkanchwa
 * 
 */
public final class MAPMail {

	public static Logger logger = LogManager.getLogger(MAPMail.class);

	public String lastError = "";

	public MAPMail() {
	}

	/**
	 * Send multipart mail, mail message and file attachment.
	 * 
	 * @param list
	 * @param from
	 * @param host
	 * @param message
	 * @param Filename
	 * @return
	 */
	public boolean sendMailAndFile(String[] list, String from, String host,
			String message, String Filename) {
		return false;
	}

	/**
	 * 
	 * @param text
	 * @return
	 */
	private String processNewLine(String text) {
		if (text.indexOf("\n") < 0) {
			return text;
		} else {
			StringTokenizer textST = new StringTokenizer(text, "\n");
			StringBuffer textSB = new StringBuffer();
			while (textST.hasMoreTokens()) {
				textSB.append(textST.nextToken());
				textSB.append("<br>");
			}

			return textSB.toString();
		}

	}

	/**
	 * Sends the mail to the list of emails.
	 * 
	 * @param list
	 * @param bccList
	 * @param from
	 * @param cc
	 * @param replyto
	 * @param host
	 * @param message
	 * @param subject
	 * @param personal
	 * @return
	 */
	public boolean sendMail(String[] list, String[] bccList, String from,
			String cc, String replyto, String host, String message,
			String subject, String personal) {

		logger.debug("from : " + from);
		logger.debug("replyTo : " + replyto);
		logger.debug("host : " + host);
		logger.debug("message : " + message);
		logger.debug("subject : " + subject);

		String errorMsg = "AllOk";
		boolean allOk = false;
		boolean debug = true;

		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		if (debug)
			props.put("mail.debug", "true");
		Session session = Session.getDefaultInstance(props, null);
		session.setDebug(debug);
		// create a message
		Message msg = new MimeMessage(session);
		try {
			if (StringUtils.isNoneEmpty(personal)) {
				msg.setFrom(new InternetAddress(from, personal));

			} else {
				msg.setFrom(new InternetAddress(from));
			}

			if (list != null) {
				InternetAddress[] address = new InternetAddress[list.length];
				for (int i = 0; i < list.length; i++) {
					address[i] = new InternetAddress(list[i]);
				}
				msg.setRecipients(Message.RecipientType.TO, address);
			}

			// Check if the 'BCC' address field is not null. If not null, set
			// the 'BCC' field.
			if (bccList != null) {
				InternetAddress[] bccAddress = new InternetAddress[bccList.length];
				for (int i = 0; i < bccList.length; i++) {
					bccAddress[i] = new InternetAddress(bccList[i]);
				}
				msg.setRecipients(Message.RecipientType.BCC, bccAddress);
			}

			if (cc != null) {
				msg.setRecipient(Message.RecipientType.CC, new InternetAddress(
						cc));
			}

			Address replyToList[] = { new InternetAddress(replyto) };

			msg.setReplyTo(replyToList);

			msg.setSubject(subject);
			msg.setSentDate(new Date());

			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(processNewLine(message), "text/html");

			// Create a related multi-part to combine the parts
			MimeMultipart multipart = new MimeMultipart("related");
			multipart.addBodyPart(messageBodyPart);

			// Associate multi-part with message
			msg.setContent(multipart);

			Transport.send(msg);
			allOk = true;

		} catch (UnsupportedEncodingException unsupEx) {
			logger.error(unsupEx.getMessage());
		} catch (MessagingException mex) {

			errorMsg = "\n--Exception handling in sendMail.java: details" + mex;
			logger.error(errorMsg);
			mex.printStackTrace();
			Exception ex = mex;
			int count = 0;
			do {

				if (count == 2) {
					ex = null;
					break;
				}
				count++;

				if (ex instanceof SendFailedException) {
					SendFailedException sfex = (SendFailedException) ex;
					Address[] invalid = sfex.getInvalidAddresses();
					if (invalid != null) {
						errorMsg = errorMsg + "\n    ** Invalid Addresses";
						logger.error(errorMsg);
						if (invalid != null) {
							for (int i = 0; i < invalid.length; i++)
								errorMsg = errorMsg + "\n         "
										+ invalid[i];
						}
						logger.debug(errorMsg);
					}
					Address[] validUnsent = sfex.getValidUnsentAddresses();
					if (validUnsent != null) {
						errorMsg = errorMsg + "\n    ** ValidUnsent Addresses";
						logger.error(errorMsg);
						if (validUnsent != null) {
							for (int i = 0; i < validUnsent.length; i++)
								errorMsg = errorMsg + "\n         "
										+ validUnsent[i];
						}
						logger.debug(errorMsg);
					}
					Address[] validSent = sfex.getValidSentAddresses();
					if (validSent != null) {
						errorMsg = errorMsg + "\n    ** ValidSent Addresses";
						logger.error(errorMsg);
						if (validSent != null) {
							for (int i = 0; i < validSent.length; i++)
								errorMsg = errorMsg + "\n         "
										+ validSent[i];
						}
						logger.debug(errorMsg);
					}
					if (allOk = retrySendingMail(msg, validUnsent))
						break;
				}

				if (ex instanceof MessagingException)
					ex = ((MessagingException) ex).getNextException();
				else
					ex = null;

				logger.debug("PwaMailRetryCount->" + count);

			} while (ex != null);
		}
		return allOk;
	}

	/**
	 * Sends the mail to list of e-mails.
	 * 
	 * @param list
	 * @param from
	 * @param replyto
	 * @param host
	 * @param message
	 * @param subject
	 * @param personal
	 * @return
	 */
	public boolean sendMail(String[] list, String from, String replyto,
			String host, String message, String subject, String personal) {

		String errorMsg = "AllOk";
		boolean allOk = false;
		boolean debug = true;

		Properties props = new Properties();
		props.put("mail.smtp.host", host);

		if (debug) {
			props.put("mail.debug", "true");
		}

		Session session = Session.getDefaultInstance(props, null);

		session.setDebug(debug);

		// create a message
		Message msg = new MimeMessage(session);
		try {

			if (StringUtils.isNoneEmpty(personal)) {
				msg.setFrom(new InternetAddress(from, personal));

			} else {
				msg.setFrom(new InternetAddress(from));
			}

			InternetAddress[] address = new InternetAddress[list.length];
			for (int i = 0; i < list.length; i++) {
				address[i] = new InternetAddress(list[i]);
			}
			msg.setRecipients(Message.RecipientType.TO, address);

			Address replyToList[] = { new InternetAddress(replyto) };

			msg.setReplyTo(replyToList);

			msg.setSubject(subject);
			msg.setSentDate(new Date());

			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(processNewLine(message), "text/html");

			// Create a related multi-part to combine the parts
			MimeMultipart multipart = new MimeMultipart("related");
			multipart.addBodyPart(messageBodyPart);

			// Associate multi-part with message
			msg.setContent(multipart);

			Transport.send(msg);
			allOk = true;

		} catch (UnsupportedEncodingException unsupEx) {
			logger.error(unsupEx.getMessage());
		} catch (MessagingException mex) {

			errorMsg = "\n--Exception handling in sendMail.java: details" + mex;
			logger.error(errorMsg);
			mex.printStackTrace();
			Exception ex = mex;
			int count = 0;
			do {

				if (count == 2) {
					ex = null;
					break;
				}
				count++;

				if (ex instanceof SendFailedException) {
					SendFailedException sfex = (SendFailedException) ex;
					Address[] invalid = sfex.getInvalidAddresses();
					if (invalid != null) {
						errorMsg = errorMsg + "\n    ** Invalid Addresses";
						logger.debug(errorMsg);
						if (invalid != null) {
							for (int i = 0; i < invalid.length; i++)
								errorMsg = errorMsg + "\n         "
										+ invalid[i];
						}
						logger.debug(errorMsg);
					}
					Address[] validUnsent = sfex.getValidUnsentAddresses();
					if (validUnsent != null) {
						errorMsg = errorMsg + "\n    ** ValidUnsent Addresses";
						logger.debug(errorMsg);
						if (validUnsent != null) {
							for (int i = 0; i < validUnsent.length; i++)
								errorMsg = errorMsg + "\n         "
										+ validUnsent[i];
						}
						logger.debug(errorMsg);
					}
					Address[] validSent = sfex.getValidSentAddresses();
					if (validSent != null) {
						errorMsg = errorMsg + "\n    ** ValidSent Addresses";
						logger.debug(errorMsg);
						if (validSent != null) {
							for (int i = 0; i < validSent.length; i++)
								errorMsg = errorMsg + "\n         "
										+ validSent[i];
						}
						logger.debug(errorMsg);
					}
					if (allOk = retrySendingMail(msg, validUnsent))
						break;
				}

				if (ex instanceof MessagingException)
					ex = ((MessagingException) ex).getNextException();
				else
					ex = null;

				logger.debug("PwaMailRetryCount->" + count);

			} while (ex != null);
		}
		return allOk;
	} // end of sendMail()

	/**
	 * Sends the email to the list of users using an inline image attachment.
	 * 
	 * @param list
	 * @param from
	 * @param replyto
	 * @param host
	 * @param message
	 * @param subject
	 * @param personal
	 * @param Base64Attachment
	 * @param contentId
	 * @return
	 */
	public boolean sendMailWithImage(String[] list, String from,
			String replyto, String host, String message, String subject,
			String personal, String[] Base64Attachment, String[] contentId) {

		String errorMsg = "AllOk";
		boolean allOk = false;
		boolean debug = true;

		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		// props.put("mail.smtp.auth", "true");

		if (debug) {
			props.put("mail.debug", "true");
		}

		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(
								"mustafa_kanchwala@discovery.com",
								"RabiAwwal1436H");
					}
				});

		session.setDebug(debug);

		// create a message
		Message msg = new MimeMessage(session);
		try {

			if (!StringUtils.isNoneEmpty(personal)) {
				msg.setFrom(new InternetAddress(from, personal));

			} else {
				msg.setFrom(new InternetAddress(from));
			}

			InternetAddress[] address = new InternetAddress[list.length];
			for (int i = 0; i < list.length; i++) {
				address[i] = new InternetAddress(list[i]);
			}
			msg.setRecipients(Message.RecipientType.TO, address);

			Address replyToList[] = { new InternetAddress(replyto) };

			msg.setReplyTo(replyToList);

			msg.setSubject(subject);
			msg.setSentDate(new Date());

			// Create a related multi-part to combine the parts
			MimeMultipart multipart = new MimeMultipart("related");

			// First Part is the HTML
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(processNewLine(message), "text/html");
			multipart.addBodyPart(messageBodyPart);

			if (Base64Attachment != null) {

				try {
					for (int i = 0; i < contentId.length; i++) {
						// Second part. Image as an attachment
						InternetHeaders headers = new InternetHeaders();
						headers.addHeader("Content-Type", "image/png");
						headers.addHeader("Content-Transfer-Encoding", "base64");
						MimeBodyPart imagePart = new MimeBodyPart(headers,
								Base64Attachment[i].getBytes());
						imagePart.setDisposition(MimeBodyPart.INLINE);
						imagePart.setContentID(contentId[i]);
						imagePart.setFileName("image" + i + ".jpg");
						multipart.addBodyPart(imagePart);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// Associate multi-part with message
			msg.setContent(multipart);

			Transport.send(msg);
			allOk = true;

		} catch (UnsupportedEncodingException unsupEx) {
			logger.error(unsupEx.getMessage());
		} catch (MessagingException mex) {

			errorMsg = "\n--Exception handling in sendMail.java: details" + mex;
			logger.error(errorMsg);
			mex.printStackTrace();
			Exception ex = mex;
			int count = 0;
			do {

				if (count == 2) {
					ex = null;
					break;
				}
				count++;

				if (ex instanceof SendFailedException) {
					SendFailedException sfex = (SendFailedException) ex;
					Address[] invalid = sfex.getInvalidAddresses();
					if (invalid != null) {
						errorMsg = errorMsg + "\n    ** Invalid Addresses";
						logger.debug(errorMsg);
						if (invalid != null) {
							for (int i = 0; i < invalid.length; i++)
								errorMsg = errorMsg + "\n         "
										+ invalid[i];
						}
						logger.debug(errorMsg);
					}
					Address[] validUnsent = sfex.getValidUnsentAddresses();
					if (validUnsent != null) {
						errorMsg = errorMsg + "\n    ** ValidUnsent Addresses";
						logger.debug(errorMsg);
						if (validUnsent != null) {
							for (int i = 0; i < validUnsent.length; i++)
								errorMsg = errorMsg + "\n         "
										+ validUnsent[i];
						}
						logger.debug(errorMsg);
					}
					Address[] validSent = sfex.getValidSentAddresses();
					if (validSent != null) {
						errorMsg = errorMsg + "\n    ** ValidSent Addresses";
						logger.debug(errorMsg);
						if (validSent != null) {
							for (int i = 0; i < validSent.length; i++)
								errorMsg = errorMsg + "\n         "
										+ validSent[i];
						}
						logger.debug(errorMsg);
					}
					if (allOk = retrySendingMail(msg, validUnsent))
						break;
				}

				if (ex instanceof MessagingException)
					ex = ((MessagingException) ex).getNextException();
				else
					ex = null;

				logger.debug("PwaMailRetryCount->" + count);

			} while (ex != null);
		}
		return allOk;
	} // end of sendMail()

	/**
	 * Retries sending the mail again.
	 * 
	 * @param msg
	 * @param address
	 * @return
	 */
	private boolean retrySendingMail(Message msg, Address[] address) {
		logger.debug("retrySendingMail...");
		boolean allOK = false;
		try {
			msg.setRecipients(Message.RecipientType.TO, address);
			Transport.send(msg);
			allOK = true;
		} catch (MessagingException me) {
			logger.error(me);
		}
		logger.debug("retrySendingMail leaving allOK : " + allOK);
		return allOK;

	}

} // End of PwaMail
