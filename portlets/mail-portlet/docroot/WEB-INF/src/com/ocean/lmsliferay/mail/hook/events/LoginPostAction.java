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

package com.ocean.lmsliferay.mail.hook.events;

import java.util.List;

import javax.portlet.PortletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.User;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.ocean.lmsliferay.mail.util.AccountTool;
import com.ocean.lmsliferay.mail.util.CustomFields;
import com.ocean.lmsliferay.mail.util.LmsMailLog;
import com.ocean.lmsliferay.mail.util.LmsPortletPropsValues;

/**
 * @author Manuel Sanchez
 */
public class LoginPostAction extends com.liferay.mail.hook.events.LoginPostAction {

	private static Log _log = LogFactoryUtil.getLog(LmsMailLog.class);
	
	@Override
	public void run(HttpServletRequest request, HttpServletResponse response)
		throws ActionException {

		try {
			
			boolean checkUserAccount= LmsPortletPropsValues.CHECK_USER_ACCOUNT;
			
			if( checkUserAccount ){
				createAccountIfNeeded(request);
			}
		}
		catch (Exception e) {
			throw new ActionException(e);
		}
		
		super.run(request, response);
	}
	
	
	/**
	 * For each site the user belongs to that have the custom field for the domain, create an account if needed sending the welcome 
	 * message and checking mail folders as per configuration
	 * @return
	 * @throws Exception 
	 * @throws PortletException 
	 */
	protected void createAccountIfNeeded(HttpServletRequest request)
			throws Exception {
		
		User user = PortalUtil.getUser(request);
		
		
		//we need a permissions checker in this thread (its short lived and its removed now)
		PermissionChecker permissionChecker = PermissionCheckerFactoryUtil.create(user);

		PermissionThreadLocal.setPermissionChecker(permissionChecker);
		
		List<Group> sites= user.getMySites(); 
		
		for( Group site: sites ){
			
			String domain= (String)site.getExpandoBridge().getAttribute(CustomFields.Group.LMS_MAIL_DOMAIN);
			
			if( StringUtils.isEmpty(domain) && site.isOrganization() ){
				Organization org= OrganizationLocalServiceUtil.getOrganization( site.getOrganizationId() );
				domain= (String)org.getExpandoBridge().getAttribute(CustomFields.Group.LMS_MAIL_DOMAIN);
			}
			
			if( !StringUtils.isEmpty(domain) ){
				String accountName= user.getScreenName() + "@" + domain.toLowerCase().trim();
				
				if( !AccountTool.isPresentAccount(request, accountName)){
					_log.info("Will create user account:" + accountName );
					
					AccountTool.setupAccount(request, user, site, accountName, domain );
				}				
			}else{
				_log.info("No domain for Lms site: " + site.getName() + ". Unable to auto-create mail account" );
			}
		}
	}
	


}