package com.personal.devetblogapi.service;

import com.personal.devetblogapi.constant.MessageConst;
import com.personal.devetblogapi.entity.EmployeeEntity;
import com.personal.devetblogapi.entity.UserEntity;
import com.personal.devetblogapi.exception.ObjectException;
import com.personal.devetblogapi.exception.ResourceAlreadyExistsException;
import com.personal.devetblogapi.exception.ResourceNotFoundException;
import com.personal.devetblogapi.model.ChangePasswordRequestDto;
import com.personal.devetblogapi.model.DataResponseDto;
import com.personal.devetblogapi.repo.EmployeeRepo;
import com.personal.devetblogapi.repo.UserRepo;
import com.personal.devetblogapi.type.UserRole;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  @Autowired
  UserRepo userRepo;
  @Autowired
  EmployeeRepo employeeRepo;
  private final PasswordEncoder passwordEncoder;

  public List<UserEntity> getAllUser() {
    return userRepo.findAll();
  }

  public UserEntity getUser(String id) throws ResourceNotFoundException {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    UserEntity loggingUser = userRepo.findByEmail(auth.getName()).get();

    UserEntity responseUser = userRepo.findById(UUID.fromString(id))
        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

    if (loggingUser.getRole() == UserRole.EMPLOYEE) {
      EmployeeEntity emp = employeeRepo.findByEmail(loggingUser.getEmail()).orElseThrow(
          () -> new ObjectException("User as employee not found", HttpStatus.BAD_REQUEST, null));
    }

    return responseUser;
  }

  public UserEntity addUser(UserEntity entity) throws ResourceAlreadyExistsException {
    if (userRepo.findByEmail(entity.getEmail()).isPresent()) {
      throw new ResourceAlreadyExistsException(
          "User already exists with email: " + entity.getEmail());
    }
    return userRepo.save(entity);
  }

  public UserEntity updateUser(UserEntity newUser)
      throws ResourceNotFoundException, ResourceAlreadyExistsException {
    if (userRepo.findByEmail(newUser.getEmail()).isPresent()) {
      throw new ResourceAlreadyExistsException(
          "User already exists with email: " + newUser.getEmail());
    }
    userRepo.findById(newUser.getId()).orElseThrow(
        () -> new ResourceNotFoundException("User not found with id: " + newUser.getId()));
    newUser.setUsername(newUser.getEmail());
    if (newUser.getRightUsername() == null) {

      throw new ResourceNotFoundException("Username not found");
    }
    if (newUser.getEmail() == null) {
      throw new ResourceNotFoundException("Email not found");
    }
    if (newUser.getRole() == null) {
      throw new ResourceNotFoundException("Role not found");
    }
    if (newUser.getPassword() == null) {
      throw new ResourceNotFoundException("Password not found");
    }
    if (newUser.getState() == null) {
      throw new ResourceNotFoundException("State not found");
    }
    newUser.setUpdate_date(new Date());
    return userRepo.save(newUser);
  }

  public void deleteUser(String id) throws ResourceNotFoundException {
    Optional<UserEntity> user = userRepo.findById(UUID.fromString(id));
    if (user.isPresent()) {
      userRepo.deleteById(UUID.fromString(id));
    } else {
      throw new ResourceNotFoundException("User not found with id: " + id);
    }
  }

  public DataResponseDto changePassword(ChangePasswordRequestDto req) throws ObjectException {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    UserEntity loggingUser = userRepo.findByEmail(auth.getName()).orElse(null);

    if (!passwordEncoder.matches(req.getCurrentPassword(), loggingUser.getPassword())) {
      throw new ObjectException(MessageConst.User.PW_INCORRECT, HttpStatus.BAD_REQUEST, null);
    }

    loggingUser.setPassword(passwordEncoder.encode(req.getNewPassword()));
    loggingUser.setUpdate_date(new Date());
    userRepo.save(loggingUser);

    return DataResponseDto.success(HttpStatus.OK.value(), MessageConst.User.CHANGE_PW_OK, null);
  }
}