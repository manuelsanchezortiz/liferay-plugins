package com.ocean.lmsliferay.mail.util;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.util.portlet.PortletProps;

/**
 * @author Manuel Sanchez
 */
public class LmsPortletPropsValues {
	
	/**
	 * If check user account
	 */
	public static final boolean CHECK_USER_ACCOUNT = GetterUtil.getBoolean(
			PortletProps.get(PortletPropsKeys.CHECK_USER_ACCOUNT));
	
	/**
	 * Incoming hostname
	 */
	public static final String IN_HOSTNAME = PortletProps.get(
		PortletPropsKeys.IN_HOSTNAME);

	/**
	 * Incoming port
	 */
	public static final int IN_PORT = GetterUtil.getInteger(
			PortletProps.get(PortletPropsKeys.IN_PORT));
	
	/**
	 * Incoming secure (ssl)
	 */
	public static final boolean IN_SECURE = GetterUtil.getBoolean(
			PortletProps.get(PortletPropsKeys.IN_SECURE));
	
	/**
	 * Incoming protocol
	 */
	public static final String PROTOCOL = PortletProps.get(PortletPropsKeys.PROTOCOL);
	
	/**
	 * Outgoing hostname
	 */
	public static final String OUT_HOSTNAME= PortletProps.get(PortletPropsKeys.OUT_HOSTNAME);
	
	
	/**
	 * Outgoing port
	 */
	public static final int OUT_PORT = GetterUtil.getInteger(
			PortletProps.get(PortletPropsKeys.OUT_PORT));
	
	/**
	 * Outgoing secure (ssl)
	 */
	public static final boolean OUT_SECURE = GetterUtil.getBoolean(
			PortletProps.get(PortletPropsKeys.OUT_SECURE));
	
	/**
	 * Login on the mail server with full address not only by name
	 */
	public static final boolean LOGIN_WITH_FULL_ADDRESS = GetterUtil.getBoolean(
			PortletProps.get(PortletPropsKeys.LOGIN_WITH_FULL_ADDRESS));
	
	/**
	 * Checked folders
	 */
	public static final boolean CHECK_FOLDERS = GetterUtil.getBoolean(PortletProps.get(PortletPropsKeys.CHECK_FOLDERS));
	
	/**
	 * Send welcome
	 */
	public static final boolean SEND_WELCOME = GetterUtil.getBoolean(
			PortletProps.get(PortletPropsKeys.SEND_WELCOME));
	
	/**
	 * Welcome message from, see {@link PorletPropsKeys.WELCOME_FROM}
	 */
	public static final String WELCOME_FROM = PortletProps.get(PortletPropsKeys.WELCOME_FROM);
	
	/**
	 * Welcome subject, see {@link PorletPropsKeys.WELCOME_SUBJECT}
	 */
	public static final String WELCOME_SUBJECT = PortletProps.get(PortletPropsKeys.WELCOME_SUBJECT);
	
	/**
	 * Welcome text, see {@link PorletPropsKeys.WELCOME_TEXT}
	 */
	public static final String WELCOME_TEXT = PortletProps.get(PortletPropsKeys.WELCOME_TEXT);
	


}