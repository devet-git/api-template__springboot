package com.personal.devetblogapi.controller;

import com.personal.devetblogapi.constant.EndpointConst;
import com.personal.devetblogapi.constant.MessageConst;
import com.personal.devetblogapi.model.DataResponseDto;
import com.personal.devetblogapi.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = {EndpointConst.File.BASE_PATH})
@CrossOrigin(maxAge = 3600)
@Tag(name = "File", description = "The file API")
public class FileController {

  @Autowired
  private final FileService fileService;

  @Operation(summary = "Upload multiple file", security = {
      @SecurityRequirement(name = "bearer-key")}, responses = {
      @ApiResponse(responseCode = "200", description = "Successful", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class))}),
      @ApiResponse(responseCode = "400", description = "Bad request", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class))}),})
  @PostMapping(value = {EndpointConst.File.UPLOAD}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> uploadMultipleFile(
      @RequestPart("files") @RequestParam("files") List<MultipartFile> files) throws IOException {
//    System.out.println(files);
    var res = fileService.storeAll(files);
    return ResponseEntity.ok(res);
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @Operation(summary = "Get list of files(ADMIN)", security = {
      @SecurityRequirement(name = "bearer-key")})
  @GetMapping("/admin")
  public ResponseEntity<?> getAllFileAdmin() {
    var res = fileService.getAllFile();
    return ResponseEntity.ok(
        DataResponseDto.success(HttpStatus.OK.value(), MessageConst.SUCCESS, res));
  }

  @Operation(summary = "Get list of files", security = {@SecurityRequirement(name = "bearer-key")})
  @GetMapping
  public ResponseEntity<?> getAllFileByUser() {

    var res = fileService.getAllFileByUser();
    return ResponseEntity.ok(
        DataResponseDto.success(HttpStatus.OK.value(), MessageConst.SUCCESS, res));
  }

  @Operation(summary = "Get a file by id", security = {
      @SecurityRequirement(name = "bearer-key")}, responses = {
      @ApiResponse(responseCode = "200", description = "Successful", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class))}),
      @ApiResponse(responseCode = "400", description = "Bad request", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class))}),})
  @GetMapping(value = {EndpointConst.File.GET_BY_ID})
  public ResponseEntity<?> getFileById(
      @Parameter(description = "File id", required = true) @PathVariable("id") UUID fileId) {
    var res = fileService.getFileByIdAndUserId(fileId);
    return ResponseEntity.ok()
        .body(DataResponseDto.success(HttpStatus.OK.value(), MessageConst.SUCCESS, res));
  }

  @Operation(summary = "Show image by id", security = {
      @SecurityRequirement(name = "bearer-key")}, responses = {
      @ApiResponse(responseCode = "200", description = "Successful", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class))}),
      @ApiResponse(responseCode = "400", description = "Bad request", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class))}),})
  @GetMapping(value = {EndpointConst.File.SHOW_IMAGE})
  public ResponseEntity<?> showImage(
      @Parameter(description = "File id", required = true) @PathVariable("id") UUID fileId) {
    var res = fileService.getFileDataById(fileId);

    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).contentType(MediaType.IMAGE_PNG)
        .body(res.getData());
  }

  @Operation(summary = "Show pdf by id", security = {
      @SecurityRequirement(name = "bearer-key")}, responses = {
      @ApiResponse(responseCode = "200", description = "Successful", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class))}),
      @ApiResponse(responseCode = "400", description = "Bad request", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class))}),})
  @GetMapping(value = {EndpointConst.File.SHOW_PDF})
  public ResponseEntity<?> showPdf(
      @Parameter(description = "File id", required = true) @PathVariable("id") UUID fileId) {

    var res = fileService.getFileDataById(fileId);

    InputStream inputStream = new ByteArrayInputStream(res.getData());
    InputStreamResource resource = new InputStreamResource(inputStream);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDisposition(
        ContentDisposition.builder("inline").filename(res.getId().toString()).build());

    return ResponseEntity.ok().headers(headers).contentLength(res.getData().length).body(resource);
  }

  @Operation(summary = "Get file's data by id", security = {
      @SecurityRequirement(name = "bearer-key")})
  @GetMapping(value = {EndpointConst.File.GET_DATA})
  public ResponseEntity<?> getFileData(
      @Parameter(description = "File id", required = true) @PathVariable("id") UUID fileId) {

    var res = fileService.getFileDataById(fileId);
    HttpHeaders headers = new HttpHeaders();
//    headers.setContentType(MediaType.APPLICATION_PDF);
//    headers.setContentDispositionFormData("attachment", fileId.toString());
    return ResponseEntity.ok().headers(headers).body(res.getData());
  }

  @Operation(summary = "Delete a file by id", security = {
      @SecurityRequirement(name = "bearer-key")}, responses = {
      @ApiResponse(responseCode = "200", description = "Successful", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class))}),
      @ApiResponse(responseCode = "400", description = "Bad request", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class))}),})
  @DeleteMapping(value = {EndpointConst.File.DELETE_BY_ID})
  public ResponseEntity<?> deleteById(
      @Parameter(description = "File id", required = true) @PathVariable("id") UUID fileId) {
    var res = fileService.deleteById(fileId);
    return ResponseEntity.ok().body(res);
  }

  @Operation(summary = "Download a file", security = {@SecurityRequirement(name = "bearer-key")})
  @GetMapping(value = {EndpointConst.File.DOWNLOAD})
  public ResponseEntity<?> downloadFile(
      @Parameter(description = "File id", required = true) @PathVariable("id") UUID fileId) {
    var downloadedFile = fileService.getFileDataById(fileId);
    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
            String.format("attachment; filename=%s", downloadedFile.getName()))
        .body(downloadedFile.getData());
  }
}
