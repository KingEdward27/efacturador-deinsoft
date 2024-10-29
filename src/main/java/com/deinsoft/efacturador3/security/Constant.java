package com.deinsoft.efacturador3.security;

public class Constant {
	
	public static final String LOGIN_URL = "/login";
	public static final String HEADER_AUTHORIZACION_KEY = "Authorization";
	public static final String TOKEN_BEARER_PREFIX = "Bearer";
	
	public static final String ADMIN = "admin";
	
	public static final String ISSUER_INFO = "OPENSYSPERU";
	public static final String SUPER_SECRET_KEY = "D4ysG0n3.";
	public static final long TOKEN_EXPIRATION_TIME = 30; // minutes
	public static final long TOKEN_REFRESH_EXPIRATION_TIME = 180;

}
