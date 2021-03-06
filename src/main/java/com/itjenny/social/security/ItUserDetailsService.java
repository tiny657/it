package com.itjenny.social.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.itjenny.domain.user.SocialUser;
import com.itjenny.service.user.SocialUserService;

@Service
public class ItUserDetailsService implements UserDetailsService {
	private final Logger logger = LoggerFactory.getLogger(ItUserDetailsService.class);

	@Autowired
	private SocialUserService socialUserService;

	// TODO: need to move property
	private String adminUsers = "jenny:tiny657";

	public void setAdminUsers(String adminUsers) {
		this.adminUsers = adminUsers;
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		SocialUser socialUser = socialUserService.findByUserId(username);
		if (socialUser == null) {
			throw new UsernameNotFoundException(String.format("%s not found!", username));
		}

		return new ItUser(username, socialUser.getAccessToken(), socialUser.getProviderIdBySnsType(),
				createGrantedAuthorities(username));
	}

	public UserDetails loadUserByEmail(String email) {
		SocialUser socialUser = socialUserService.findByEmail(email);
		if (socialUser == null) {
			throw new UsernameNotFoundException(String.format("%s not found!", email));
		}

		return new ItUser(email, socialUser.getPassword(), socialUser.getProviderIdBySnsType(),
				createGrantedAuthorities(email));
	}

	private List<GrantedAuthority> createGrantedAuthorities(String username) {
		logger.debug("UserName : {}", username);
		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		if (isAdmin(username)) {
			grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMINISTRATOR"));
		}
		return grantedAuthorities;
	}

	boolean isAdmin(String username) {
		String[] userIds = adminUsers.split(":");
		return Arrays.asList(userIds).contains(username);
	}
}