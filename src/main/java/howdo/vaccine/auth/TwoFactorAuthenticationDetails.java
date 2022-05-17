package howdo.vaccine.auth;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

public class TwoFactorAuthenticationDetails extends WebAuthenticationDetails
{
    private final String verificationCode;
    public TwoFactorAuthenticationDetails(HttpServletRequest request)
    {
        super(request);
        verificationCode = request.getParameter("2fa");
    }
    public String getVerificationCode() { return verificationCode; }
}
