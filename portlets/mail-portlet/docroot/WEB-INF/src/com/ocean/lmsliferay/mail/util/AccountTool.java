package com.ocean.lmsliferay.mail.util;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.portlet.PortletException;
import javax.servlet.http.HttpServletRequest;

import com.liferay.mail.model.Account;
import com.liferay.mail.util.MailManager;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.User;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;

public class AccountTool {

	private static Log _log = LogFactoryUtil.getLog(LmsMailLog.class);
	
	/**
	 * Search account
	 * @param request
	 * @param domain 
	 * @param user 
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static boolean isPresentAccount(HttpServletRequest request, String accountName )
			throws PortalException, SystemException {
		
		MailManager mailManager = MailManager.getInstance(request);

		List<Account> accounts = mailManager.getAccounts();

		boolean found= false;
		for (Account account : accounts) {
			if( account.getAddress() != null && account.getAddress().trim().equals( accountName ) ){
				found= true;
				break;
			}
		};
		return found;
	}

	/**
	 * Setup account, multiple steps if configured in properties
	 * <ul>
	 * <li>Create account</li>
	 * <li>Send welcome message, this will create initial folders. Always done y check folders isn't empty</li>
	 * <li>Check basic folders existence</li>
	 * </ul>
	 * 
	 * @param request
	 * @param user
	 * @param site
	 * @param address
	 * @param domain
	 * @throws PortalException
	 * @throws SystemException
	 * @throws PortletException 
	 */
	public static void setupAccount(HttpServletRequest request, User user,
			Group site, String address, String domain)
					throws PortalException, SystemException {
	
		MailManager mailManager = MailManager.getInstance(request);
		
		//Some server needs to send an email to create the account, so it has to be done before
		boolean sendWelcome= LmsPortletPropsValues.SEND_WELCOME;
		
		if( sendWelcome ){
			sendWelcome(request, user, site, address, domain);
		}
		
		createAccount(request, mailManager, user, site, address, domain);
				
		boolean checkFolders= LmsPortletPropsValues.CHECK_FOLDERS;
		
		if( checkFolders ){
			checkFolders(mailManager, user, address);
		}
		
	}
	
	private static void checkFolders(MailManager mailManager, User user, String address) throws PortalException, SystemException {
		
		List<Account> accounts= mailManager.getAccounts();
		String prefix= "INBOX.";
		for( Account account: accounts ){
			if( account.getAddress().equals(address)){
				
				//create default folders, ignore errors
				mailManager.addFolder(account.getAccountId(), prefix + "Sent");
				mailManager.addFolder(account.getAccountId(), prefix + "Draft");
				mailManager.addFolder(account.getAccountId(), prefix + "Trash");
				
			}
		}
		
	}

	/**
	 * Send a welcome message this will create folder infrastructure en virtual servers, so is recommended
	 * @param mailManager
	 * @throws PortalException 
	 * @throws SystemException 
	 * @throws PortletException 
	 */
	private static void sendWelcome(HttpServletRequest request, User user, Group group, String address, String domain) throws PortalException, SystemException {
		
		boolean loginWithFullAddress= LmsPortletPropsValues.LOGIN_WITH_FULL_ADDRESS;
		
		final String username = loginWithFullAddress ? address : user.getScreenName();
		final String password = PortalUtil.getUserPassword(request);
		boolean outSecure= LmsPortletPropsValues.OUT_SECURE;
		String outgoingHostName= LmsPortletPropsValues.OUT_HOSTNAME;
		int outgoingPort= LmsPortletPropsValues.OUT_PORT;
		String fromAddress= LmsPortletPropsValues.WELCOME_FROM;
		String subject= LmsPortletPropsValues.WELCOME_SUBJECT;
		String text= LmsPortletPropsValues.WELCOME_TEXT;
		
		//TODO multilanguage subject/text
		
		CheckNotNull(fromAddress, PortletPropsKeys.WELCOME_FROM);
		CheckNotNull(subject, PortletPropsKeys.WELCOME_SUBJECT);
		CheckNotNull(text, PortletPropsKeys.WELCOME_TEXT);
		
		fromAddress= fromAddress.replace("${domain}", domain);
		//school has to be organizations
		String schoolName= group.getName();
		if( group.isOrganization() ){
			Organization org= OrganizationLocalServiceUtil.getOrganization( group.getOrganizationId() );
			schoolName= org.getName();
		}
		
		text= text.replace("${school}", schoolName );

		Properties props = new Properties();
		
		if( !outSecure ){
			props.setProperty("mail.user", username );
			props.setProperty("mail.password", password );
		}
			
		props.put("mail.smtp.auth", new Boolean(outSecure).toString());
		if( outSecure ){
			props.put("mail.smtp.starttls.enable", "true");
		}
		props.put("mail.smtp.host", outgoingHostName );
		props.put("mail.smtp.port", outgoingPort );

		Session session = null;
		if( outSecure ){	
			session= Session.getInstance(props,
			  new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			  });
		}else{
			session= Session.getInstance(props);
		}

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromAddress));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(address));
			message.setSubject(subject);
			message.setText(text);

			Transport.send(message);

			if( _log.isInfoEnabled() ){
				_log.info("Welcome mail sent to " + address );
			}

		} catch (MessagingException e) {
			throw new PortalException(e);
		}
	}


	/**
	 * Create an account, we already checked it's needed (not existent)
	 * @param request
	 * @param user
	 * @param site
	 * @param address
	 * @param domain
	 * @throws PortalException
	 * @throws SystemException
	 */
	private static void createAccount(HttpServletRequest request, MailManager mailManager, User user,
			Group site, String address, String domain)
					throws PortalException, SystemException {
				
		String protocol= LmsPortletPropsValues.PROTOCOL;
		String personalName= user.getFullName();
		String incomingHostName= LmsPortletPropsValues.IN_HOSTNAME;
		int incomingPort= LmsPortletPropsValues.IN_PORT;
		boolean incomingSecure= LmsPortletPropsValues.IN_SECURE;
		String outgoingHostName= LmsPortletPropsValues.OUT_HOSTNAME;
		int outgoingPort= LmsPortletPropsValues.OUT_PORT;
		boolean outgoingSecure= LmsPortletPropsValues.OUT_SECURE;
		
		String login= user.getScreenName();
		if( LmsPortletPropsValues.LOGIN_WITH_FULL_ADDRESS ){
			login= address;
		}
		
		String password= PortalUtil.getUserPassword(request);
		
		CheckNotNull(protocol, PortletPropsKeys.PROTOCOL);
		CheckNotNull(incomingHostName, PortletPropsKeys.IN_HOSTNAME);
		CheckNotNull(incomingPort, PortletPropsKeys.IN_PORT);
		
		CheckNotNull(outgoingHostName, PortletPropsKeys.OUT_HOSTNAME);
		CheckNotNull(outgoingPort, PortletPropsKeys.OUT_PORT);
		
				
		mailManager.addAccount(address, personalName, protocol,
				incomingHostName, incomingPort, incomingSecure,
				outgoingHostName, outgoingPort, outgoingSecure,
				login, password, true, null, false, null, true);
		
		
	}
	
	private static void CheckNotNull(Object o, String propertyName) throws PortalException {
		if( o == null ){
			throw new PortalException("Missing property: " + propertyName );
		}
	}
}
