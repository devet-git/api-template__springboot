package com.personal.devetblogapi.controller;

import com.personal.devetblogapi.constant.EndpointConst;
import com.personal.devetblogapi.service.OrcService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = {EndpointConst.Orc.BASE_PATH})
@CrossOrigin(maxAge = 3600)
@Tag(name = "OCR", description = "The OCR API")
public class OrcController {

  @Autowired
  private final OrcService orcService;

  @Operation(summary = "Generate text from image", security = {
      @SecurityRequirement(name = "bearer-key")})
  @PostMapping(value = {
      EndpointConst.Orc.GEN_TEXT_FROM_IMG}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> genTextFromImage(
      @RequestPart("file") @RequestParam("file") MultipartFile file)
      throws IOException, TesseractException {

    var res = orcService.readTextFromImage(file);
    return ResponseEntity.ok(res);

  }
}
