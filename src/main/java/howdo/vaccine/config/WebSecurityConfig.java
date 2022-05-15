package howdo.vaccine.config;

import howdo.vaccine.filter.CSPNonceFilter;
import howdo.vaccine.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.header.HeaderWriterFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public IpFilter ipFilterAuthenticationProvider() {
        return new IpFilter(passwordEncoder, userDetailsService);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(ipFilterAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("GET", "/", "/login", "/register").permitAll()
                .antMatchers("GET", "/css/**", "/fonts/**", "/js/**").permitAll()
                .anyRequest().authenticated()
            .and().formLogin()
                .loginPage("/login")
            .and().httpBasic()
            .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .and().requiresChannel().anyRequest().requiresSecure();

        http
            .csrf().disable()
            .headers()
                .xssProtection()
                .and().contentSecurityPolicy("script-src 'self' 'unsafe-inline' 'nonce-{nonce}'; object-src 'none'; base-uri 'none'; report-uri http://localhost:8000/");
        http.addFilterBefore(new CSPNonceFilter(), HeaderWriterFilter.class);
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
}
