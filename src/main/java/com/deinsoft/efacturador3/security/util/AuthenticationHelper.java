package com.deinsoft.efacturador3.security.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import com.deinsoft.efacturador3.security.JWTAuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.deinsoft.efacturador3.security.SecurityConstants;

@Service
public class AuthenticationHelper {

    
    public Map<String, Object> getJwtLoggedUserData(HttpServletRequest request) {

        String jwt = request.getHeader(SecurityConstants.HEADER_AUTHORIZACION_KEY).replace("\"", "");
        Jws<Claims> claims = Jwts.parser()
                .setSigningKey(SecurityConstants.SUPER_SECRET_KEY)
                .parseClaimsJws(jwt.replace(SecurityConstants.TOKEN_BEARER_PREFIX + " ", ""));
        
        
        Map<String, Object> mp = new HashMap<>();
        
        //mp.put("idProfile",claims.getBody().get("prf").toString());
        mp.put("user", claims.getBody().get("user"));
        //mp.put("idCompanyGroup", claims.getBody().get("cg"));
        //mp.put("idCompany", claims.getBody().get("cia").toString());
        //mp.put("idBranch", claims.getBody().get("brn").toString());
        //mp.put("authorities", claims.getBody().get("authorities"));

        return mp;
    }

}
