package howdo.vaccine.authentication;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

public class CustomWebAuthenticationDetails extends WebAuthenticationDetails
{
    private String verificationCode;
    public CustomWebAuthenticationDetails(HttpServletRequest request)
    {
        super(request);
        verificationCode = request.getParameter("2fa");
    }
    public String getVerificationCode() { return verificationCode; }
}
