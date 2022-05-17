package howdo.vaccine.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class TwoFactorAuthenticationToken extends UsernamePasswordAuthenticationToken {


    public TwoFactorAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public TwoFactorAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
