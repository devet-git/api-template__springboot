package com.personal.devetblogapi.config;

import net.sourceforge.tess4j.Tesseract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OCRConfig {

  @Bean
  public Tesseract tesseract() {
    Tesseract tesseract = new Tesseract();
    tesseract.setDatapath("src/main/resources/tessdata");
    tesseract.setLanguage("vie");
//    tesseract.setPageSegMode(1);
//    tesseract.setOcrEngineMode(1);
    return tesseract;
  }

}
