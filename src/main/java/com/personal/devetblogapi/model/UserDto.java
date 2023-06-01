package com.personal.devetblogapi.model;

import com.personal.devetblogapi.type.UserRole;
import com.personal.devetblogapi.type.UserState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private UUID id;
    private String email;
    private UserRole role;
    private String username;
    private UserState state;
    private Date create_date;
    private Date update_date;
}
