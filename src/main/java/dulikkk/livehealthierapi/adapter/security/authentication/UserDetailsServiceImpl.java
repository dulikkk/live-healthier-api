package dulikkk.livehealthierapi.adapter.security.authentication;

import dulikkk.livehealthierapi.domain.user.dto.UserDto;
import dulikkk.livehealthierapi.domain.user.query.UserQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserQueryRepository userQueryRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userQueryRepository.findByUsername(username)
                .map(this::getUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika " + username));
    }

    private UserDetails getUserDetails(UserDto user) {
        return User.builder()
                .username(user.getUsername())
                .authorities(convertAuthorities(user))
                .password(user.getPassword())
                .disabled(!user.isActive())
                .build();
    }

    private List<GrantedAuthority> convertAuthorities(UserDto user) {
        return user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.toString()))
                .collect(Collectors.toUnmodifiableList());
    }
}
