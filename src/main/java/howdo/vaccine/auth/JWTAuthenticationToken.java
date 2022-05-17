package howdo.vaccine.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JWTAuthenticationToken extends AbstractAuthenticationToken {
    private final DecodedJWT token;
    private final Object principal;

    public JWTAuthenticationToken(Object principal, DecodedJWT token, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.token = token;
        this.principal = principal;
        setAuthenticated(true);
    }

    public JWTAuthenticationToken(String token) {
        super(null);
        this.token = JWT.decode(token);
        this.principal = this.token.getSubject();
        setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public DecodedJWT getToken() {
        return token;
    }
}
