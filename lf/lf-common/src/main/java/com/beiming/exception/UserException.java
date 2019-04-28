package com.beiming.exception;

@SuppressWarnings("serial")
public class UserException extends RuntimeException implements ExceptionInterface{

  private ExceptionInterface exception;

  public UserException(ExceptionInterface exception) {
    super();
    this.exception = exception;
  }

  public UserException(ExceptionInterface exception,String errMsg) {
    super();
    this.exception = exception;
    this.exception.setMeaasge(errMsg);
  }

  public UserException() {
    super();
  }

  @Override
  public Integer getCode() {
    return this.exception.getCode();
  }
  
  @Override
  public String getMessage() {
    return this.exception.getMessage();
  }

  @Override
  public ExceptionInterface setMeaasge(String message) {
    return this.exception.setMeaasge(message);
  }

}
