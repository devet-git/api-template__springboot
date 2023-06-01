package com.personal.devetblogapi.config;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimeZoneConfig {

  @PostConstruct
  public void init() {
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
//    TimeZone.setDefault(TimeZone.getTimeZone(TimeZone.getDefault().getID()));
  }

}
