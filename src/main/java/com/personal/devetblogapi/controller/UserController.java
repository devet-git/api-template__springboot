package com.personal.devetblogapi.controller;

import com.personal.devetblogapi.constant.EndpointConst;
import com.personal.devetblogapi.entity.UserEntity;
import com.personal.devetblogapi.exception.ResourceAlreadyExistsException;
import com.personal.devetblogapi.exception.ResourceNotFoundException;
import com.personal.devetblogapi.logging.LoggerManager;
import com.personal.devetblogapi.mapping.UserMapper;
import com.personal.devetblogapi.model.ChangePasswordRequestDto;
import com.personal.devetblogapi.model.DataResponseDto;
import com.personal.devetblogapi.model.UserAddingDto;
import com.personal.devetblogapi.model.UserDto;
import com.personal.devetblogapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(maxAge = 3600)
@Tag(name = "User", description = "The user API")
public class UserController {

  private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
  @Autowired
  UserService userService;


  @Operation(summary = "List all users", security = {@SecurityRequirement(name = "bearer-key")})
  @GetMapping

  public ResponseEntity<?> getAllUser() {
    LoggerManager.info("call api get /users");
    List<UserEntity> userEntities = userService.getAllUser();
    List<UserDto> usersDto = userMapper.userEntityToUserDto(userEntities);

    return ResponseEntity.ok(usersDto);
  }

  @Operation(summary = "Get user by id", security = {
      @SecurityRequirement(name = "bearer-key")}, responses = {
      @ApiResponse(responseCode = "200", description = "Successful", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class))}),
      @ApiResponse(responseCode = "400", description = "Bad request", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class))}),})
  @GetMapping("/{id}")
  public ResponseEntity<?> getUser(@PathVariable String id) throws ResourceNotFoundException {
    LoggerManager.info("call api get /users/id");
    UserEntity userEntity;
    try {
      userEntity = userService.getUser(id);
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }

    UserDto userDto = userMapper.userEntityToUserDto(userEntity);
    return ResponseEntity.ok(userDto);
  }

  @Operation(summary = "Add user", security = {
      @SecurityRequirement(name = "bearer-key")}, responses = {
      @ApiResponse(responseCode = "200", description = "Successful", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class))}),
      @ApiResponse(responseCode = "400", description = "Bad request", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class))}),})
  @PostMapping
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<?> addUser(@RequestBody UserAddingDto user)
      throws ResourceAlreadyExistsException {
    LoggerManager.info("call api Post /users");
    UserEntity userEntity = userMapper.addingUserToUserEntity(user);
    try {
      userService.addUser(userEntity);
    } catch (ResourceAlreadyExistsException e) {
      return ResponseEntity.ok(ResponseEntity.ok(e.getMessage()));
    }

    return ResponseEntity.ok(userEntity);
  }

  @Operation(summary = "Update user by id", security = {
      @SecurityRequirement(name = "bearer-key")}, responses = {
      @ApiResponse(responseCode = "200", description = "Successful", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class))}),
      @ApiResponse(responseCode = "400", description = "Bad request", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class))}),})
  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody UserAddingDto user)
      throws ResourceNotFoundException {
    LoggerManager.info("call api Put /users");
    UserEntity userEntity = userMapper.addingUserToUserEntity(user);
    userEntity.setId(UUID.fromString(id));
    try {
      userService.updateUser(userEntity);
    } catch (ResourceAlreadyExistsException | ResourceNotFoundException e) {
      return ResponseEntity.ok(e.getMessage());
    }
    return ResponseEntity.ok(userEntity);
  }

  @Operation(summary = "Delete user by id", security = {
      @SecurityRequirement(name = "bearer-key")}, responses = {
      @ApiResponse(responseCode = "200", description = "Successful", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class))}),
      @ApiResponse(responseCode = "400", description = "Bad request", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class))}),})
  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<?> deleteUser(@PathVariable String id) throws ResourceNotFoundException {
    LoggerManager.info("call api Delete /user");
    try {
      userService.deleteUser(id);
      return new ResponseEntity<>("User has been deleted successfully", HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @Operation(summary = "Change password", security = {
      @SecurityRequirement(name = "bearer-key")}, responses = {
      @ApiResponse(responseCode = "200", description = "Successful", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class))}),
      @ApiResponse(responseCode = "400", description = "Bad request", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class))}),})
  @PostMapping(EndpointConst.User.CHANGE_PASSWORD)
  public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequestDto req) {
    var res = userService.changePassword(req);
    return ResponseEntity.ok(res);
  }
}
