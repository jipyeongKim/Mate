package com.mate.kosmo.config.handler;

import com.mate.kosmo.command.users.UsersDetailsDTO;
import com.mate.kosmo.service.impl.users.UsersDetailsServiceImpl;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;

@Slf4j
@RequiredArgsConstructor
public class MateAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private UsersDetailsServiceImpl usersDetailsService;

    @NonNull
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        System.out.println("2.MateAuthenticationProvider 시작!");

        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authentication;

        String id = authenticationToken.getName();
        String password = (String) authenticationToken.getCredentials();

        System.out.println("아이디: " + id + ",비밀번호: " + password);

        UsersDetailsDTO usersDetailsDTO = (UsersDetailsDTO) usersDetailsService.loadUserByUsername(id);

        if (!bCryptPasswordEncoder.matches(password,usersDetailsDTO.getPassword())) {
            throw new BadCredentialsException(usersDetailsDTO.getUsername() + "비밀번호가 틀립니다");
        }// if

        return new UsernamePasswordAuthenticationToken(usersDetailsDTO,password,usersDetailsDTO.getAuthorities());

    }// authenticate

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }// supports

}// MateAuthenticationProvider