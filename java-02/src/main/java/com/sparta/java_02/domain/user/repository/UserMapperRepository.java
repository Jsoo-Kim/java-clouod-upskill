package com.sparta.java_02.domain.user.repository;

import com.sparta.java_02.domain.user.dto.SearchUserDto;
import com.sparta.java_02.domain.user.dto.UserDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapperRepository {

  SearchUserDto getUserById(Long id);

  void insertUser(@Param("users") List<UserDto> users);

  void updateUser(UserDto user);

  void deleteUser(Long id);

}
