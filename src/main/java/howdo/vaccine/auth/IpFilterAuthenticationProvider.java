package howdo.vaccine.auth;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class IpFilterAuthenticationProvider implements AuthenticationProvider {
    public static final int IP_BAN_MINUTES = 20;

    private final Map<String, Date> bannedIps = new HashMap<>();

    public void banAddress(String ip) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, IP_BAN_MINUTES);
        bannedIps.put(ip, calendar.getTime());
    }

    private boolean isBanned(String remoteAddress) {
        if (!bannedIps.containsKey(remoteAddress)) {
            return false;
        }

        if (bannedIps.get(remoteAddress).toInstant().isAfter(Instant.now())) {
            return true;
        }

        bannedIps.remove(remoteAddress);

        return false;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
        if (isBanned(details.getRemoteAddress())) {
            throw new BadCredentialsException("IP Banned");
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
