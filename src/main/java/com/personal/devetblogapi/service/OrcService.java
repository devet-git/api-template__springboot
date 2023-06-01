package com.personal.devetblogapi.service;


import com.personal.devetblogapi.model.DataResponseDto;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * The type Logout service.
 */
@Service
@RequiredArgsConstructor
public class OrcService {

  @Autowired
  private Tesseract tesseract;


  public DataResponseDto readTextFromImage(MultipartFile file)
      throws IOException, TesseractException {

    String result = tesseract.doOCR(convert(file));

    return DataResponseDto.success(HttpStatus.OK.value(), HttpStatus.OK.name(), result);
  }

  public static File convert(MultipartFile file) throws IOException {
    File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
    convFile.createNewFile();
    FileOutputStream fos = new FileOutputStream(convFile);
    fos.write(file.getBytes());
    fos.close();

    return convFile;
  }
}
