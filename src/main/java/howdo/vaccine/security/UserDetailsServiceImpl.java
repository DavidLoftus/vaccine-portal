package howdo.vaccine.security;

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
import java.util.List;
import java.util.stream.Collectors;

@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<User> users = userRepository.getUserByPpsNumber(username);

        if (users.size() == 0) {
            this.logger.debug("Query returned no results for user '" + username + "'");

            throw new UsernameNotFoundException(
                    MessageFormat.format("Username {0} not found", username));
        }

        User user = users.get(0); // contains no GrantedAuthority[]

        List<GrantedAuthority> dbAuths = user.getAuthorities()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        if (dbAuths.size() == 0) {
            this.logger.debug("User '{}' has no authorities and will be treated as 'not found'", username);

            throw new UsernameNotFoundException(
                    MessageFormat.format("User {0} has no GrantedAuthority", username));
        }

        return createUserDetails(username, user, dbAuths);
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
