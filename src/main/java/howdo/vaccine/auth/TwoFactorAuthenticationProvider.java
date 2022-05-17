package howdo.vaccine.auth;

import howdo.vaccine.model.User;
import howdo.vaccine.service.UserService;
import org.jboss.aerogear.security.otp.Totp;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public class TwoFactorAuthenticationProvider extends DaoAuthenticationProvider {

    private UserService userService;

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException
    {
        String verificationCode = ((TwoFactorAuthenticationDetails) auth.getDetails()).getVerificationCode();

        UserDetails userDetails = getUserDetailsService().loadUserByUsername(auth.getName());

        User user = userService.getUser(userDetails.getUsername());

        if(user.isUsing2FA())
        {
            Totp totp = new Totp(user.getSecret());
            if (!isValidLong(verificationCode) || !totp.verify(verificationCode))
            {
                throw new BadCredentialsException("Invalid verfication code");
            }
        }
        Authentication result = super.authenticate(auth);
        return new UsernamePasswordAuthenticationToken(userDetails, result.getCredentials(), result.getAuthorities());
    }

    private boolean isValidLong(String code)
    {
        try
        {
            Long.parseLong(code);
        }
        catch (NumberFormatException e)
        {
            return false;
        }
        return true;
    }

    @Override
    public boolean supports(Class<?> authentication) { return authentication.equals(UsernamePasswordAuthenticationToken.class); }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
