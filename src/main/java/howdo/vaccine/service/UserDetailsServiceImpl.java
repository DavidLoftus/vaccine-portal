package howdo.vaccine.service;

import howdo.vaccine.model.User;
import howdo.vaccine.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.transaction.Transactional;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);


    public class MyUserDetails implements UserDetails {

        private final User user;

        public MyUserDetails(User user) {
            this.user = user;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            var dbAuths = user.getAuthorities()
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            if (dbAuths.isEmpty()) {
                logger.warn("User '{}' has no authorities and will be treated as 'not found'", user.getPpsNumber());
            }

            return dbAuths;
        }

        @Override
        public String getPassword() {
            return user.getPassword();
        }

        @Override
        public String getUsername() {
            return user.getPpsNumber();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return user.getAccountLockExpiry() == null || user.getAccountLockExpiry().compareTo(new Date()) < 0;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        public User getUser() {
            return user;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<User> users = userRepository.getUserByPpsNumber(username);

        if (users.size() == 0) {
            this.logger.debug("Query returned no results for user '" + username + "'");

            throw new UsernameNotFoundException(
                    MessageFormat.format("Username {0} not found", username));
        }

        User user = users.get(0);
        return new MyUserDetails(user);
    }

    protected UserDetails createUserDetails(String username,
                                            User userFromUserQuery, List<GrantedAuthority> combinedAuthorities) {
        return new org.springframework.security.core.userdetails.User(
                userFromUserQuery.getPpsNumber(),
                userFromUserQuery.getPassword(),
                true,
                true,
                true,
                true,
                combinedAuthorities);
    }
}
