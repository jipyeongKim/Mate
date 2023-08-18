package com.mate.kosmo.model.users;

import com.mate.kosmo.command.users.UsersDTO;
import com.mate.kosmo.mapper.users.UsersMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UsersDAO {

    private final UsersMapper usersMapper;

    public int createUser(UsersDTO usersDTO) {
        return usersMapper.createUser(usersDTO);
    }// createUser

    public int createKakaoUser(UsersDTO usersDTO) {
        return usersMapper.createKakaoUser(usersDTO);
    }// createKakaoUser

    public int createNaverUser(UsersDTO usersDTO) {
        return usersMapper.createNaverUser(usersDTO);
    }// createNaverUser

    public int createGoogleUser(UsersDTO usersDTO) {
        return usersMapper.createGoogleUser(usersDTO);
    }// createGoogleUser

    public Optional<UsersDTO> login(UsersDTO usersDTO) {
        return usersMapper.login(usersDTO);
    }// login

    public Optional<UsersDTO> getUserInfo(UsersDTO usersDTO) {
        return usersMapper.getUserInfoByUnum(usersDTO);
    }// getUserInfo

    public Optional<UsersDTO> getUserInfoByKakaoId(UsersDTO usersDTO) {
        return usersMapper.getUserInfoByKakaoId(usersDTO);
    }// getUserInfoByKakaoId

    public Optional<UsersDTO> getUserInfoByNaverId(UsersDTO usersDTO) {
        return usersMapper.getUserInfoByNaverId(usersDTO);
    }// getUserInfoByNaverId

    public Optional<UsersDTO> getUserInfoByGoogleId(UsersDTO usersDTO) {
        return usersMapper.getUserInfoByGoogleId(usersDTO);
    }// getUserInfoByGoogleId

    public int deleteUser(UsersDTO usersDTO) {
        return usersMapper.deleteUser(usersDTO);
    }// deleteUser

    public int setUserInfo(UsersDTO usersDTO) {
        return usersMapper.setUserInfo(usersDTO);
    }// setUserInfo

}// UsersDAO
