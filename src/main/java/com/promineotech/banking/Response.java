package com.promineotech.banking;

/**
 * A wrapper for an action or response.
 * @param <T> The type of data being wrapped.
 */
public class Response<T> {
  public static final int OK = 200;
  public static final int BAD_REQUEST = 400;
  public static final int NOT_FOUND = 404;
  public static final int SERVER_ERROR = 500;
  
  private int code;
  private String message;
  private T result;

  protected Response(int code, T result, String message) {
    this.code = code;
    this.result = result;
    this.message = message;
  }

  public static <T> Response<T> OK(T result) {
    return(OK(result, null));
  }
  public static <T> Response<T> OK(T result, String message) {
    return(new Response<T>(OK, result, message));
  }
  public static <T> Response<T> NotFound(String message) {
    return(NotFound(NOT_FOUND, message));
  }
  public static <T> Response<T> NotFound(int code, String message) {
    return(new Response<T>(code, null, message));
  }
  public static <T> Response<T> BadRequest(String message) {
    return(BadRequest(null, message));
  }
  public static <T> Response<T> BadRequest(T result) {
    return(BadRequest(result, null));
  }
  public static <T> Response<T> BadRequest(T result, String message) {
    return(new Response<T>(BAD_REQUEST, result, message));
  }
  public static <T> Response<T> Error(String message) {
    return(Error(SERVER_ERROR, message));
  }
  public static <T> Response<T> Error(T result) {
    return(Error(result, null));
  }
  public static <T> Response<T> Error(T result, String message) {
    return(new Response<T>(SERVER_ERROR, result, message));
  }
  public static <T> Response<T> Error(int code, String message) {
    return(new Response<T>(code, null, message));
  }
  public static <T> Response<T> Create(int code, String message) {
    return(new Response<T>(code, null, message));
  }
  public static <T> Response<T> Create(int code, T result, String message) {
    return(new Response<T>(code, result, message));
  }

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public T get() {
    return result;
  }
}
