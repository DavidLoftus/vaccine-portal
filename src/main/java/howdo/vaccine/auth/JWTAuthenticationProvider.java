package howdo.vaccine.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static howdo.vaccine.jwt.SecurityConstants.SECRET;

@Component
public class JWTAuthenticationProvider implements AuthenticationProvider {

    private final Algorithm algorithm = HMAC512(SECRET.getBytes());
    private final JWTVerifier verifier = JWT.require(algorithm).build();

    @Autowired
    private UserDetailsService userDetailsService;


    protected void doAfterPropertiesSet() {
        Assert.notNull(this.userDetailsService, "A UserDetailsService must be set");
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(JWTAuthenticationToken.class, authentication);

        JWTAuthenticationToken jwtAuth = (JWTAuthenticationToken) authentication;

        DecodedJWT token = verifier.verify(jwtAuth.getToken());

        UserDetails userDetails = userDetailsService.loadUserByUsername(token.getSubject());

        return new JWTAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JWTAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
