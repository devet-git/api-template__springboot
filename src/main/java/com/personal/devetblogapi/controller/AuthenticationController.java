package com.personal.devetblogapi.controller;

import com.personal.devetblogapi.constant.EndpointConst;
import com.personal.devetblogapi.constant.MessageConst;
import com.personal.devetblogapi.model.AuthenticationRequestDto;
import com.personal.devetblogapi.model.DataResponseDto;
import com.personal.devetblogapi.model.RegisterRequestDto;
import com.personal.devetblogapi.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = EndpointConst.AUTH_BASE_PATH)
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "The authentication API")
@CrossOrigin(maxAge = 3600)
public class AuthenticationController {

  @Autowired
  private final AuthenticationService authenticationService;

  @GetMapping("/test")
  public ResponseEntity<?> test() {
    return ResponseEntity.ok().body("Test ahah");
  }

  @Operation(summary = "Register", responses = {
      @ApiResponse(responseCode = "200", description = "Successful", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class))}),
      @ApiResponse(responseCode = "400", description = "Bad request", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class))}),})
  @PostMapping(value = {EndpointConst.REGISTER})
  public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDto req) {
    var response = authenticationService.register(req);
    return ResponseEntity.ok(
        DataResponseDto.success(HttpStatus.OK.value(), MessageConst.SUCCESS, response));
  }

  @Operation(summary = "Login", responses = {
      @ApiResponse(responseCode = "200", description = "Successful", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class))}),
      @ApiResponse(responseCode = "400", description = "Bad request", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class))}),})
  @PostMapping(value = {EndpointConst.LOGIN})
  public ResponseEntity<?> login(@Valid @RequestBody AuthenticationRequestDto req) {
    var response = authenticationService.authenticate(req);
    return ResponseEntity.ok(
        DataResponseDto.success(HttpStatus.OK.value(), MessageConst.SUCCESS, response));
  }

  @GetMapping(value = {EndpointConst.LOGOUT})
  public ResponseEntity<?> logout() {
    return ResponseEntity.ok(
        DataResponseDto.success(HttpStatus.OK.value(), MessageConst.SUCCESS, null));
  }
}
