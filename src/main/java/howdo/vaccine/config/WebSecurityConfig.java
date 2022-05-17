package howdo.vaccine.config;

import howdo.vaccine.auth.TwoFactorAuthenticationDetailsSource;
import howdo.vaccine.auth.IpFilterAuthenticationProvider;
import howdo.vaccine.auth.JWTAuthenticationProvider;
import howdo.vaccine.auth.TwoFactorAuthenticationProvider;
import howdo.vaccine.controller.AuthController;
import howdo.vaccine.filter.CSPNonceFilter;
import howdo.vaccine.filter.JWTAuthenticationFilter;
import howdo.vaccine.service.UserDetailsServiceImpl;
import howdo.vaccine.service.UserService;
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

import static howdo.vaccine.auth.SecurityConstants.COOKIE_NAME;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IpFilterAuthenticationProvider ipFilterAuthenticationProvider;

    @Autowired
    private JWTAuthenticationProvider jwtAuthenticationProvider;

    @Autowired
    private TwoFactorAuthenticationDetailsSource detailsSource;

    @Autowired
    AuthController authController;

    @Bean
    public TwoFactorAuthenticationProvider twoFactorAuthenticationProvider() {
        TwoFactorAuthenticationProvider provider = new TwoFactorAuthenticationProvider();
        provider.setUserService(userService);
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(ipFilterAuthenticationProvider());
        auth
                .userDetailsService(userDetailsService)
                .and()
                .authenticationProvider(ipFilterAuthenticationProvider)
                .authenticationProvider(jwtAuthenticationProvider)
                .authenticationProvider(twoFactorAuthenticationProvider());
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

        JWTAuthenticationFilter jwtFilter = new JWTAuthenticationFilter();

        http = http
                .formLogin()
                    .successHandler(jwtFilter::successHandler)
                    .authenticationDetailsSource(detailsSource)
                    .loginPage("/login")
                    .permitAll()
                .and();

        authController.setSuccessHandler(jwtFilter::successHandler);

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
