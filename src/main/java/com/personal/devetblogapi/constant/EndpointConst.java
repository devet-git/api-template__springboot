package com.personal.devetblogapi.constant;

public final class EndpointConst {

  private static final String ROOT_V1 = "/api/v1";

  public static final class Orc {

    public static final String BASE_PATH = ROOT_V1 + "/orc";
    public static final String GEN_TEXT_FROM_IMG = "image";
  }

  public static final class Mail {

    public static final String BASE_PATH = ROOT_V1 + "/mails";
    public static final String SEND = "";
  }

  public static final class Employee {

    public static final String BASE_PATH = ROOT_V1 + "/employees";
    public static final String LIST = "/batch";
    public static final String SEARCH = "/search";
    public static final String DELETE_BY_ID = "{id}";
    public static final String UPDATE_BY_ID = "{id}";
    public static final String GET_BY_ID = "{id}";
    public static final String EXPORT_EXCEL = "export";
  }


  public static final String AUTH_BASE_PATH = ROOT_V1 + "/auth";
  public static final String REGISTER = "/register";
  public static final String LOGIN = "/login";
  public static final String LOGOUT = "/logout";


  public static final class File {

    public static final String BASE_PATH = ROOT_V1 + "/files";
    public static final String UPLOAD = "";
    public static final String GET_BY_ID = "{id}";
    public static final String SHOW_IMAGE = "images/{id}";
    public static final String SHOW_PDF = "pdf/{id}";
    public static final String GET_DATA = "data/{id}";
    public static final String DELETE_BY_ID = "{id}";
    public static final String UPLOAD_ONE = "/upload";
    public static final String DOWNLOAD = "/download/{id}";
  }

  public static final class User {

    public static final String CHANGE_PASSWORD = "/change-password";
  }
}
