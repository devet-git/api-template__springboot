package com.personal.devetblogapi.mapping;

import com.personal.devetblogapi.entity.UserEntity;
import com.personal.devetblogapi.model.UserAddingDto;
import com.personal.devetblogapi.model.UserDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    UserDto userEntityToUserDto(UserEntity userEntity);
    UserEntity userDtoToUserEntity(UserDto userDto);
    List<UserDto> userEntityToUserDto(List<UserEntity> userEntity);
    List<UserEntity> userDtoToUserEntity(List<UserDto> userDto);
    UserEntity addingUserToUserEntity(UserAddingDto userAddingDto);
}
