package ru.otus.spring.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.UserAccount;
import ru.otus.spring.repository.UserAccountRepository;

@Service
@RequiredArgsConstructor
public class UserAccountService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = userAccountRepository.findByUsername(username);
        if (userAccount == null) {
            throw new UsernameNotFoundException(username);
        }
        return User.builder()
                .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder()::encode)
                .username(userAccount.getUsername())
                .password(userAccount.getPassword())
                .roles(userAccount.getRoles())
                .build();
    }
}
