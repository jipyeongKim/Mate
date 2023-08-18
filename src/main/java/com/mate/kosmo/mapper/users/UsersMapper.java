package com.mate.kosmo.mapper.users;

import com.mate.kosmo.command.users.UsersDTO;
import org.apache.ibatis.annotations.*;

import java.util.Optional;

@Mapper
public interface UsersMapper {

    @Insert("INSERT INTO USERS VALUES (SEQ_USERS.NEXTVAL,#{id},#{password},#{name},#{nick},#{tel},DEFAULT,DEFAULT,DEFAULT,'ROLE_ADMIN')")
    @Options(useGeneratedKeys = true, keyProperty = "unum", keyColumn = "UNUM")
    int createUser(UsersDTO usersDTO);

    @Insert("INSERT INTO USERS VALUES (SEQ_USERS.NEXTVAL,#{id},#{password},#{name},'kakao','010-8598-1942',#{profile},DEFAULT,DEFAULT,DEFAULT)")
    @Options(useGeneratedKeys = true, keyProperty = "unum", keyColumn = "UNUM")
    int createKakaoUser(UsersDTO usersDTO);

    @Insert("INSERT INTO USERS VALUES (SEQ_USERS.NEXTVAL,#{id},#{password},#{name},#{nick},#{tel},#{profile},DEFAULT,DEFAULT,DEFAULT)")
    @Options(useGeneratedKeys = true, keyProperty = "unum", keyColumn = "UNUM")
    int createNaverUser(UsersDTO usersDTO);

    @Insert("INSERT INTO USERS VALUES (SEQ_USERS.NEXTVAL,#{id},#{password},#{name},'google','010-8598-1942',#{profile},DEFAULT,DEFAULT,DEFAULT)")
    @Options(useGeneratedKeys = true, keyProperty = "unum", keyColumn = "UNUM")
    int createGoogleUser(UsersDTO usersDTO);

    @Select("SELECT * FROM USERS WHERE ID = #{id}")
    Optional<UsersDTO> login(UsersDTO usersDTO);

    @Select("SELECT * FROM USERS WHERE UNUM = #{unum}")
    Optional<UsersDTO> getUserInfoByUnum(UsersDTO usersDTO);

    @Select("SELECT * FROM USERS WHERE ID = #{id}")
    Optional<UsersDTO> getUserInfoByKakaoId(UsersDTO usersDTO);

    @Select("SELECT * FROM USERS WHERE ID = #{id}")
    Optional<UsersDTO> getUserInfoByNaverId(UsersDTO usersDTO);

    @Select("SELECT * FROM USERS WHERE ID = #{id}")
    Optional<UsersDTO> getUserInfoByGoogleId(UsersDTO usersDTO);

    @Delete("DELETE FROM USERS WHERE UNUM = #{unum}")
    int deleteUser(UsersDTO usersDTO);

    @Update("UPDATE USERS SET ID = #{id} WHERE UNUM = #{unum}")
    int setUserInfo(UsersDTO usersDTO);

}// UsersMapper