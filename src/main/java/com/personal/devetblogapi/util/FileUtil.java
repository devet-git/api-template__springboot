package com.personal.devetblogapi.util;

import com.personal.devetblogapi.constant.EndpointConst;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public final class FileUtil {

  public static String getFileDownloadUri(String fileId) {
    return ServletUriComponentsBuilder.fromCurrentContextPath().path(EndpointConst.File.BASE_PATH)
        .path("/download/").path(fileId).toUriString();
  }
}
