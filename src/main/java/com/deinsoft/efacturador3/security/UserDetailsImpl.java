package com.deinsoft.efacturador3.security;

import com.deinsoft.efacturador3.model.SecUser;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsImpl implements UserDetails{
	
	private long id;
	private String username;
	private String password;
	private boolean isAccountNonExpired;
	
	Collection<? extends GrantedAuthority> authorities;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UserDetailsImpl(SecUser user) {
		super();
	}
	
	public UserDetailsImpl(Long id, String username, String email, String password,
			Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.authorities = authorities;
	}
	
	public long getUserId() {
	      return id;
	}
	 
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return isAccountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

}
