package howdo.vaccine.config;

import howdo.vaccine.auth.IpFilterAuthenticationProvider;
import howdo.vaccine.auth.JWTAuthenticationProvider;
import howdo.vaccine.filter.CSPNonceFilter;
import howdo.vaccine.filter.JWTPreAuthenticationFilter;
import howdo.vaccine.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.HeaderWriterFilter;

import static howdo.vaccine.jwt.SecurityConstants.COOKIE_NAME;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public IpFilterAuthenticationProvider ipFilterAuthenticationProvider() {
        IpFilterAuthenticationProvider authenticationProvider = new IpFilterAuthenticationProvider(passwordEncoder, userDetailsService);
        return authenticationProvider;
    }

    @Autowired
    private JWTAuthenticationProvider jwtAuthenticationProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(ipFilterAuthenticationProvider());
        auth
                .userDetailsService(userDetailsService)
                .and()
                .authenticationProvider(ipFilterAuthenticationProvider())
                .authenticationProvider(jwtAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // Enable CORS and disable CSRF
        http = http.cors().and().csrf().disable();

        // Set session management to stateless
        http = http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        // Set unauthorized requests exception handler
//        http = http
//                .exceptionHandling()
//                .authenticationEntryPoint(
//                        (request, response, ex) -> {
//                            response.sendError(
//                                    HttpServletResponse.SC_UNAUTHORIZED,
//                                    ex.getMessage()
//                            );
//                        }
//                )
//                .and();



        http = http
            .authorizeRequests()
                .antMatchers("GET", "/", "/login", "/register").permitAll()
                .antMatchers("GET", "/css/**", "/fonts/**", "/js/**").permitAll()
                .anyRequest().authenticated()
            .and();

        JWTPreAuthenticationFilter jwtFilter = new JWTPreAuthenticationFilter();

        http = http
                .formLogin()
                    .successHandler(jwtFilter::successHandler)
                    .loginPage("/login")
                    .permitAll()
                .and();

        http = http.httpBasic().and();


        http = http.logout().logoutUrl("/logout").deleteCookies(COOKIE_NAME).and();

        // Add JWT token filter
        http = http
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new CSPNonceFilter(), HeaderWriterFilter.class);

        http.headers()
                .xssProtection()
            .and()
                .contentSecurityPolicy("script-src 'self' 'unsafe-inline' 'nonce-{nonce}'; object-src 'none'; base-uri 'none';/");
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        DelegatingPasswordEncoder delegatingPasswordEncoder =
                (DelegatingPasswordEncoder) PasswordEncoderFactories
                        .createDelegatingPasswordEncoder();

        delegatingPasswordEncoder
                .setDefaultPasswordEncoderForMatches(new BCryptPasswordEncoder());

        return delegatingPasswordEncoder;
    }

    @Bean
    public ServletContextInitializer servletContextInitializer() {
        return servletContext -> servletContext.getSessionCookieConfig().setSecure(true);
    }
}
