package br.com.erudio.securityJwt;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.auth0.jwt.algorithms.Algorithm;

import br.com.erudio.vo.v1.security.TokenVO;
import jakarta.annotation.PostConstruct;

@Service
public class JwtTokenProvider {

	@Value("${security.jwt.token.secret-key:secret}")
	private String secretKey = "secret";

	@Value("${security.jwt.token.expire-length:3600000}")
	private long validityInMilliSeconds = 3600000; // 1H

	@Autowired
	private UserDetailsService userDetailsService;

	Algorithm algorithm = null;
	
	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
		algorithm = Algorithm.HMAC256(secretKey.getBytes());
	}
	
	public TokenVO createAcessToken(String userName, List<String> roles) {
		Date now = new Date();
		Date validity = new Date(now.getTime() + validityInMilliSeconds);
		var accessToken = getAccessToken(userName, roles, now, validity);
		var refreshToken = getAccessToken(userName, roles, now);
		
		return new TokenVO(userName, true, now, validity, accessToken, refreshToken);
	}

	private String getAccessToken(String userName, List<String> roles, Date now) {
		// TODO Auto-generated method stub
		return null;
	}

	private String getAccessToken(String userName, List<String> roles, Date now, Date validity) {
		// TODO Auto-generated method stub
		return null;
	}

}
