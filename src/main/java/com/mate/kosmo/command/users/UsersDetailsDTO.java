package com.mate.kosmo.command.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Slf4j
@Getter
@AllArgsConstructor
public class UsersDetailsDTO implements UserDetails {

    private UsersDTO usersDTO;

    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }// getAuthorities

    @Override
    public String getPassword() {
        return usersDTO.getPassword();
    }// getPassword

    @Override
    public String getUsername() {
        return usersDTO.getId();
    }// getUsername

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }// isAccountNonExpired

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }// isAccountNonLocked

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }// isCredentialsNonExpired

    @Override
    public boolean isEnabled() {
        return false;
    }// isEnabled

}// UsersDetailsDTO