/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.ocean.lmsliferay.mail.util;

/**
 * @author Manuel Sanchez
 */
public interface PortletPropsKeys {

	/**
	 * Check user's lms mail account and create if it does not exists
	 */
	public static final String CHECK_USER_ACCOUNT = "lms.mail.checkuseraccount";

	/**
	 * Incoming hostname
	 */
	public static final String IN_HOSTNAME = "lms.mail.in.hostname";
	/**
	 * Incoming port
	 */
	public static final String IN_PORT = "lms.mail.in.port";
	/**
	 * Incoming secure
	 */
	public static final String IN_SECURE = "lms.mail.in.secure";
	/**
	 * Incoming protocol
	 */
	public static final String PROTOCOL= "lms.mail.protocol";

	/**
	 * Outgoing hostname
	 */
	public static final String OUT_HOSTNAME = "lms.mail.out.hostname";
	/**
	 * Outgoing port
	 */
	public static final String OUT_PORT = "lms.mail.out.port";
	/**
	 * Outgoing secure
	 */
	public static final String OUT_SECURE = "lms.mail.out.secure";
	
	/**
	 * Login on the mail server with full address
	 */
	public static final String LOGIN_WITH_FULL_ADDRESS= "lms.mail.loginwithfulladdress";
	
	/**
	 * Check if needed folders are present and create them
	 */
	public static final String CHECK_FOLDERS= "lms.mail.checkfolders";
	
	/**
	 * Send welcome message
	 */
	public static final String SEND_WELCOME= "lms.mail.sendwelcome";
	
	/**
	 * Welcome message from, the domain could be fixed of use the
	 * ${domain} key to be replace by current domain (is a parameter for
	 * the user creation)
	 */
	public static final String WELCOME_FROM= "lms.mail.welcome.from";
	
	/**
	 * Welcome subject, localizable using the same technique as the Welcome text
	 */
	public static final String WELCOME_SUBJECT= "lms.mail.welcome.subject";
	
	/**
	 * Welcome text. The last key part can be a language specific, for
	 * example: lms.mail.welcome.text.es</br>
	 * The system will try to load user locale (simple locale) key and
	 * later without the locale: Example:</br>
	 * <code>
	 * lms.mail.welcome.text.es_es, lms.mail.welcome.text.es, lms.mail.welcome.text
	 * </code>
	 * System allow to replace ${school} with site name (which must be the school name)
	 */
	public static final String WELCOME_TEXT= "lms.mail.welcome.text";

}