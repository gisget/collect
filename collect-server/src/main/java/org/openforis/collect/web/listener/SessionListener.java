/**
 * 
 */
package org.openforis.collect.web.listener;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openforis.collect.manager.SessionManager;
import org.openforis.collect.model.User;
import org.openforis.collect.web.session.InvalidSessionException;
import org.openforis.collect.web.session.SessionState;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author M. Togna
 * @author S. Ricci
 * 
 */
public class SessionListener implements HttpSessionListener {

	private static Log LOG = LogFactory.getLog(SessionListener.class);
	
	@Override
	public void sessionCreated(HttpSessionEvent se) {
		SessionManager sessionManager = getSessionManager(se);
		sessionManager.createSessionState(se.getSession());
		
		if ( LOG.isInfoEnabled() ) {
			LOG.info("Session created: " + se.getSession().getId());
		}
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		SessionManager sessionManager = getSessionManager(se);
		try {
			SessionState sessionState = sessionManager.getSessionState();
			User user = null;
			if (sessionState != null) {
				user = sessionState.getUser();
				if (user != null) {
					sessionManager.sessionDestroyed();
				}
			}
			if ( LOG.isInfoEnabled() ) {
				String message = "Session destroyed: " + se.getSession().getId();
				if ( user != null ) {
					message += " username: " +user.getUsername();
				}
				LOG.info(message);
			}
		} catch(InvalidSessionException e) {
			//ignore it, session was anonymous
		}
	}

	private SessionManager getSessionManager(HttpSessionEvent se) {
		return getBean(se, "sessionManager");
	}

	@SuppressWarnings("unchecked")
	private <T extends Object> T getBean(HttpSessionEvent se,
			String name) {
		ServletContext sc = se.getSession().getServletContext();
		WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);
		return (T) applicationContext.getBean(name);
	}
	
}
