package howdo.vaccine.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import howdo.vaccine.auth.JWTAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static howdo.vaccine.auth.SecurityConstants.COOKIE_NAME;
import static howdo.vaccine.auth.SecurityConstants.SECRET;

public class JWTAuthenticationFilter extends GenericFilterBean {

    private AuthenticationManager authenticationManager;

    private Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            try {
                return Arrays.stream(cookies)
                        .filter(cookie -> cookie.getName().equals(COOKIE_NAME))
                        .findFirst()
                        .map(cookie -> makeAuthentication(cookie, request)).orElse(null);
            } catch (JWTDecodeException ignored) {}
        }
        return null;
    }

    private Authentication makeAuthentication(Cookie cookie, HttpServletRequest request) {
        JWTAuthenticationToken authentication = new JWTAuthenticationToken(cookie.getValue());
        authentication.setDetails(new WebAuthenticationDetails(request));
        return authentication;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        boolean hasJwt = false;
        try {
            Authentication authentication = attemptAuthentication(request, response);
            if (authentication != null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                hasJwt = true;
            }
        } catch (AuthenticationException ignored) {

        }

        chain.doFilter(req, res);
    }

    protected void addJwtToResponse(HttpServletRequest request, HttpServletResponse response, Authentication authResult)
            throws IOException, ServletException {
        String token = JWT.create().
                withSubject(authResult.getName())
                .withExpiresAt(new Date((long) (System.currentTimeMillis() + 8.64e+8))) //plus 10 days in milliseconds
                .sign(HMAC512(SECRET.getBytes()));

        addCookie(token, response);

        if (request.getAttribute("redirectLocation") != null) {
            response.sendRedirect((String) request.getAttribute("redirectLocation"));
        } else {
            response.sendRedirect("/");
        }
    }

    private void addCookie(String token, HttpServletResponse response)
    {
        Cookie cookie = new Cookie(COOKIE_NAME, token);
        cookie.setMaxAge(7 * 24 * 60 * 60);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);
    }

    public void successHandler(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        addJwtToResponse(request, response, authentication);

    }
}
