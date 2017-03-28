package org.openforis.collect.security;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openforis.collect.manager.UserManager;
import org.openforis.collect.model.User;
import org.openforis.collect.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class CollectAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserManager userManager;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = (String) authentication.getPrincipal();
		String rawPassword = (String) authentication.getCredentials();
		if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(rawPassword)) {
			boolean success = userManager.login(username, rawPassword);
			if (success) {
				User user = userManager.loadByUserName(username);
				List<UserRole> roles = user.getRoles();
				List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>(roles.size());
				for (UserRole userRole : roles) {
					grantedAuthorities.add(new SimpleGrantedAuthority(userRole.getCode()));
				}
				return new UsernamePasswordAuthenticationToken(
			              username, rawPassword, grantedAuthorities);
			} else {
				throw new BadCredentialsException("Wrong username or password");
			}
		} else {
			throw new BadCredentialsException("Username or password not specified");
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.equals(authentication);
	}
}
