package com.mate.kosmo.service.impl.users;

import com.mate.kosmo.command.users.UsersDTO;
import com.mate.kosmo.command.users.UsersDetailsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UsersDetailsServiceImpl implements UserDetailsService {

    private final UsersServiceImpl usersService;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        UsersDTO usersDTO = UsersDTO
                                    .builder()
                                    .id(id)
                                    .build();

        if (id == null || id.equals("")) {
            return usersService.login(usersDTO)
                    .map((user) -> new UsersDetailsDTO(user, Collections.singleton(new SimpleGrantedAuthority(user.getId()))))
                    .orElseThrow(() -> new AuthenticationServiceException(id));
        } else {
            return usersService.login(usersDTO)
                    .map((user) -> new UsersDetailsDTO(user, Collections.singleton(new SimpleGrantedAuthority(user.getId()))))
                    .orElseThrow(() -> new BadCredentialsException(id));
        }// else

    }// loadUserByUsername

}// UsersDetailsServiceImpl